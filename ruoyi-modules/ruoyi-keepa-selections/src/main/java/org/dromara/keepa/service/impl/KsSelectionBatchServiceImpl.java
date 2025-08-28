package org.dromara.keepa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.keepa.domain.KsSelectionBatch;
import org.dromara.keepa.domain.bo.KsSelectionBatchBo;
import org.dromara.keepa.domain.vo.KsSelectionBatchVo;
import org.dromara.keepa.mapper.KsAmazonDomainMapper;
import org.dromara.keepa.mapper.KsProductAsinMapper;
import org.dromara.keepa.mapper.KsSelectionBatchMapper;
import org.dromara.keepa.service.IKsSelectionBatchService;
import org.dromara.keepa.service.IKeepaApiService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 选品批次Service业务层处理
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KsSelectionBatchServiceImpl implements IKsSelectionBatchService {

    private final KsSelectionBatchMapper baseMapper;
    private final KsAmazonDomainMapper amazonDomainMapper;
    private final KsProductAsinMapper productAsinMapper;
    private final IKeepaApiService keepaApiService;

    @Override
    public KsSelectionBatchVo queryById(Long id) {
        KsSelectionBatchVo vo = baseMapper.selectVoById(id);
        if (vo != null) {
            // 设置域名名称
            if (vo.getAmazonDomain() != null) {
                var domain = amazonDomainMapper.selectOne("domain_code", vo.getAmazonDomain());
                if (domain != null) {
                    vo.setAmazonDomainName(domain.getDomainName());
                }
            }
            
            // 设置操作权限
            setOperationPermissions(vo);
        }
        return vo;
    }

    @Override
    public TableDataInfo<KsSelectionBatchVo> queryPageList(KsSelectionBatchBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KsSelectionBatch> lqw = buildQueryWrapper(bo);
        Page<KsSelectionBatchVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        
        // 设置扩展信息
        for (KsSelectionBatchVo vo : result.getRecords()) {
            if (vo.getAmazonDomain() != null) {
                var domain = amazonDomainMapper.selectOne("domain_code", vo.getAmazonDomain());
                if (domain != null) {
                    vo.setAmazonDomainName(domain.getDomainName());
                }
            }
            setOperationPermissions(vo);
        }
        
        return TableDataInfo.build(result);
    }

    @Override
    public List<KsSelectionBatchVo> queryList(KsSelectionBatchBo bo) {
        LambdaQueryWrapper<KsSelectionBatch> lqw = buildQueryWrapper(bo);
        List<KsSelectionBatchVo> list = baseMapper.selectVoList(lqw);
        
        // 设置扩展信息
        for (KsSelectionBatchVo vo : list) {
            if (vo.getAmazonDomain() != null) {
                var domain = amazonDomainMapper.selectOne("domain_code", vo.getAmazonDomain());
                if (domain != null) {
                    vo.setAmazonDomainName(domain.getDomainName());
                }
            }
            setOperationPermissions(vo);
        }
        
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(KsSelectionBatchBo bo) {
        KsSelectionBatch add = MapstructUtils.convert(bo, KsSelectionBatch.class);
        validEntityBeforeSave(add);
        
        // 生成批次编码
        if (StrUtil.isBlank(add.getBatchCode())) {
            add.setBatchCode(generateBatchCode());
        }
        
        // 设置初始状态
        add.setStatus(KsSelectionBatch.BatchStatus.INITIAL);
        add.setCurrentAsinCount(0);
        add.setCurrentDetailCount(0);
        
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(KsSelectionBatchBo bo) {
        KsSelectionBatch update = MapstructUtils.convert(bo, KsSelectionBatch.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 验证是否可以删除
            for (Long id : ids) {
                KsSelectionBatch batch = baseMapper.selectById(id);
                if (batch != null && !canDelete(batch)) {
                    throw new RuntimeException("批次 " + batch.getBatchName() + " 正在执行中，无法删除");
                }
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createBatch(String batchName, String description, Integer amazonDomain, 
                           String categoryIds, Integer maxAsinCount) {
        
        // 验证参数
        if (StrUtil.isBlank(batchName)) {
            throw new IllegalArgumentException("批次名称不能为空");
        }
        if (amazonDomain == null) {
            throw new IllegalArgumentException("亚马逊站点不能为空");
        }
        if (StrUtil.isBlank(categoryIds)) {
            throw new IllegalArgumentException("类目不能为空");
        }
        
        // 验证域名是否存在
        var domain = amazonDomainMapper.selectOne("domain_code", amazonDomain);
        if (domain == null) {
            throw new IllegalArgumentException("不支持的亚马逊站点");
        }
        
        // 创建批次
        KsSelectionBatch batch = new KsSelectionBatch();
        batch.setBatchName(batchName);
        batch.setBatchCode(generateBatchCode());
        batch.setDescription(description);
        batch.setStatus(KsSelectionBatch.BatchStatus.INITIAL);
        batch.setAmazonDomain(amazonDomain);
        batch.setCategoryIds(categoryIds);
        batch.setMaxAsinCount(maxAsinCount != null ? maxAsinCount : 5000);
        batch.setCurrentAsinCount(0);
        batch.setCurrentDetailCount(0);
        
        baseMapper.insert(batch);
        
        log.info("创建选品批次成功，ID: {}, 名称: {}", batch.getId(), batchName);
        return batch.getId();
    }

    @Override
    @Async("asyncExecutor")
    public void startFetchAsinList(Long batchId) {
        log.info("开始抓取ASIN列表，批次ID: {}", batchId);
        
        try {
            // 更新状态
            updateBatchStatus(batchId, KsSelectionBatch.BatchStatus.FETCHING_ASIN);
            
            // TODO: 实现ASIN列表抓取逻辑
            // 这里应该调用ASIN抓取服务
            
            log.info("ASIN列表抓取完成，批次ID: {}", batchId);
            
        } catch (Exception e) {
            log.error("抓取ASIN列表失败，批次ID: {}, 错误: {}", batchId, e.getMessage(), e);
            updateBatchStatus(batchId, KsSelectionBatch.BatchStatus.FAILED, e.getMessage());
        }
    }

    @Override
    @Async("asyncExecutor")
    public void startFetchProductDetails(Long batchId) {
        log.info("开始抓取商品详情，批次ID: {}", batchId);
        
        try {
            // 更新状态
            updateBatchStatus(batchId, KsSelectionBatch.BatchStatus.FETCHING_DETAIL);
            
            // TODO: 实现商品详情抓取逻辑
            // 这里应该调用商品详情抓取服务
            
            log.info("商品详情抓取完成，批次ID: {}", batchId);
            
        } catch (Exception e) {
            log.error("抓取商品详情失败，批次ID: {}, 错误: {}", batchId, e.getMessage(), e);
            updateBatchStatus(batchId, KsSelectionBatch.BatchStatus.FAILED, e.getMessage());
        }
    }

    @Override
    @Async("asyncExecutor")
    public void startAnalyzeProducts(Long batchId) {
        log.info("开始分析商品数据，批次ID: {}", batchId);
        
        try {
            // 更新状态
            updateBatchStatus(batchId, KsSelectionBatch.BatchStatus.ANALYZING);
            
            // TODO: 实现商品数据分析逻辑
            // 这里应该调用分析服务
            
            log.info("商品数据分析完成，批次ID: {}", batchId);
            updateBatchStatus(batchId, KsSelectionBatch.BatchStatus.COMPLETED);
            
        } catch (Exception e) {
            log.error("分析商品数据失败，批次ID: {}, 错误: {}", batchId, e.getMessage(), e);
            updateBatchStatus(batchId, KsSelectionBatch.BatchStatus.FAILED, e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchStatus(Long batchId, String status) {
        updateBatchStatus(batchId, status, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchStatus(Long batchId, String status, String errorMessage) {
        KsSelectionBatch batch = new KsSelectionBatch();
        batch.setId(batchId);
        batch.setStatus(status);
        batch.setErrorMessage(errorMessage);
        
        if (KsSelectionBatch.BatchStatus.FETCHING_ASIN.equals(status)) {
            batch.setStartTime(LocalDateTime.now());
        } else if (KsSelectionBatch.BatchStatus.COMPLETED.equals(status) || 
                   KsSelectionBatch.BatchStatus.FAILED.equals(status)) {
            batch.setEndTime(LocalDateTime.now());
        }
        
        baseMapper.updateById(batch);
    }

    @Override
    public KsSelectionBatchVo getBatchProgress(Long batchId) {
        KsSelectionBatchVo vo = queryById(batchId);
        if (vo != null) {
            // 实时更新统计数据
            int asinCount = productAsinMapper.countByBatchId(batchId);
            int detailCount = productAsinMapper.countCompletedDetailByBatchId(batchId);
            
            vo.setCurrentAsinCount(asinCount);
            vo.setCurrentDetailCount(detailCount);
        }
        return vo;
    }

    @Override
    public boolean canExecuteOperation(Long batchId, String operation) {
        KsSelectionBatch batch = baseMapper.selectById(batchId);
        if (batch == null) {
            return false;
        }
        
        switch (operation) {
            case "FETCH_ASIN":
                return KsSelectionBatch.BatchStatus.INITIAL.equals(batch.getStatus()) ||
                       KsSelectionBatch.BatchStatus.FAILED.equals(batch.getStatus());
            case "FETCH_DETAIL":
                return KsSelectionBatch.BatchStatus.INITIAL.equals(batch.getStatus()) && 
                       batch.getCurrentAsinCount() > 0;
            case "ANALYZE":
                return KsSelectionBatch.BatchStatus.INITIAL.equals(batch.getStatus()) && 
                       batch.getCurrentDetailCount() > 0;
            default:
                return false;
        }
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<KsSelectionBatch> buildQueryWrapper(KsSelectionBatchBo bo) {
        Map<String, Object> params = BeanUtil.beanToMap(bo, false, true);
        LambdaQueryWrapper<KsSelectionBatch> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getBatchName()), KsSelectionBatch::getBatchName, bo.getBatchName());
        lqw.eq(StringUtils.isNotBlank(bo.getBatchCode()), KsSelectionBatch::getBatchCode, bo.getBatchCode());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), KsSelectionBatch::getStatus, bo.getStatus());
        lqw.eq(bo.getAmazonDomain() != null, KsSelectionBatch::getAmazonDomain, bo.getAmazonDomain());
        lqw.orderByDesc(KsSelectionBatch::getCreateTime);
        return lqw;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KsSelectionBatch entity) {
        // 校验批次名称唯一性
        if (StrUtil.isNotBlank(entity.getBatchName())) {
            LambdaQueryWrapper<KsSelectionBatch> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(KsSelectionBatch::getBatchName, entity.getBatchName());
            if (entity.getId() != null) {
                wrapper.ne(KsSelectionBatch::getId, entity.getId());
            }
            if (baseMapper.selectCount(wrapper) > 0) {
                throw new RuntimeException("批次名称已存在");
            }
        }
    }

    /**
     * 生成批次编码
     */
    private String generateBatchCode() {
        return "BATCH_" + System.currentTimeMillis() + "_" + IdUtil.fastSimpleUUID().substring(0, 8);
    }

    /**
     * 判断是否可以删除
     */
    private boolean canDelete(KsSelectionBatch batch) {
        return !KsSelectionBatch.BatchStatus.FETCHING_ASIN.equals(batch.getStatus()) &&
               !KsSelectionBatch.BatchStatus.FETCHING_DETAIL.equals(batch.getStatus()) &&
               !KsSelectionBatch.BatchStatus.ANALYZING.equals(batch.getStatus());
    }

    /**
     * 设置操作权限
     */
    private void setOperationPermissions(KsSelectionBatchVo vo) {
        if (vo == null || vo.getId() == null) {
            return;
        }
        
        vo.setCanStartFetchAsin(canExecuteOperation(vo.getId(), "FETCH_ASIN"));
        vo.setCanStartFetchDetail(canExecuteOperation(vo.getId(), "FETCH_DETAIL"));
        vo.setCanStartAnalyze(canExecuteOperation(vo.getId(), "ANALYZE"));
    }
}

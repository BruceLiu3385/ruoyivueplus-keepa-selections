package org.dromara.keepa.service;

import org.dromara.keepa.domain.KsSelectionBatch;
import org.dromara.keepa.domain.vo.KsSelectionBatchVo;
import org.dromara.keepa.domain.bo.KsSelectionBatchBo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 选品批次Service接口
 *
 * @author ruoyi
 * @date 2025-01-02
 */
public interface IKsSelectionBatchService {

    /**
     * 查询选品批次
     */
    KsSelectionBatchVo queryById(Long id);

    /**
     * 查询选品批次列表
     */
    TableDataInfo<KsSelectionBatchVo> queryPageList(KsSelectionBatchBo bo, PageQuery pageQuery);

    /**
     * 查询选品批次列表
     */
    List<KsSelectionBatchVo> queryList(KsSelectionBatchBo bo);

    /**
     * 新增选品批次
     */
    Boolean insertByBo(KsSelectionBatchBo bo);

    /**
     * 修改选品批次
     */
    Boolean updateByBo(KsSelectionBatchBo bo);

    /**
     * 校验并批量删除选品批次信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 创建选品批次
     */
    Long createBatch(String batchName, String description, Integer amazonDomain, String categoryIds, Integer maxAsinCount);

    /**
     * 开始抓取ASIN列表
     */
    void startFetchAsinList(Long batchId);

    /**
     * 开始抓取商品详情
     */
    void startFetchProductDetails(Long batchId);

    /**
     * 开始分析选品数据
     */
    void startAnalyzeProducts(Long batchId);

    /**
     * 更新批次状态
     */
    void updateBatchStatus(Long batchId, String status);

    /**
     * 更新批次状态和错误信息
     */
    void updateBatchStatus(Long batchId, String status, String errorMessage);

    /**
     * 获取批次进度信息
     */
    KsSelectionBatchVo getBatchProgress(Long batchId);

    /**
     * 检查批次是否可以执行指定操作
     */
    boolean canExecuteOperation(Long batchId, String operation);
}

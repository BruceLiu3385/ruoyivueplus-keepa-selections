package org.dromara.keepa.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.keepa.domain.bo.KsSelectionBatchBo;
import org.dromara.keepa.domain.vo.KsSelectionBatchVo;
import org.dromara.keepa.service.IKsSelectionBatchService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Arrays;

/**
 * 选品批次管理
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Tag(name = "选品批次管理", description = "Keepa亚马逊选品批次的增删改查操作")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/keepa/batch")
public class KsSelectionBatchController extends BaseController {

    private final IKsSelectionBatchService ksSelectionBatchService;

    /**
     * 查询选品批次列表
     */
    @Operation(summary = "查询选品批次列表", description = "分页查询选品批次信息")
    @SaCheckPermission("keepa:batch:list")
    @GetMapping("/list")
    public TableDataInfo<KsSelectionBatchVo> list(KsSelectionBatchBo bo, PageQuery pageQuery) {
        return ksSelectionBatchService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出选品批次列表
     */
    @SaCheckPermission("keepa:batch:export")
    @Log(title = "选品批次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KsSelectionBatchBo bo, HttpServletResponse response) {
        List<KsSelectionBatchVo> list = ksSelectionBatchService.queryList(bo);
        ExcelUtil.exportExcel(list, "选品批次", KsSelectionBatchVo.class, response);
    }

    /**
     * 获取选品批次详细信息
     */
    @SaCheckPermission("keepa:batch:query")
    @GetMapping("/{id}")
    public R<KsSelectionBatchVo> getInfo(@NotNull(message = "主键不能为空")
                                         @PathVariable Long id) {
        return R.ok(ksSelectionBatchService.queryById(id));
    }

    /**
     * 新增选品批次
     */
    @SaCheckPermission("keepa:batch:add")
    @Log(title = "选品批次", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KsSelectionBatchBo bo) {
        return toAjax(ksSelectionBatchService.insertByBo(bo));
    }

    /**
     * 修改选品批次
     */
    @SaCheckPermission("keepa:batch:edit")
    @Log(title = "选品批次", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KsSelectionBatchBo bo) {
        return toAjax(ksSelectionBatchService.updateByBo(bo));
    }

    /**
     * 删除选品批次
     */
    @SaCheckPermission("keepa:batch:remove")
    @Log(title = "选品批次", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(ksSelectionBatchService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 创建选品批次
     */
    @SaCheckPermission("keepa:batch:create")
    @Log(title = "创建选品批次", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public R<Long> createBatch(@RequestBody CreateBatchRequest request) {
        Long batchId = ksSelectionBatchService.createBatch(
            request.getBatchName(),
            request.getDescription(),
            request.getAmazonDomain(),
            request.getCategoryIds(),
            request.getMaxAsinCount()
        );
        return R.ok(batchId);
    }

    /**
     * 开始抓取ASIN列表
     */
    @SaCheckPermission("keepa:batch:fetchAsin")
    @Log(title = "抓取ASIN列表", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/fetch-asin")
    public R<Void> startFetchAsin(@NotNull(message = "批次ID不能为空") @PathVariable Long id) {
        if (!ksSelectionBatchService.canExecuteOperation(id, "FETCH_ASIN")) {
            return R.fail("当前状态不允许抓取ASIN");
        }
        ksSelectionBatchService.startFetchAsinList(id);
        return R.ok();
    }

    /**
     * 开始抓取商品详情
     */
    @SaCheckPermission("keepa:batch:fetchDetail")
    @Log(title = "抓取商品详情", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/fetch-detail")
    public R<Void> startFetchDetail(@NotNull(message = "批次ID不能为空") @PathVariable Long id) {
        if (!ksSelectionBatchService.canExecuteOperation(id, "FETCH_DETAIL")) {
            return R.fail("当前状态不允许抓取商品详情");
        }
        ksSelectionBatchService.startFetchProductDetails(id);
        return R.ok();
    }

    /**
     * 开始分析商品数据
     */
    @SaCheckPermission("keepa:batch:analyze")
    @Log(title = "分析商品数据", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/analyze")
    public R<Void> startAnalyze(@NotNull(message = "批次ID不能为空") @PathVariable Long id) {
        if (!ksSelectionBatchService.canExecuteOperation(id, "ANALYZE")) {
            return R.fail("当前状态不允许分析商品数据");
        }
        ksSelectionBatchService.startAnalyzeProducts(id);
        return R.ok();
    }

    /**
     * 获取批次进度
     */
    @SaCheckPermission("keepa:batch:progress")
    @GetMapping("/{id}/progress")
    public R<KsSelectionBatchVo> getBatchProgress(@NotNull(message = "批次ID不能为空") @PathVariable Long id) {
        return R.ok(ksSelectionBatchService.getBatchProgress(id));
    }

    /**
     * 创建批次请求对象
     */
    public static class CreateBatchRequest {
        private String batchName;
        private String description;
        private Integer amazonDomain;
        private String categoryIds;
        private Integer maxAsinCount;

        // Getters and Setters
        public String getBatchName() {
            return batchName;
        }

        public void setBatchName(String batchName) {
            this.batchName = batchName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getAmazonDomain() {
            return amazonDomain;
        }

        public void setAmazonDomain(Integer amazonDomain) {
            this.amazonDomain = amazonDomain;
        }

        public String getCategoryIds() {
            return categoryIds;
        }

        public void setCategoryIds(String categoryIds) {
            this.categoryIds = categoryIds;
        }

        public Integer getMaxAsinCount() {
            return maxAsinCount;
        }

        public void setMaxAsinCount(Integer maxAsinCount) {
            this.maxAsinCount = maxAsinCount;
        }
    }
}

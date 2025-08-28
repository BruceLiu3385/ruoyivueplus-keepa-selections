package org.dromara.keepa.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelEnumFormat;
import org.dromara.common.excel.convert.ExcelEnumConvert;
import org.dromara.keepa.domain.enums.BatchStatusEnum;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 选品批次对象 ks_selection_batch
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ks_selection_batch")
@ExcelIgnoreUnannotated
public class KsSelectionBatch extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 批次ID
     */
    @ExcelProperty(value = "批次ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 批次名称
     */
    @ExcelProperty(value = "批次名称")
    @TableField("batch_name")
    private String batchName;

    /**
     * 批次编码
     */
    @ExcelProperty(value = "批次编码")
    @TableField("batch_code")
    private String batchCode;

    /**
     * 批次描述
     */
    @ExcelProperty(value = "批次描述")
    @TableField("description")
    private String description;

    /**
     * 状态(0初始,1配置中,2抓取ASIN中,3抓取详情中,4分析中,5完成,9失败)
     */
    @ExcelProperty(value = "状态", converter = ExcelEnumConvert.class)
    @ExcelEnumFormat(enumClass = BatchStatusEnum.class, textField = "info")
    @TableField("status")
    private String status;

    /**
     * 亚马逊站点域名
     */
    @ExcelProperty(value = "亚马逊站点")
    @TableField("amazon_domain")
    private Integer amazonDomain;

    /**
     * 选择的类目ID列表(JSON格式)
     */
    @TableField("category_ids")
    private String categoryIds;

    /**
     * 最大ASIN数量
     */
    @ExcelProperty(value = "最大ASIN数量")
    @TableField("max_asin_count")
    private Integer maxAsinCount;

    /**
     * 当前ASIN数量
     */
    @ExcelProperty(value = "当前ASIN数量")
    @TableField("current_asin_count")
    private Integer currentAsinCount;

    /**
     * 当前详情数量
     */
    @ExcelProperty(value = "当前详情数量")
    @TableField("current_detail_count")
    private Integer currentDetailCount;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "开始时间")
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "结束时间")
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    // 批次状态常量（保留兼容性）
    public static class BatchStatus {
        public static final String INITIAL = BatchStatusEnum.INITIAL.getCode();        // 初始
        public static final String CONFIGURING = BatchStatusEnum.CONFIGURING.getCode();    // 配置中
        public static final String FETCHING_ASIN = BatchStatusEnum.FETCHING_ASIN.getCode();  // 抓取ASIN中
        public static final String FETCHING_DETAIL = BatchStatusEnum.FETCHING_DETAIL.getCode(); // 抓取详情中
        public static final String ANALYZING = BatchStatusEnum.ANALYZING.getCode();      // 分析中
        public static final String COMPLETED = BatchStatusEnum.COMPLETED.getCode();      // 完成
        public static final String FAILED = BatchStatusEnum.FAILED.getCode();         // 失败
    }
}

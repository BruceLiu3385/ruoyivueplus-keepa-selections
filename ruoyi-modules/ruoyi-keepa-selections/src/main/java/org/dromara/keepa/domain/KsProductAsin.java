package org.dromara.keepa.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;

import java.io.Serial;

/**
 * 商品ASIN对象 ks_product_asin
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ks_product_asin")
@ExcelIgnoreUnannotated
public class KsProductAsin extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 批次ID
     */
    @ExcelProperty(value = "批次ID")
    @TableField("batch_id")
    private Long batchId;

    /**
     * ASIN
     */
    @ExcelProperty(value = "ASIN")
    @TableField("asin")
    private String asin;

    /**
     * 类目ID
     */
    @ExcelProperty(value = "类目ID")
    @TableField("category_id")
    private String categoryId;

    /**
     * 域名编码
     */
    @ExcelProperty(value = "域名编码")
    @TableField("domain_code")
    private Integer domainCode;

    /**
     * 销售排名
     */
    @ExcelProperty(value = "销售排名")
    @TableField("sales_rank")
    private Integer salesRank;

    /**
     * 详情状态(0未抓取,1抓取中,2已完成,9失败)
     */
    @ExcelProperty(value = "详情状态")
    @TableField("detail_status")
    private String detailStatus;

    /**
     * 分析状态(0未分析,1已分析)
     */
    @ExcelProperty(value = "分析状态")
    @TableField("analysis_status")
    private String analysisStatus;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    // 详情状态常量
    public static class DetailStatus {
        public static final String NOT_FETCHED = "0";  // 未抓取
        public static final String FETCHING = "1";     // 抓取中
        public static final String COMPLETED = "2";    // 已完成
        public static final String FAILED = "9";       // 失败
    }

    // 分析状态常量
    public static class AnalysisStatus {
        public static final String NOT_ANALYZED = "0"; // 未分析
        public static final String ANALYZED = "1";     // 已分析
    }
}

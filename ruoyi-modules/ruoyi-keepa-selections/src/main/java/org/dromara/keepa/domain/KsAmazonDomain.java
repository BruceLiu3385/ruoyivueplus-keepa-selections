package org.dromara.keepa.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;

import java.io.Serial;
import java.io.Serializable;

/**
 * 亚马逊域名站点对象 ks_amazon_domain
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Data
@TableName("ks_amazon_domain")
@ExcelIgnoreUnannotated
public class KsAmazonDomain implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 域名ID
     */
    @ExcelProperty(value = "域名ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 域名编码
     */
    @ExcelProperty(value = "域名编码")
    @TableField("domain_code")
    private Integer domainCode;

    /**
     * 域名名称
     */
    @ExcelProperty(value = "域名名称")
    @TableField("domain_name")
    private String domainName;

    /**
     * 国家代码
     */
    @ExcelProperty(value = "国家代码")
    @TableField("country_code")
    private String countryCode;

    /**
     * 域名URL
     */
    @ExcelProperty(value = "域名URL")
    @TableField("domain_url")
    private String domainUrl;

    /**
     * 货币代码
     */
    @ExcelProperty(value = "货币代码")
    @TableField("currency")
    private String currency;

    /**
     * 语言代码
     */
    @ExcelProperty(value = "语言代码")
    @TableField("language")
    private String language;

    /**
     * 状态(0正常,1停用)
     */
    @ExcelProperty(value = "状态")
    @TableField("status")
    private String status;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    @TableField("sort_order")
    private Integer sortOrder;
}

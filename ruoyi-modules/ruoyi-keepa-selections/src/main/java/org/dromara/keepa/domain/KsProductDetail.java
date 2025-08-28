package org.dromara.keepa.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.domain.BaseEntity;
import org.dromara.common.excel.annotation.ExcelIgnoreUnannotated;
import org.dromara.common.excel.annotation.ExcelProperty;

import java.io.Serial;
import java.time.LocalDate;

/**
 * 商品详情对象 ks_product_detail
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ks_product_detail")
@ExcelIgnoreUnannotated
public class KsProductDetail extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * ASIN记录ID
     */
    @TableField("asin_id")
    private Long asinId;

    /**
     * ASIN
     */
    @ExcelProperty(value = "ASIN")
    @TableField("asin")
    private String asin;

    /**
     * 商品标题
     */
    @ExcelProperty(value = "商品标题")
    @TableField("title")
    private String title;

    /**
     * 品牌
     */
    @ExcelProperty(value = "品牌")
    @TableField("brand")
    private String brand;

    /**
     * 制造商
     */
    @ExcelProperty(value = "制造商")
    @TableField("manufacturer")
    private String manufacturer;

    /**
     * 型号
     */
    @ExcelProperty(value = "型号")
    @TableField("model")
    private String model;

    /**
     * 类目树(JSON)
     */
    @TableField("category_tree")
    private String categoryTree;

    /**
     * 主图链接
     */
    @ExcelProperty(value = "主图链接")
    @TableField("image_url")
    private String imageUrl;

    /**
     * 产品特性(JSON)
     */
    @TableField("features")
    private String features;

    /**
     * 商品描述
     */
    @ExcelProperty(value = "商品描述")
    @TableField("description")
    private String description;

    /**
     * 尺寸
     */
    @ExcelProperty(value = "尺寸")
    @TableField("dimensions")
    private String dimensions;

    /**
     * 重量
     */
    @ExcelProperty(value = "重量")
    @TableField("weight")
    private String weight;

    /**
     * 包装尺寸
     */
    @ExcelProperty(value = "包装尺寸")
    @TableField("package_dimensions")
    private String packageDimensions;

    /**
     * 包装重量
     */
    @ExcelProperty(value = "包装重量")
    @TableField("package_weight")
    private String packageWeight;

    /**
     * 颜色
     */
    @ExcelProperty(value = "颜色")
    @TableField("color")
    private String color;

    /**
     * 尺码
     */
    @ExcelProperty(value = "尺码")
    @TableField("size")
    private String size;

    /**
     * 库存状态
     */
    @ExcelProperty(value = "库存状态")
    @TableField("availability")
    private String availability;

    /**
     * 是否成人产品
     */
    @ExcelProperty(value = "是否成人产品")
    @TableField("is_adult")
    private Boolean isAdult;

    /**
     * 装订类型
     */
    @ExcelProperty(value = "装订类型")
    @TableField("binding")
    private String binding;

    /**
     * 版本
     */
    @ExcelProperty(value = "版本")
    @TableField("edition")
    private String edition;

    /**
     * 语言
     */
    @ExcelProperty(value = "语言")
    @TableField("language")
    private String language;

    /**
     * 发布日期
     */
    @ExcelProperty(value = "发布日期")
    @TableField("publication_date")
    private LocalDate publicationDate;

    /**
     * 发行日期
     */
    @ExcelProperty(value = "发行日期")
    @TableField("release_date")
    private LocalDate releaseDate;

    /**
     * 危险品标识
     */
    @ExcelProperty(value = "危险品标识")
    @TableField("hazmat")
    private String hazmat;
}

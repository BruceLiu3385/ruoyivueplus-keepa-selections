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
import java.math.BigDecimal;

/**
 * 选品分析结果对象 ks_selection_result
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ks_selection_result")
@ExcelIgnoreUnannotated
public class KsSelectionResult extends BaseEntity {

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
    @TableField("batch_id")
    private Long batchId;

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
     * 主图链接
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 当前价格
     */
    @ExcelProperty(value = "当前价格")
    @TableField("current_price")
    private BigDecimal currentPrice;

    /**
     * 30天最低价
     */
    @ExcelProperty(value = "30天最低价")
    @TableField("min_price_30d")
    private BigDecimal minPrice30d;

    /**
     * 30天最高价
     */
    @ExcelProperty(value = "30天最高价")
    @TableField("max_price_30d")
    private BigDecimal maxPrice30d;

    /**
     * 30天平均价
     */
    @ExcelProperty(value = "30天平均价")
    @TableField("avg_price_30d")
    private BigDecimal avgPrice30d;

    /**
     * 价格波动率(%)
     */
    @ExcelProperty(value = "价格波动率(%)")
    @TableField("price_volatility")
    private BigDecimal priceVolatility;

    /**
     * 价格趋势(上升/下降/稳定)
     */
    @ExcelProperty(value = "价格趋势")
    @TableField("price_trend")
    private String priceTrend;

    /**
     * 折扣率(%)
     */
    @ExcelProperty(value = "折扣率(%)")
    @TableField("discount_rate")
    private BigDecimal discountRate;

    /**
     * 当前销量排名
     */
    @ExcelProperty(value = "当前销量排名")
    @TableField("current_sales_rank")
    private Integer currentSalesRank;

    /**
     * 30天最佳排名
     */
    @ExcelProperty(value = "30天最佳排名")
    @TableField("best_sales_rank_30d")
    private Integer bestSalesRank30d;

    /**
     * 30天最差排名
     */
    @ExcelProperty(value = "30天最差排名")
    @TableField("worst_sales_rank_30d")
    private Integer worstSalesRank30d;

    /**
     * 30天平均排名
     */
    @ExcelProperty(value = "30天平均排名")
    @TableField("avg_sales_rank_30d")
    private Integer avgSalesRank30d;

    /**
     * 排名波动率(%)
     */
    @ExcelProperty(value = "排名波动率(%)")
    @TableField("rank_volatility")
    private BigDecimal rankVolatility;

    /**
     * 排名趋势(上升/下降/稳定)
     */
    @ExcelProperty(value = "排名趋势")
    @TableField("rank_trend")
    private String rankTrend;

    /**
     * 当前评分
     */
    @ExcelProperty(value = "当前评分")
    @TableField("current_rating")
    private BigDecimal currentRating;

    /**
     * 当前评论数
     */
    @ExcelProperty(value = "当前评论数")
    @TableField("current_review_count")
    private Integer currentReviewCount;

    /**
     * 30天评论增长数
     */
    @ExcelProperty(value = "30天评论增长数")
    @TableField("review_growth_30d")
    private Integer reviewGrowth30d;

    /**
     * 评论增长率(%)
     */
    @ExcelProperty(value = "评论增长率(%)")
    @TableField("review_growth_rate")
    private BigDecimal reviewGrowthRate;

    /**
     * 竞争对手数量
     */
    @ExcelProperty(value = "竞争对手数量")
    @TableField("competitor_count")
    private Integer competitorCount;

    /**
     * 新品offer数量
     */
    @ExcelProperty(value = "新品offer数量")
    @TableField("new_offer_count")
    private Integer newOfferCount;

    /**
     * 二手offer数量
     */
    @ExcelProperty(value = "二手offer数量")
    @TableField("used_offer_count")
    private Integer usedOfferCount;

    /**
     * FBA offer数量
     */
    @ExcelProperty(value = "FBA offer数量")
    @TableField("fba_offer_count")
    private Integer fbaOfferCount;

    /**
     * 库存状态
     */
    @ExcelProperty(value = "库存状态")
    @TableField("availability_status")
    private String availabilityStatus;

    /**
     * 缺货频次
     */
    @ExcelProperty(value = "缺货频次")
    @TableField("stock_out_frequency")
    private Integer stockOutFrequency;

    /**
     * 选品评分(0-100)
     */
    @ExcelProperty(value = "选品评分")
    @TableField("selection_score")
    private BigDecimal selectionScore;

    /**
     * 盈利潜力(高/中/低)
     */
    @ExcelProperty(value = "盈利潜力")
    @TableField("profit_potential")
    private String profitPotential;

    /**
     * 市场饱和度(高/中/低)
     */
    @ExcelProperty(value = "市场饱和度")
    @TableField("market_saturation")
    private String marketSaturation;

    /**
     * 进入难度(高/中/低)
     */
    @ExcelProperty(value = "进入难度")
    @TableField("entry_difficulty")
    private String entryDifficulty;

    /**
     * 推荐等级(强烈推荐/推荐/一般/不推荐)
     */
    @ExcelProperty(value = "推荐等级")
    @TableField("recommendation_level")
    private String recommendationLevel;

    /**
     * 分析摘要
     */
    @TableField("analysis_summary")
    private String analysisSummary;

    /**
     * 优势分析
     */
    @TableField("advantages")
    private String advantages;

    /**
     * 风险分析
     */
    @TableField("risks")
    private String risks;

    /**
     * 建议
     */
    @TableField("suggestions")
    private String suggestions;

    // 推荐等级常量
    public static class RecommendationLevel {
        public static final String HIGHLY_RECOMMENDED = "强烈推荐";
        public static final String RECOMMENDED = "推荐";
        public static final String GENERAL = "一般";
        public static final String NOT_RECOMMENDED = "不推荐";
    }

    // 潜力等级常量
    public static class PotentialLevel {
        public static final String HIGH = "高";
        public static final String MEDIUM = "中";
        public static final String LOW = "低";
    }

    // 趋势常量
    public static class Trend {
        public static final String RISING = "上升";
        public static final String FALLING = "下降";
        public static final String STABLE = "稳定";
    }
}

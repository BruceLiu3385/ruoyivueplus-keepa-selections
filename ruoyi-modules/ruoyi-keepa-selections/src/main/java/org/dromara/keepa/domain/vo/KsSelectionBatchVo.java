package org.dromara.keepa.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.dromara.keepa.domain.KsSelectionBatch;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 选品批次视图对象 ks_selection_batch
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Data
public class KsSelectionBatchVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 批次ID
     */
    private Long id;

    /**
     * 批次名称
     */
    private String batchName;

    /**
     * 批次编码
     */
    private String batchCode;

    /**
     * 批次描述
     */
    private String description;

    /**
     * 状态(0初始,1配置中,2抓取ASIN中,3抓取详情中,4分析中,5完成,9失败)
     */
    private String status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 亚马逊站点域名
     */
    private Integer amazonDomain;

    /**
     * 亚马逊站点名称
     */
    private String amazonDomainName;

    /**
     * 选择的类目ID列表(JSON格式)
     */
    private String categoryIds;

    /**
     * 最大ASIN数量
     */
    private Integer maxAsinCount;

    /**
     * 当前ASIN数量
     */
    private Integer currentAsinCount;

    /**
     * 当前详情数量
     */
    private Integer currentDetailCount;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 进度百分比
     */
    private Integer progressPercentage;

    /**
     * 耗时（秒）
     */
    private Long durationSeconds;

    /**
     * 是否可以开始抓取ASIN
     */
    private Boolean canStartFetchAsin;

    /**
     * 是否可以开始抓取详情
     */
    private Boolean canStartFetchDetail;

    /**
     * 是否可以开始分析
     */
    private Boolean canStartAnalyze;

    /**
     * 获取状态名称
     */
    public String getStatusName() {
        if (status == null) {
            return "";
        }
        switch (status) {
            case KsSelectionBatch.BatchStatus.INITIAL:
                return "初始";
            case KsSelectionBatch.BatchStatus.CONFIGURING:
                return "配置中";
            case KsSelectionBatch.BatchStatus.FETCHING_ASIN:
                return "抓取ASIN中";
            case KsSelectionBatch.BatchStatus.FETCHING_DETAIL:
                return "抓取详情中";
            case KsSelectionBatch.BatchStatus.ANALYZING:
                return "分析中";
            case KsSelectionBatch.BatchStatus.COMPLETED:
                return "完成";
            case KsSelectionBatch.BatchStatus.FAILED:
                return "失败";
            default:
                return status;
        }
    }

    /**
     * 计算进度百分比
     */
    public Integer getProgressPercentage() {
        if (maxAsinCount == null || maxAsinCount <= 0) {
            return 0;
        }
        
        switch (status) {
            case KsSelectionBatch.BatchStatus.INITIAL:
            case KsSelectionBatch.BatchStatus.CONFIGURING:
                return 0;
            case KsSelectionBatch.BatchStatus.FETCHING_ASIN:
                return Math.min(20, currentAsinCount * 20 / maxAsinCount);
            case KsSelectionBatch.BatchStatus.FETCHING_DETAIL:
                return 20 + Math.min(60, currentDetailCount * 60 / maxAsinCount);
            case KsSelectionBatch.BatchStatus.ANALYZING:
                return 80 + Math.min(15, currentDetailCount * 15 / maxAsinCount);
            case KsSelectionBatch.BatchStatus.COMPLETED:
                return 100;
            case KsSelectionBatch.BatchStatus.FAILED:
                return progressPercentage != null ? progressPercentage : 0;
            default:
                return 0;
        }
    }
}

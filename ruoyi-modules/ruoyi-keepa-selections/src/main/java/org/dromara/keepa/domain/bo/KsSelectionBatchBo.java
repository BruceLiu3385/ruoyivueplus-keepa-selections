package org.dromara.keepa.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 选品批次业务对象 ks_selection_batch
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KsSelectionBatchBo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 批次ID
     */
    @NotNull(message = "批次ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 批次名称
     */
    @NotBlank(message = "批次名称不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 亚马逊站点域名
     */
    @NotNull(message = "亚马逊站点不能为空", groups = { AddGroup.class })
    private Integer amazonDomain;

    /**
     * 选择的类目ID列表(JSON格式)
     */
    @NotBlank(message = "类目不能为空", groups = { AddGroup.class })
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
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 错误信息
     */
    private String errorMessage;
}

package org.dromara.keepa.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 批次状态枚举
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Getter
@AllArgsConstructor
public enum BatchStatusEnum {

    INITIAL("0", "初始"),
    CONFIGURING("1", "配置中"),
    FETCHING_ASIN("2", "抓取ASIN中"),
    FETCHING_DETAIL("3", "抓取详情中"),
    ANALYZING("4", "分析中"),
    COMPLETED("5", "完成"),
    FAILED("9", "失败");

    private final String code;
    private final String info;
}

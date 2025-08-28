package org.dromara.keepa.domain;

import org.dromara.common.excel.convert.ExcelEnumConverter;

/**
 * 批次状态转换器
 *
 * @author ruoyi
 * @date 2025-01-02
 */
public class BatchStatusConverter implements ExcelEnumConverter<String> {

    @Override
    public String convertToJavaData(String cellValue) {
        switch (cellValue) {
            case "初始":
                return KsSelectionBatch.BatchStatus.INITIAL;
            case "配置中":
                return KsSelectionBatch.BatchStatus.CONFIGURING;
            case "抓取ASIN中":
                return KsSelectionBatch.BatchStatus.FETCHING_ASIN;
            case "抓取详情中":
                return KsSelectionBatch.BatchStatus.FETCHING_DETAIL;
            case "分析中":
                return KsSelectionBatch.BatchStatus.ANALYZING;
            case "完成":
                return KsSelectionBatch.BatchStatus.COMPLETED;
            case "失败":
                return KsSelectionBatch.BatchStatus.FAILED;
            default:
                return cellValue;
        }
    }

    @Override
    public String convertToExcelData(String value) {
        switch (value) {
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
                return value;
        }
    }
}

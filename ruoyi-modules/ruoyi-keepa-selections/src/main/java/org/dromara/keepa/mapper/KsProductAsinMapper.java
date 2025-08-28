package org.dromara.keepa.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.keepa.domain.KsProductAsin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品ASINMapper接口
 *
 * @author ruoyi
 * @date 2025-01-02
 */
public interface KsProductAsinMapper extends BaseMapperPlus<KsProductAsin, KsProductAsin> {

    /**
     * 根据批次ID获取待抓取详情的ASIN列表
     */
    List<KsProductAsin> selectPendingDetailAsinsByBatchId(@Param("batchId") Long batchId, @Param("limit") Integer limit);

    /**
     * 根据批次ID获取待分析的ASIN列表
     */
    List<KsProductAsin> selectPendingAnalysisAsinsByBatchId(@Param("batchId") Long batchId, @Param("limit") Integer limit);

    /**
     * 更新详情抓取状态
     */
    int updateDetailStatus(@Param("asinId") Long asinId, @Param("status") String status, @Param("errorMessage") String errorMessage);

    /**
     * 更新分析状态
     */
    int updateAnalysisStatus(@Param("asinId") Long asinId, @Param("status") String status);

    /**
     * 统计批次ASIN数量
     */
    int countByBatchId(@Param("batchId") Long batchId);

    /**
     * 统计已完成详情抓取的ASIN数量
     */
    int countCompletedDetailByBatchId(@Param("batchId") Long batchId);

    /**
     * 统计已分析的ASIN数量
     */
    int countAnalyzedByBatchId(@Param("batchId") Long batchId);
}

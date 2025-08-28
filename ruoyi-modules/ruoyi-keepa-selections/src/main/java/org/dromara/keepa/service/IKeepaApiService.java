package org.dromara.keepa.service;

import com.keepa.api.backend.structs.Category;
import com.keepa.api.backend.structs.Product;
import org.dromara.keepa.domain.KsAmazonDomain;

import java.util.List;
import java.util.Map;

/**
 * Keepa API服务接口
 *
 * @author ruoyi
 * @date 2025-01-02
 */
public interface IKeepaApiService {

    /**
     * 获取类目信息
     *
     * @param domainCode 域名编码
     * @param categoryIds 类目ID列表
     * @return 类目信息Map
     */
    Map<String, Category> getCategories(Integer domainCode, List<String> categoryIds);

    /**
     * 搜索类目
     *
     * @param domainCode 域名编码
     * @param keyword 关键词
     * @return 类目信息Map
     */
    Map<String, Category> searchCategories(Integer domainCode, String keyword);

    /**
     * 获取类目最佳销售商品ASIN列表
     *
     * @param domainCode 域名编码
     * @param categoryId 类目ID
     * @param maxCount 最大数量
     * @return ASIN列表
     */
    List<String> getCategoryBestSellers(Integer domainCode, String categoryId, Integer maxCount);

    /**
     * 批量查询商品信息
     *
     * @param domainCode 域名编码
     * @param asinList ASIN列表
     * @return 商品信息列表
     */
    List<Product> queryProducts(Integer domainCode, List<String> asinList);

    /**
     * 查询单个商品信息（包含历史数据）
     *
     * @param domainCode 域名编码
     * @param asin ASIN
     * @param includeHistory 是否包含历史数据
     * @param includeOffers 是否包含offer信息
     * @return 商品信息
     */
    Product queryProductDetail(Integer domainCode, String asin, boolean includeHistory, boolean includeOffers);

    /**
     * 检查API Token余量
     *
     * @return 剩余Token数量，-1表示无法获取
     */
    Integer checkTokensRemaining();

    /**
     * 等待Token恢复
     *
     * @param requiredTokens 需要的Token数量
     * @return 是否成功等待
     */
    boolean waitForTokens(Integer requiredTokens);

    /**
     * 获取域名信息
     *
     * @param domainCode 域名编码
     * @return 域名信息
     */
    KsAmazonDomain getDomainInfo(Integer domainCode);

    /**
     * 验证API密钥是否有效
     *
     * @return 是否有效
     */
    boolean validateApiKey();

    /**
     * 获取API使用统计
     *
     * @return 使用统计信息
     */
    Map<String, Object> getApiUsageStats();
}

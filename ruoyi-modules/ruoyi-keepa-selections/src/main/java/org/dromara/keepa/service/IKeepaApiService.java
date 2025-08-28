package org.dromara.keepa.service;

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
     * 获取指定域名的类目信息
     *
     * @param domainCode 域名编码
     * @return 类目ID到名称的映射
     */
    Map<Long, String> getCategories(Integer domainCode);

    /**
     * 搜索指定类目的商品
     *
     * @param domainCode 域名编码
     * @param categoryId 类目ID
     * @return ASIN列表
     */
    List<String> searchProductsByCategory(Integer domainCode, String categoryId);

    /**
     * 获取指定类目的畅销商品
     *
     * @param domainCode 域名编码
     * @param categoryId 类目ID
     * @param limit      返回数量限制
     * @return ASIN列表
     */
    List<String> getBestSellers(Integer domainCode, String categoryId, Integer limit);

    /**
     * 获取商品详情信息
     *
     * @param asins ASIN列表
     * @return 商品详情数据
     */
    Map<String, Object> getProductDetails(List<String> asins);

    /**
     * 检查剩余Token数量
     *
     * @return 剩余Token数量
     */
    Integer checkTokensRemaining();

    /**
     * 等待足够的Token
     *
     * @param requiredTokens 需要的Token数量
     * @return 是否成功获取到足够的Token
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
}
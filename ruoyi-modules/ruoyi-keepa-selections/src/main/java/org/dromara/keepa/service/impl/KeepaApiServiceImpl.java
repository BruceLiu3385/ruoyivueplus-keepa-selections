package org.dromara.keepa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.keepa.api.backend.KeepaAPI;
import com.keepa.api.backend.helper.KeepaTime;
import com.keepa.api.backend.structs.AmazonLocale;
import com.keepa.api.backend.structs.Category;
import com.keepa.api.backend.structs.Product;
import com.keepa.api.backend.structs.Request;
import com.keepa.api.backend.structs.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.keepa.config.KeepaApiConfig;
import org.dromara.keepa.domain.KsAmazonDomain;
import org.dromara.keepa.mapper.KsAmazonDomainMapper;
import org.dromara.keepa.service.IKeepaApiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Keepa API服务实现
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KeepaApiServiceImpl implements IKeepaApiService {

    private final KeepaAPI keepaAPI;
    private final KeepaApiConfig keepaApiConfig;
    private final KsAmazonDomainMapper amazonDomainMapper;

    /**
     * 域名编码到AmazonLocale的映射
     */
    private static final Map<Integer, AmazonLocale> DOMAIN_LOCALE_MAP = new HashMap<>();

    static {
        DOMAIN_LOCALE_MAP.put(1, AmazonLocale.US);
        DOMAIN_LOCALE_MAP.put(2, AmazonLocale.DE);
        DOMAIN_LOCALE_MAP.put(3, AmazonLocale.UK);
        DOMAIN_LOCALE_MAP.put(4, AmazonLocale.FR);
        DOMAIN_LOCALE_MAP.put(5, AmazonLocale.JP);
        DOMAIN_LOCALE_MAP.put(6, AmazonLocale.CA);
        DOMAIN_LOCALE_MAP.put(7, AmazonLocale.IT);
        DOMAIN_LOCALE_MAP.put(8, AmazonLocale.ES);
        DOMAIN_LOCALE_MAP.put(9, AmazonLocale.IN);
        DOMAIN_LOCALE_MAP.put(11, AmazonLocale.MX);
        DOMAIN_LOCALE_MAP.put(12, AmazonLocale.AU);
        DOMAIN_LOCALE_MAP.put(13, AmazonLocale.BR);
        DOMAIN_LOCALE_MAP.put(14, AmazonLocale.TR);
    }

    @Override
    public Map<String, Category> getCategories(Integer domainCode, List<String> categoryIds) {
        Map<String, Category> result = new HashMap<>();
        
        try {
            AmazonLocale locale = getAmazonLocale(domainCode);
            if (locale == null) {
                log.error("不支持的域名编码: {}", domainCode);
                return result;
            }

            // 等待Token
            if (!waitForTokens(1)) {
                log.error("等待Token超时");
                return result;
            }

            Request request = Request.getCategoryRequest(locale, categoryIds.toArray(new String[0]));
            Response response = keepaAPI.sendRequestSync(request);

            if (response != null && response.categories != null) {
                for (Category category : response.categories) {
                    result.put(String.valueOf(category.catId), category);
                }
            }

            log.info("获取类目信息完成，域名: {}, 类目数量: {}", domainCode, result.size());

        } catch (Exception e) {
            log.error("获取类目信息失败，域名: {}, 错误: {}", domainCode, e.getMessage(), e);
        }

        return result;
    }

    @Override
    public Map<String, Category> searchCategories(Integer domainCode, String keyword) {
        Map<String, Category> result = new HashMap<>();

        try {
            AmazonLocale locale = getAmazonLocale(domainCode);
            if (locale == null) {
                log.error("不支持的域名编码: {}", domainCode);
                return result;
            }

            // 等待Token
            if (!waitForTokens(1)) {
                log.error("等待Token超时");
                return result;
            }

            Request request = Request.getCategorySearchRequest(locale, keyword);
            Response response = keepaAPI.sendRequestSync(request);

            if (response != null && response.categoryLookup != null) {
                for (Map.Entry<Integer, String> entry : response.categoryLookup.entrySet()) {
                    Category category = new Category();
                    category.catId = entry.getKey();
                    category.name = entry.getValue();
                    result.put(String.valueOf(entry.getKey()), category);
                }
            }

            log.info("搜索类目完成，域名: {}, 关键词: {}, 类目数量: {}", domainCode, keyword, result.size());

        } catch (Exception e) {
            log.error("搜索类目失败，域名: {}, 关键词: {}, 错误: {}", domainCode, keyword, e.getMessage(), e);
        }

        return result;
    }

    @Override
    public List<String> getCategoryBestSellers(Integer domainCode, String categoryId, Integer maxCount) {
        List<String> result = new ArrayList<>();

        try {
            AmazonLocale locale = getAmazonLocale(domainCode);
            if (locale == null) {
                log.error("不支持的域名编码: {}", domainCode);
                return result;
            }

            // 等待Token
            if (!waitForTokens(1)) {
                log.error("等待Token超时");
                return result;
            }

            // 限制最大数量
            int actualMaxCount = Math.min(maxCount, 5000);
            
            Request request = Request.getBestSellersRequest(locale, categoryId, actualMaxCount);
            Response response = keepaAPI.sendRequestSync(request);

            if (response != null && response.bestSellersList != null && response.bestSellersList.length > 0) {
                String[] asinArray = response.bestSellersList[0].asinList;
                if (asinArray != null) {
                    for (int i = 0; i < Math.min(asinArray.length, actualMaxCount); i++) {
                        if (StrUtil.isNotBlank(asinArray[i])) {
                            result.add(asinArray[i]);
                        }
                    }
                }
            }

            log.info("获取类目最佳销售商品完成，域名: {}, 类目: {}, ASIN数量: {}", domainCode, categoryId, result.size());

        } catch (Exception e) {
            log.error("获取类目最佳销售商品失败，域名: {}, 类目: {}, 错误: {}", domainCode, categoryId, e.getMessage(), e);
        }

        return result;
    }

    @Override
    public List<Product> queryProducts(Integer domainCode, List<String> asinList) {
        List<Product> result = new ArrayList<>();

        if (CollUtil.isEmpty(asinList)) {
            return result;
        }

        try {
            AmazonLocale locale = getAmazonLocale(domainCode);
            if (locale == null) {
                log.error("不支持的域名编码: {}", domainCode);
                return result;
            }

            // 等待Token
            if (!waitForTokens(asinList.size())) {
                log.error("等待Token超时");
                return result;
            }

            String[] asinArray = asinList.toArray(new String[0]);
            Request request = Request.getProductRequest(locale, 0, null, asinArray);
            Response response = keepaAPI.sendRequestSync(request);

            if (response != null && response.products != null) {
                for (Product product : response.products) {
                    if (product != null) {
                        result.add(product);
                    }
                }
            }

            log.info("批量查询商品信息完成，域名: {}, 请求ASIN数量: {}, 返回商品数量: {}", 
                    domainCode, asinList.size(), result.size());

        } catch (Exception e) {
            log.error("批量查询商品信息失败，域名: {}, ASIN数量: {}, 错误: {}", 
                    domainCode, asinList.size(), e.getMessage(), e);
        }

        return result;
    }

    @Override
    public Product queryProductDetail(Integer domainCode, String asin, boolean includeHistory, boolean includeOffers) {
        try {
            AmazonLocale locale = getAmazonLocale(domainCode);
            if (locale == null) {
                log.error("不支持的域名编码: {}", domainCode);
                return null;
            }

            // 等待Token
            if (!waitForTokens(1)) {
                log.error("等待Token超时");
                return null;
            }

            int historyDays = includeHistory ? 365 : 0;
            int offers = includeOffers ? 20 : 0;

            Request request = Request.getProductRequest(locale, historyDays, null, new String[]{asin});
            request.offers = offers;

            Response response = keepaAPI.sendRequestSync(request);

            if (response != null && response.products != null && response.products.length > 0) {
                Product product = response.products[0];
                log.info("查询商品详情完成，域名: {}, ASIN: {}, 标题: {}", 
                        domainCode, asin, product.title);
                return product;
            }

            log.warn("商品详情查询无结果，域名: {}, ASIN: {}", domainCode, asin);

        } catch (Exception e) {
            log.error("查询商品详情失败，域名: {}, ASIN: {}, 错误: {}", domainCode, asin, e.getMessage(), e);
        }

        return null;
    }

    @Override
    public Integer checkTokensRemaining() {
        try {
            Request request = Request.getTokenStatusRequest();
            Response response = keepaAPI.sendRequestSync(request);
            
            if (response != null && response.tokensLeft != null) {
                return response.tokensLeft;
            }
        } catch (Exception e) {
            log.error("检查Token余量失败: {}", e.getMessage());
        }
        return -1;
    }

    @Override
    public boolean waitForTokens(Integer requiredTokens) {
        if (!keepaApiConfig.getEnableRateLimit()) {
            return true;
        }

        try {
            Integer tokensLeft = checkTokensRemaining();
            if (tokensLeft == null || tokensLeft < 0) {
                // 无法获取Token信息，使用默认延迟
                Thread.sleep(keepaApiConfig.getRequestDelayMs());
                return true;
            }

            if (tokensLeft >= requiredTokens) {
                return true;
            }

            // 计算需要等待的时间
            int tokensNeeded = requiredTokens - tokensLeft;
            long waitTimeMs = (long) Math.ceil((double) tokensNeeded * 60000 / keepaApiConfig.getTokensPerMinute());
            
            // 添加随机延迟避免并发冲突
            waitTimeMs += ThreadLocalRandom.current().nextLong(1000, 5000);

            log.info("Token不足，需要等待 {} 毫秒，当前Token: {}, 需要Token: {}", 
                    waitTimeMs, tokensLeft, requiredTokens);

            Thread.sleep(Math.min(waitTimeMs, 300000)); // 最多等待5分钟
            return true;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("等待Token时被中断: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("等待Token失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public KsAmazonDomain getDomainInfo(Integer domainCode) {
        return amazonDomainMapper.selectOne("domain_code", domainCode);
    }

    @Override
    public boolean validateApiKey() {
        try {
            Integer tokensLeft = checkTokensRemaining();
            return tokensLeft != null && tokensLeft >= 0;
        } catch (Exception e) {
            log.error("验证API密钥失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Map<String, Object> getApiUsageStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            Integer tokensLeft = checkTokensRemaining();
            stats.put("tokensLeft", tokensLeft);
            stats.put("tokensPerMinute", keepaApiConfig.getTokensPerMinute());
            stats.put("maxBatchSize", keepaApiConfig.getMaxBatchSize());
            stats.put("requestDelayMs", keepaApiConfig.getRequestDelayMs());
            stats.put("checkTime", KeepaTime.nowMinutes());
        } catch (Exception e) {
            log.error("获取API使用统计失败: {}", e.getMessage());
        }
        return stats;
    }

    /**
     * 获取AmazonLocale
     */
    private AmazonLocale getAmazonLocale(Integer domainCode) {
        return DOMAIN_LOCALE_MAP.get(domainCode);
    }
}

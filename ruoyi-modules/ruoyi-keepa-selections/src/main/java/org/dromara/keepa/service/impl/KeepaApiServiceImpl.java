package org.dromara.keepa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    private final KeepaApiConfig keepaApiConfig;
    private final KsAmazonDomainMapper amazonDomainMapper;

    @Override
    public Map<Long, String> getCategories(Integer domainCode) {
        log.info("获取域名 {} 的类目", domainCode);
        // TODO: 实现Keepa API调用获取类目
        Map<Long, String> categories = new HashMap<>();
        categories.put(1L, "Electronics");
        categories.put(2L, "Books");
        categories.put(3L, "Clothing & Accessories");
        return categories;
    }

    @Override
    public List<String> searchProductsByCategory(Integer domainCode, String categoryId) {
        log.info("搜索域名 {} 类目 {} 的商品", domainCode, categoryId);
        // TODO: 实现Keepa API调用搜索商品
        List<String> asins = new ArrayList<>();
        // 模拟返回一些ASIN
        for (int i = 1; i <= 10; i++) {
            asins.add("B00TEST" + String.format("%03d", i));
        }
        return asins;
    }

    @Override
    public List<String> getBestSellers(Integer domainCode, String categoryId, Integer limit) {
        log.info("获取域名 {} 类目 {} 的畅销商品，限制 {} 个", domainCode, categoryId, limit);
        // TODO: 实现Keepa API调用获取畅销商品
        List<String> asins = new ArrayList<>();
        int count = limit != null ? Math.min(limit, 100) : 100;
        for (int i = 1; i <= count; i++) {
            asins.add("B00BEST" + String.format("%03d", i));
        }
        return asins;
    }

    @Override
    public Map<String, Object> getProductDetails(List<String> asins) {
        log.info("获取 {} 个商品的详情", asins.size());
        // TODO: 实现Keepa API调用获取商品详情
        Map<String, Object> productDetails = new HashMap<>();
        for (String asin : asins) {
            Map<String, Object> detail = new HashMap<>();
            detail.put("asin", asin);
            detail.put("title", "Test Product " + asin);
            detail.put("price", 29.99);
            detail.put("salesRank", 1000);
            productDetails.put(asin, detail);
        }
        return productDetails;
    }

    @Override
    public Integer checkTokensRemaining() {
        log.info("检查剩余Token数量");
        // TODO: 实现Keepa API调用检查Token
        return 100; // 模拟返回100个Token
    }

    @Override
    public boolean waitForTokens(Integer requiredTokens) {
        log.info("等待 {} 个Token", requiredTokens);
        try {
            Integer currentTokens = checkTokensRemaining();
            if (currentTokens >= requiredTokens) {
                return true;
            }
            
            // 计算需要等待的时间
            int tokensNeeded = requiredTokens - currentTokens;
            long waitTimeMs = (long) (tokensNeeded * 60.0 / keepaApiConfig.getTokensPerMinute() * 1000);
            
            log.info("当前Token: {}, 需要: {}, 等待时间: {}ms", currentTokens, requiredTokens, waitTimeMs);
            Thread.sleep(waitTimeMs);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("等待Token失败: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("等待Token失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public KsAmazonDomain getDomainInfo(Integer domainCode) {
        LambdaQueryWrapper<KsAmazonDomain> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KsAmazonDomain::getDomainCode, domainCode);
        return amazonDomainMapper.selectOne(wrapper);
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
}
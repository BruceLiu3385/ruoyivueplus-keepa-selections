package org.dromara.keepa.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.keepa.service.IKeepaApiService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Keepa API接口管理
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Tag(name = "Keepa API接口", description = "Keepa API的调用和测试接口")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/keepa/api")
public class KeepaApiController extends BaseController {

    private final IKeepaApiService keepaApiService;

    /**
     * 获取指定域名的类目信息
     */
    @Operation(summary = "获取类目信息", description = "获取指定亚马逊域名的商品类目信息")
    @SaCheckPermission("keepa:api:category")
    @GetMapping("/categories/{domainCode}")
    public R<Map<Long, String>> getCategories(
            @Parameter(description = "域名编码", required = true, example = "1")
            @PathVariable @NotNull Integer domainCode) {
        try {
            Map<Long, String> categories = keepaApiService.getCategories(domainCode);
            return R.ok(categories);
        } catch (Exception e) {
            log.error("获取类目信息失败: {}", e.getMessage());
            return R.fail("获取类目信息失败: " + e.getMessage());
        }
    }

    /**
     * 搜索指定类目的商品
     */
    @Operation(summary = "搜索商品ASIN", description = "根据域名和类目搜索商品ASIN列表")
    @SaCheckPermission("keepa:api:search")
    @GetMapping("/products/search")
    public R<List<String>> searchProducts(
            @Parameter(description = "域名编码", required = true, example = "1")
            @RequestParam @NotNull Integer domainCode,
            @Parameter(description = "类目ID", required = true, example = "541966")
            @RequestParam @NotBlank String categoryId) {
        try {
            List<String> asins = keepaApiService.searchProductsByCategory(domainCode, categoryId);
            return R.ok(asins);
        } catch (Exception e) {
            log.error("搜索商品失败: {}", e.getMessage());
            return R.fail("搜索商品失败: " + e.getMessage());
        }
    }

    /**
     * 获取畅销商品列表
     */
    @Operation(summary = "获取畅销商品", description = "获取指定类目的畅销商品ASIN列表")
    @SaCheckPermission("keepa:api:bestsellers")
    @GetMapping("/products/bestsellers")
    public R<List<String>> getBestSellers(
            @Parameter(description = "域名编码", required = true, example = "1")
            @RequestParam @NotNull Integer domainCode,
            @Parameter(description = "类目ID", required = true, example = "541966")
            @RequestParam @NotBlank String categoryId,
            @Parameter(description = "返回数量限制", example = "100")
            @RequestParam(defaultValue = "100") Integer limit) {
        try {
            List<String> asins = keepaApiService.getBestSellers(domainCode, categoryId, limit);
            return R.ok(asins);
        } catch (Exception e) {
            log.error("获取畅销商品失败: {}", e.getMessage());
            return R.fail("获取畅销商品失败: " + e.getMessage());
        }
    }

    /**
     * 获取商品详情信息
     */
    @Operation(summary = "获取商品详情", description = "批量获取商品详情信息")
    @SaCheckPermission("keepa:api:details")
    @PostMapping("/products/details")
    public R<Map<String, Object>> getProductDetails(
            @Parameter(description = "ASIN列表", required = true)
            @RequestBody List<String> asins) {
        try {
            if (asins == null || asins.isEmpty()) {
                return R.fail("ASIN列表不能为空");
            }
            if (asins.size() > 100) {
                return R.fail("单次查询ASIN数量不能超过100个");
            }
            Map<String, Object> details = keepaApiService.getProductDetails(asins);
            return R.ok(details);
        } catch (Exception e) {
            log.error("获取商品详情失败: {}", e.getMessage());
            return R.fail("获取商品详情失败: " + e.getMessage());
        }
    }

    /**
     * 检查剩余Token数量
     */
    @Operation(summary = "检查Token状态", description = "检查Keepa API剩余Token数量")
    @SaCheckPermission("keepa:api:token")
    @GetMapping("/token/status")
    public R<Integer> checkTokenStatus() {
        try {
            Integer tokensLeft = keepaApiService.checkTokensRemaining();
            return R.ok(tokensLeft);
        } catch (Exception e) {
            log.error("检查Token状态失败: {}", e.getMessage());
            return R.fail("检查Token状态失败: " + e.getMessage());
        }
    }

    /**
     * 验证API密钥
     */
    @Operation(summary = "验证API密钥", description = "验证当前配置的Keepa API密钥是否有效")
    @SaCheckPermission("keepa:api:validate")
    @GetMapping("/validate")
    public R<Boolean> validateApiKey() {
        try {
            boolean isValid = keepaApiService.validateApiKey();
            if (isValid) {
                return R.ok(true);
            } else {
                return R.fail("API密钥验证失败");
            }
        } catch (Exception e) {
            log.error("验证API密钥失败: {}", e.getMessage());
            return R.fail("验证API密钥失败: " + e.getMessage());
        }
    }

    /**
     * 获取域名信息
     */
    @Operation(summary = "获取域名信息", description = "根据域名编码获取域名详细信息")
    @SaCheckPermission("keepa:api:domain")
    @GetMapping("/domain/{domainCode}")
    public R<Object> getDomainInfo(
            @Parameter(description = "域名编码", required = true, example = "1")
            @PathVariable @NotNull Integer domainCode) {
        try {
            var domain = keepaApiService.getDomainInfo(domainCode);
            if (domain != null) {
                return R.ok(domain);
            } else {
                return R.fail("未找到指定的域名信息");
            }
        } catch (Exception e) {
            log.error("获取域名信息失败: {}", e.getMessage());
            return R.fail("获取域名信息失败: " + e.getMessage());
        }
    }
}

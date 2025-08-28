package org.dromara.keepa.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.keepa.domain.KsAmazonDomain;
import org.dromara.keepa.mapper.KsAmazonDomainMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 亚马逊域名站点管理
 *
 * @author ruoyi
 * @date 2025-01-02
 */
@Tag(name = "亚马逊域名管理", description = "亚马逊站点域名的查询和管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/keepa/domain")
public class KsAmazonDomainController extends BaseController {

    private final KsAmazonDomainMapper amazonDomainMapper;

    /**
     * 获取所有可用的亚马逊站点
     */
    @Operation(summary = "获取可用站点列表", description = "获取所有状态为启用的亚马逊站点")
    @SaCheckPermission("keepa:domain:list")
    @GetMapping("/list")
    public R<List<KsAmazonDomain>> list() {
        LambdaQueryWrapper<KsAmazonDomain> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KsAmazonDomain::getStatus, "0")
               .orderByAsc(KsAmazonDomain::getSortOrder);
        List<KsAmazonDomain> list = amazonDomainMapper.selectList(wrapper);
        return R.ok(list);
    }

    /**
     * 获取所有亚马逊站点（包括停用的）
     */
    @Operation(summary = "获取所有站点列表", description = "获取所有亚马逊站点，包括启用和停用的")
    @SaCheckPermission("keepa:domain:all")
    @GetMapping("/all")
    public R<List<KsAmazonDomain>> all() {
        LambdaQueryWrapper<KsAmazonDomain> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(KsAmazonDomain::getSortOrder);
        List<KsAmazonDomain> list = amazonDomainMapper.selectList(wrapper);
        return R.ok(list);
    }
}

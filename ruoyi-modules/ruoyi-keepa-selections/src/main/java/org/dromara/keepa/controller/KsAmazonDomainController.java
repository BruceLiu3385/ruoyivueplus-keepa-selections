package org.dromara.keepa.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
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
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/keepa/domain")
public class KsAmazonDomainController extends BaseController {

    private final KsAmazonDomainMapper amazonDomainMapper;

    /**
     * 获取所有可用的亚马逊站点
     */
    @SaCheckPermission("keepa:domain:list")
    @GetMapping("/list")
    public R<List<KsAmazonDomain>> list() {
        List<KsAmazonDomain> list = amazonDomainMapper.selectList("status", "0", "sort_order");
        return R.ok(list);
    }

    /**
     * 获取所有亚马逊站点（包括停用的）
     */
    @SaCheckPermission("keepa:domain:all")
    @GetMapping("/all")
    public R<List<KsAmazonDomain>> all() {
        List<KsAmazonDomain> list = amazonDomainMapper.selectList(null, "sort_order");
        return R.ok(list);
    }
}

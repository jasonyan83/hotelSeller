package com.ceair.hotel.admin.controller;

import com.ceair.hotel.admin.service.CacheStrategyService;
import com.ceair.hotel.common.dto.R;
import com.ceair.hotel.common.entity.SupplierCacheStrategy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 缓存策略管理 Controller
 */
@Api(tags = "缓存策略管理")
@RestController
@RequestMapping("/api/v1/admin/suppliers")
@RequiredArgsConstructor
public class CacheStrategyController {

    private final CacheStrategyService cacheStrategyService;

    @ApiOperation("查询缓存策略")
    @GetMapping("/{supplierId}/cache-strategy")
    public R<SupplierCacheStrategy> getCacheStrategy(@PathVariable Long supplierId) {
        return R.ok(cacheStrategyService.getCacheStrategy(supplierId));
    }

    @ApiOperation("设置缓存策略")
    @PutMapping("/{supplierId}/cache-strategy")
    public R<Void> saveCacheStrategy(@PathVariable Long supplierId,
                                      @RequestBody SupplierCacheStrategy strategy) {
        cacheStrategyService.saveCacheStrategy(supplierId, strategy);
        return R.ok("设置成功", null);
    }
}

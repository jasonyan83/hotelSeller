package com.ceair.hotel.admin.controller;

import com.ceair.hotel.admin.service.PriceStrategyService;
import com.ceair.hotel.common.dto.R;
import com.ceair.hotel.common.entity.PriceStrategyGlobal;
import com.ceair.hotel.common.entity.PriceStrategySpecial;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 价格策略管理 Controller
 */
@Api(tags = "价格策略管理")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class PriceStrategyController {

    private final PriceStrategyService priceStrategyService;

    @ApiOperation("查询全局价格策略")
    @GetMapping("/suppliers/{supplierId}/global-strategy")
    public R<PriceStrategyGlobal> getGlobalStrategy(@PathVariable Long supplierId) {
        return R.ok(priceStrategyService.getGlobalStrategy(supplierId));
    }

    @ApiOperation("设置全局价格策略")
    @PutMapping("/suppliers/{supplierId}/global-strategy")
    public R<Void> saveGlobalStrategy(@PathVariable Long supplierId,
                                       @RequestBody PriceStrategyGlobal strategy) {
        priceStrategyService.saveGlobalStrategy(supplierId, strategy);
        return R.ok("设置成功", null);
    }

    @ApiOperation("查询特殊价格策略列表")
    @GetMapping("/suppliers/{supplierId}/price-strategies")
    public R<List<PriceStrategySpecial>> listSpecialStrategies(@PathVariable Long supplierId) {
        return R.ok(priceStrategyService.listSpecialStrategies(supplierId));
    }

    @ApiOperation("添加特殊价格策略")
    @PostMapping("/suppliers/{supplierId}/price-strategies")
    public R<Long> addSpecialStrategy(@PathVariable Long supplierId,
                                       @RequestBody PriceStrategySpecial strategy) {
        Long id = priceStrategyService.addSpecialStrategy(supplierId, strategy);
        return R.ok("添加成功", id);
    }

    @ApiOperation("编辑特殊价格策略")
    @PutMapping("/price-strategies/{strategyId}")
    public R<Void> updateSpecialStrategy(@PathVariable Long strategyId,
                                          @RequestBody PriceStrategySpecial strategy) {
        priceStrategyService.updateSpecialStrategy(strategyId, strategy);
        return R.ok("编辑成功", null);
    }

    @ApiOperation("删除特殊价格策略")
    @DeleteMapping("/price-strategies/{strategyId}")
    public R<Void> deleteSpecialStrategy(@PathVariable Long strategyId) {
        priceStrategyService.deleteSpecialStrategy(strategyId);
        return R.ok("删除成功", null);
    }
}

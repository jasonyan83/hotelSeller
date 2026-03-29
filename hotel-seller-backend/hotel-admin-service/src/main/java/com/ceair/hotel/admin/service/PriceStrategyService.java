package com.ceair.hotel.admin.service;

import com.ceair.hotel.common.entity.PriceStrategyGlobal;
import com.ceair.hotel.common.entity.PriceStrategySpecial;

import java.util.List;

/**
 * 价格策略管理服务接口
 */
public interface PriceStrategyService {

    /** 查询全局策略 */
    PriceStrategyGlobal getGlobalStrategy(Long supplierId);

    /** 设置全局策略(新增或更新) */
    void saveGlobalStrategy(Long supplierId, PriceStrategyGlobal strategy);

    /** 查询特殊价格策略列表 */
    List<PriceStrategySpecial> listSpecialStrategies(Long supplierId);

    /** 添加特殊价格策略 */
    Long addSpecialStrategy(Long supplierId, PriceStrategySpecial strategy);

    /** 编辑特殊价格策略 */
    void updateSpecialStrategy(Long strategyId, PriceStrategySpecial strategy);

    /** 删除特殊价格策略(逻辑删除) */
    void deleteSpecialStrategy(Long strategyId);
}

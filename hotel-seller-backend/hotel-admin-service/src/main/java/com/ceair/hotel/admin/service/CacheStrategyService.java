package com.ceair.hotel.admin.service;

import com.ceair.hotel.common.entity.SupplierCacheStrategy;

/**
 * 缓存策略管理服务接口
 */
public interface CacheStrategyService {

    /** 查询供应商缓存策略 */
    SupplierCacheStrategy getCacheStrategy(Long supplierId);

    /** 设置供应商缓存策略 */
    void saveCacheStrategy(Long supplierId, SupplierCacheStrategy strategy);
}

package com.ceair.hotel.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ceair.hotel.admin.mapper.SupplierCacheStrategyMapper;
import com.ceair.hotel.admin.mapper.SupplierOperationLogMapper;
import com.ceair.hotel.admin.service.CacheStrategyService;
import com.ceair.hotel.common.entity.SupplierCacheStrategy;
import com.ceair.hotel.common.entity.SupplierOperationLog;
import com.ceair.hotel.common.enums.OperateType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheStrategyServiceImpl implements CacheStrategyService {

    private final SupplierCacheStrategyMapper cacheStrategyMapper;
    private final SupplierOperationLogMapper logMapper;

    @Override
    public SupplierCacheStrategy getCacheStrategy(Long supplierId) {
        return cacheStrategyMapper.selectOne(
                new LambdaQueryWrapper<SupplierCacheStrategy>()
                        .eq(SupplierCacheStrategy::getSupplierId, supplierId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCacheStrategy(Long supplierId, SupplierCacheStrategy strategy) {
        SupplierCacheStrategy existing = getCacheStrategy(supplierId);
        if (existing != null) {
            strategy.setId(existing.getId());
            strategy.setSupplierId(supplierId);
            cacheStrategyMapper.updateById(strategy);
        } else {
            strategy.setSupplierId(supplierId);
            cacheStrategyMapper.insert(strategy);
        }

        SupplierOperationLog opLog = new SupplierOperationLog();
        opLog.setSupplierId(supplierId);
        opLog.setOperator("system");
        opLog.setOperateTime(LocalDateTime.now());
        opLog.setOperateType(OperateType.EDIT.name());
        opLog.setOperateTarget("缓存策略");
        opLog.setOperateContent("设置缓存策略: 参与列表页=" + strategy.getParticipateListPrice()
                + ", 详情页报价源=" + strategy.getDetailPriceSource());
        logMapper.insert(opLog);

        log.info("保存缓存策略, supplierId={}", supplierId);
    }
}

package com.ceair.hotel.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ceair.hotel.admin.mapper.PriceStrategyGlobalMapper;
import com.ceair.hotel.admin.mapper.PriceStrategySpecialMapper;
import com.ceair.hotel.admin.mapper.SupplierOperationLogMapper;
import com.ceair.hotel.admin.service.PriceStrategyService;
import com.ceair.hotel.common.entity.PriceStrategyGlobal;
import com.ceair.hotel.common.entity.PriceStrategySpecial;
import com.ceair.hotel.common.entity.SupplierOperationLog;
import com.ceair.hotel.common.enums.OperateType;
import com.ceair.hotel.common.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceStrategyServiceImpl implements PriceStrategyService {

    private final PriceStrategyGlobalMapper globalMapper;
    private final PriceStrategySpecialMapper specialMapper;
    private final SupplierOperationLogMapper logMapper;

    @Override
    public PriceStrategyGlobal getGlobalStrategy(Long supplierId) {
        return globalMapper.selectOne(
                new LambdaQueryWrapper<PriceStrategyGlobal>()
                        .eq(PriceStrategyGlobal::getSupplierId, supplierId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGlobalStrategy(Long supplierId, PriceStrategyGlobal strategy) {
        PriceStrategyGlobal existing = getGlobalStrategy(supplierId);
        if (existing != null) {
            strategy.setId(existing.getId());
            strategy.setSupplierId(supplierId);
            globalMapper.updateById(strategy);
        } else {
            strategy.setSupplierId(supplierId);
            globalMapper.insert(strategy);
        }
        saveLog(supplierId, OperateType.EDIT, "全局价格策略",
                "设置全局策略: 加价比例=" + strategy.getMarkupRate() + "%, 加价金额=" + strategy.getMarkupAmount());
        log.info("保存全局价格策略, supplierId={}", supplierId);
    }

    @Override
    public List<PriceStrategySpecial> listSpecialStrategies(Long supplierId) {
        return specialMapper.selectList(
                new LambdaQueryWrapper<PriceStrategySpecial>()
                        .eq(PriceStrategySpecial::getSupplierId, supplierId)
                        .orderByDesc(PriceStrategySpecial::getUpdatedTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addSpecialStrategy(Long supplierId, PriceStrategySpecial strategy) {
        strategy.setSupplierId(supplierId);
        specialMapper.insert(strategy);
        saveLog(supplierId, OperateType.CREATE, "特殊价格策略",
                "新增特殊策略: " + strategy.getStrategyNo());
        log.info("新增特殊价格策略, supplierId={}, strategyNo={}", supplierId, strategy.getStrategyNo());
        return strategy.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpecialStrategy(Long strategyId, PriceStrategySpecial strategy) {
        PriceStrategySpecial old = specialMapper.selectById(strategyId);
        if (old == null) {
            throw new BizException(404, "策略不存在");
        }
        strategy.setId(strategyId);
        specialMapper.updateById(strategy);
        saveLog(old.getSupplierId(), OperateType.EDIT, "特殊价格策略",
                "编辑特殊策略: " + old.getStrategyNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSpecialStrategy(Long strategyId) {
        PriceStrategySpecial old = specialMapper.selectById(strategyId);
        if (old == null) {
            throw new BizException(404, "策略不存在");
        }
        specialMapper.deleteById(strategyId); // 逻辑删除
        saveLog(old.getSupplierId(), OperateType.DELETE, "特殊价格策略",
                "删除特殊策略: " + old.getStrategyNo());
    }

    private void saveLog(Long supplierId, OperateType type, String target, String content) {
        SupplierOperationLog opLog = new SupplierOperationLog();
        opLog.setSupplierId(supplierId);
        opLog.setOperator("system");
        opLog.setOperateTime(LocalDateTime.now());
        opLog.setOperateType(type.name());
        opLog.setOperateTarget(target);
        opLog.setOperateContent(content);
        logMapper.insert(opLog);
    }
}

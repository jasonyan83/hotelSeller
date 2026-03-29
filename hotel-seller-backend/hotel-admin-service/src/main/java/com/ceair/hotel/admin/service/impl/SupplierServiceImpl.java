package com.ceair.hotel.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ceair.hotel.admin.mapper.*;
import com.ceair.hotel.admin.service.SupplierService;
import com.ceair.hotel.common.dto.PageResult;
import com.ceair.hotel.common.entity.*;
import com.ceair.hotel.common.enums.OperateType;
import com.ceair.hotel.common.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierMapper supplierMapper;
    private final SupplierWorkScheduleMapper scheduleMapper;
    private final SupplierContactMapper contactMapper;
    private final SupplierCacheStrategyMapper cacheStrategyMapper;
    private final SupplierOperationLogMapper logMapper;

    @Override
    public PageResult<Supplier> listSuppliers(String keyword, Integer status, int pageNo, int pageSize) {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                .like(Supplier::getSupplierName, keyword)
                .or().like(Supplier::getSupplierCode, keyword)
            );
        }
        if (status != null) {
            wrapper.eq(Supplier::getStatus, status);
        }
        wrapper.orderByDesc(Supplier::getUpdatedTime);

        Page<Supplier> page = supplierMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        return PageResult.of(page.getRecords(), page.getTotal(), pageNo, pageSize);
    }

    @Override
    public Supplier getById(Long id) {
        Supplier supplier = supplierMapper.selectById(id);
        if (supplier == null) {
            throw new BizException(404, "供应商不存在");
        }
        return supplier;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addSupplier(Supplier supplier, List<SupplierWorkSchedule> schedules, SupplierContact contact) {
        // 检查编号唯一性
        Long exists = supplierMapper.selectCount(
            new LambdaQueryWrapper<Supplier>().eq(Supplier::getSupplierCode, supplier.getSupplierCode()));
        if (exists > 0) {
            throw new BizException("供应商编号已存在: " + supplier.getSupplierCode());
        }

        supplierMapper.insert(supplier);
        Long supplierId = supplier.getId();

        // 保存工作时间
        if (schedules != null) {
            for (SupplierWorkSchedule s : schedules) {
                s.setSupplierId(supplierId);
                scheduleMapper.insert(s);
            }
        }

        // 保存联系人
        if (contact != null) {
            contact.setSupplierId(supplierId);
            contactMapper.insert(contact);
        }

        // 初始化缓存策略
        SupplierCacheStrategy cacheStrategy = new SupplierCacheStrategy();
        cacheStrategy.setSupplierId(supplierId);
        cacheStrategy.setParticipateListPrice(1);
        cacheStrategy.setDetailPriceSource("CACHE_FIRST");
        cacheStrategyMapper.insert(cacheStrategy);

        // 记录日志
        saveLog(supplierId, supplier.getSupplierName(), OperateType.CREATE, "供应商", "新增供应商: " + supplier.getSupplierName(), "system");

        log.info("新增供应商成功, id={}, code={}", supplierId, supplier.getSupplierCode());
        return supplierId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSupplier(Long id, Supplier supplier, List<SupplierWorkSchedule> schedules, SupplierContact contact) {
        Supplier old = getById(id);
        supplier.setId(id);
        supplierMapper.updateById(supplier);

        // 更新工作时间(先删后插)
        if (schedules != null) {
            scheduleMapper.delete(new LambdaQueryWrapper<SupplierWorkSchedule>().eq(SupplierWorkSchedule::getSupplierId, id));
            for (SupplierWorkSchedule s : schedules) {
                s.setId(null);
                s.setSupplierId(id);
                scheduleMapper.insert(s);
            }
        }

        // 更新联系人
        if (contact != null) {
            contactMapper.delete(new LambdaQueryWrapper<SupplierContact>().eq(SupplierContact::getSupplierId, id));
            contact.setId(null);
            contact.setSupplierId(id);
            contactMapper.insert(contact);
        }

        saveLog(id, old.getSupplierName(), OperateType.EDIT, "供应商", "编辑供应商信息", "system");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status, String operator) {
        Supplier supplier = getById(id);
        supplier.setStatus(status);
        supplierMapper.updateById(supplier);

        OperateType type = status == 1 ? OperateType.ONLINE : OperateType.OFFLINE;
        saveLog(id, supplier.getSupplierName(), type, "供应商状态", type.getDesc() + "供应商: " + supplier.getSupplierName(), operator);
    }

    @Override
    public List<SupplierWorkSchedule> getWorkSchedules(Long supplierId) {
        return scheduleMapper.selectList(
            new LambdaQueryWrapper<SupplierWorkSchedule>().eq(SupplierWorkSchedule::getSupplierId, supplierId));
    }

    @Override
    public SupplierContact getContact(Long supplierId) {
        return contactMapper.selectOne(
            new LambdaQueryWrapper<SupplierContact>().eq(SupplierContact::getSupplierId, supplierId));
    }

    private void saveLog(Long supplierId, String supplierName, OperateType type, String target, String content, String operator) {
        SupplierOperationLog opLog = new SupplierOperationLog();
        opLog.setSupplierId(supplierId);
        opLog.setSupplierName(supplierName);
        opLog.setOperator(operator);
        opLog.setOperateTime(LocalDateTime.now());
        opLog.setOperateType(type.name());
        opLog.setOperateTarget(target);
        opLog.setOperateContent(content);
        logMapper.insert(opLog);
    }
}

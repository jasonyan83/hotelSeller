package com.ceair.hotel.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ceair.hotel.admin.mapper.SupplierOperationLogMapper;
import com.ceair.hotel.admin.service.OperationLogService;
import com.ceair.hotel.common.dto.PageResult;
import com.ceair.hotel.common.entity.SupplierOperationLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {

    private final SupplierOperationLogMapper logMapper;

    @Override
    public PageResult<SupplierOperationLog> listLogs(Long supplierId, String operateType, int pageNo, int pageSize) {
        LambdaQueryWrapper<SupplierOperationLog> wrapper = new LambdaQueryWrapper<>();
        if (supplierId != null) {
            wrapper.eq(SupplierOperationLog::getSupplierId, supplierId);
        }
        if (StringUtils.hasText(operateType)) {
            wrapper.eq(SupplierOperationLog::getOperateType, operateType);
        }
        wrapper.orderByDesc(SupplierOperationLog::getOperateTime);

        Page<SupplierOperationLog> page = logMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        return PageResult.of(page.getRecords(), page.getTotal(), pageNo, pageSize);
    }
}

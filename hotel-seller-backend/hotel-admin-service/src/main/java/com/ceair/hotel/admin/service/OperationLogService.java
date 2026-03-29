package com.ceair.hotel.admin.service;

import com.ceair.hotel.common.dto.PageResult;
import com.ceair.hotel.common.entity.SupplierOperationLog;

/**
 * 操作日志服务接口
 */
public interface OperationLogService {

    /** 分页查询供应商操作日志 */
    PageResult<SupplierOperationLog> listLogs(Long supplierId, String operateType, int pageNo, int pageSize);
}

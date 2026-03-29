package com.ceair.hotel.admin.controller;

import com.ceair.hotel.admin.service.OperationLogService;
import com.ceair.hotel.common.dto.PageResult;
import com.ceair.hotel.common.dto.R;
import com.ceair.hotel.common.entity.SupplierOperationLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志 Controller
 */
@Api(tags = "操作日志")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class OperationLogController {

    private final OperationLogService operationLogService;

    @ApiOperation("查询供应商操作日志")
    @GetMapping("/suppliers/{supplierId}/logs")
    public R<PageResult<SupplierOperationLog>> supplierLogs(
            @PathVariable Long supplierId,
            @RequestParam(required = false) String operateType,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        return R.ok(operationLogService.listLogs(supplierId, operateType, pageNo, pageSize));
    }

    @ApiOperation("查询全部操作日志")
    @GetMapping("/logs")
    public R<PageResult<SupplierOperationLog>> allLogs(
            @RequestParam(required = false) String operateType,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        return R.ok(operationLogService.listLogs(null, operateType, pageNo, pageSize));
    }
}

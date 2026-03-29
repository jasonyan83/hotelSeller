package com.ceair.hotel.admin.controller;

import com.ceair.hotel.admin.service.SupplierService;
import com.ceair.hotel.common.dto.PageResult;
import com.ceair.hotel.common.dto.R;
import com.ceair.hotel.common.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商管理 Controller
 */
@Api(tags = "供应商管理")
@RestController
@RequestMapping("/api/v1/admin/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @ApiOperation("分页查询供应商列表")
    @GetMapping
    public R<PageResult<Supplier>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(supplierService.listSuppliers(keyword, status, pageNo, pageSize));
    }

    @ApiOperation("查询供应商详情")
    @GetMapping("/{id}")
    public R<SupplierDetailVO> detail(@PathVariable Long id) {
        Supplier supplier = supplierService.getById(id);
        List<SupplierWorkSchedule> schedules = supplierService.getWorkSchedules(id);
        SupplierContact contact = supplierService.getContact(id);

        SupplierDetailVO vo = new SupplierDetailVO();
        vo.setSupplier(supplier);
        vo.setSchedules(schedules);
        vo.setContact(contact);
        return R.ok(vo);
    }

    @ApiOperation("新增供应商")
    @PostMapping
    public R<Long> add(@RequestBody SupplierSaveCmd cmd) {
        Long id = supplierService.addSupplier(cmd.getSupplier(), cmd.getSchedules(), cmd.getContact());
        return R.ok("新增成功", id);
    }

    @ApiOperation("编辑供应商")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody SupplierSaveCmd cmd) {
        supplierService.updateSupplier(id, cmd.getSupplier(), cmd.getSchedules(), cmd.getContact());
        return R.ok("编辑成功", null);
    }

    @ApiOperation("上下线供应商")
    @PutMapping("/{id}/status")
    public R<Void> updateStatus(@PathVariable Long id, @RequestBody StatusCmd cmd) {
        supplierService.updateStatus(id, cmd.getStatus(), cmd.getOperator());
        return R.ok();
    }

    @ApiOperation("查询供应商工作时间")
    @GetMapping("/{id}/schedules")
    public R<List<SupplierWorkSchedule>> schedules(@PathVariable Long id) {
        return R.ok(supplierService.getWorkSchedules(id));
    }

    @ApiOperation("查询供应商联系人")
    @GetMapping("/{id}/contact")
    public R<SupplierContact> contact(@PathVariable Long id) {
        return R.ok(supplierService.getContact(id));
    }

    /* ---------- 内部VO/DTO ---------- */

    @Data
    public static class SupplierSaveCmd {
        private Supplier supplier;
        private List<SupplierWorkSchedule> schedules;
        private SupplierContact contact;
    }

    @Data
    public static class StatusCmd {
        private Integer status;
        private String operator;
    }

    @Data
    public static class SupplierDetailVO {
        private Supplier supplier;
        private List<SupplierWorkSchedule> schedules;
        private SupplierContact contact;
    }
}

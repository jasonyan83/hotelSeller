package com.ceair.hotel.admin.service;

import com.ceair.hotel.common.dto.PageResult;
import com.ceair.hotel.common.entity.*;
import java.util.List;

/**
 * 供应商管理服务接口
 */
public interface SupplierService {

    /** 分页查询供应商列表 */
    PageResult<Supplier> listSuppliers(String keyword, Integer status, int pageNo, int pageSize);

    /** 根据ID查询供应商详情(含工作时间/联系人/缓存策略) */
    Supplier getById(Long id);

    /** 新增供应商 */
    Long addSupplier(Supplier supplier, List<SupplierWorkSchedule> schedules, SupplierContact contact);

    /** 编辑供应商 */
    void updateSupplier(Long id, Supplier supplier, List<SupplierWorkSchedule> schedules, SupplierContact contact);

    /** 上下线供应商 */
    void updateStatus(Long id, Integer status, String operator);

    /** 获取工作时间 */
    List<SupplierWorkSchedule> getWorkSchedules(Long supplierId);

    /** 获取联系人 */
    SupplierContact getContact(Long supplierId);
}

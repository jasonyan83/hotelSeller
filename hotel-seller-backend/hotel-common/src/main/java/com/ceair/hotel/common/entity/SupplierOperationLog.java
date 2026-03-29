package com.ceair.hotel.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 供应商操作日志表
 */
@Data
@TableName("t_supplier_operation_log")
public class SupplierOperationLog implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long supplierId;
    private String supplierName;
    private String operator;
    private LocalDateTime operateTime;

    /** 操作类别: CREATE/EDIT/DELETE/ONLINE/OFFLINE */
    private String operateType;

    /** 操作对象 */
    private String operateTarget;

    /** 操作内容 */
    private String operateContent;
}

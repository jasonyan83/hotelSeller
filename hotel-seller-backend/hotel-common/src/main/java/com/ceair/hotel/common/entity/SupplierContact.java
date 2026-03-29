package com.ceair.hotel.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;

/**
 * 供应商联系信息
 */
@Data
@TableName("t_supplier_contact")
public class SupplierContact implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long supplierId;

    /** 我方商务负责人 */
    private String businessContact;
    private String businessPhone;
    private String businessEmail;

    /** 对方客服 */
    private String supplierContact;
    private String supplierPhone;
    private String supplierEmail;
}

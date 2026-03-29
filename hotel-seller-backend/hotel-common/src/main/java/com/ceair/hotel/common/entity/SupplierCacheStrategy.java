package com.ceair.hotel.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 供应商缓存策略表
 */
@Data
@TableName("t_supplier_cache_strategy")
public class SupplierCacheStrategy implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long supplierId;

    /** 是否参与列表页报价: 1-是 0-否 */
    private Integer participateListPrice;

    /** CACHE_FIRST/REALTIME */
    private String detailPriceSource;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}

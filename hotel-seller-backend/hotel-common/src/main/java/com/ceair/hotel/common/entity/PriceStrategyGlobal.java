package com.ceair.hotel.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 全局价格策略表
 */
@Data
@TableName("t_price_strategy_global")
public class PriceStrategyGlobal implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long supplierId;

    /** 加价比例(%) */
    private BigDecimal markupRate;

    /** 加价金额(元/间夜) */
    private BigDecimal markupAmount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}

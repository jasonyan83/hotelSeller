package com.ceair.hotel.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 特殊价格策略表
 */
@Data
@TableName("t_price_strategy_special")
public class PriceStrategySpecial implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long supplierId;

    /** 策略编号 */
    private String strategyNo;

    /** 适用渠道JSON */
    private String channel;

    /** 各渠道加减价配置JSON */
    private String channelConfigs;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @TableLogic
    private Integer isDeleted;
}

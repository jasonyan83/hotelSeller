package com.ceair.hotel.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 报价快照表(三级降级兜底数据源)
 */
@Data
@TableName("t_price_snapshot")
public class PriceSnapshot implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String hotelId;
    private String roomTypeId;
    private String roomTypeName;
    private String sellRoomId;
    private String sellRoomName;
    private String supplierId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    /** 售卖价(已加价) */
    private BigDecimal sellPrice;

    /** 底价(供应商原价) */
    private BigDecimal costPrice;

    private String currency;
    private Integer breakfastCount;
    private String cancelRuleDesc;
    private String guaranteeDesc;

    /** 支付方式: 1-预付 2-现付 */
    private Integer paymentType;

    private String promotionTags;

    /** 快照采集时间 */
    private LocalDateTime snapshotTime;

    /** 数据来源: 1-用户搜索回写 2-后台定时探测 3-详情页回写 */
    private Integer dataSource;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
}

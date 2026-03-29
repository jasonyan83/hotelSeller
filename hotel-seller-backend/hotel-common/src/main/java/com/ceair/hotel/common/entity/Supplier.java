package com.ceair.hotel.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 供应商信息表
 */
@Data
@TableName("t_supplier")
public class Supplier implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 供应商编号 */
    private String supplierCode;

    /** 供应商名称 */
    private String supplierName;

    /** 品牌名 */
    private String brandName;

    /** 状态: 1-上线 0-下线 */
    private Integer status;

    /** 上架渠道JSON: ["APP","H5","WEB"] */
    private String channels;

    /** 报价模式 */
    private String quoteMode;

    /** 开通业务JSON: ["PREPAY","PAY_AT_HOTEL"] */
    private String businessTypes;

    /** 报价类型: BASE_PRICE/SELL_PRICE */
    private String quoteType;

    /** 结算币种 */
    private String settlementCurrency;

    /** 结算周期 */
    private String settlementCycle;

    /** 预付结算方式 */
    private String prepaySettleMode;

    /** 预付佣金比例 */
    private java.math.BigDecimal prepayCommissionRate;

    /** 现付结算方式 */
    private String payatSettleMode;

    /** 现付佣金比例 */
    private java.math.BigDecimal payatCommissionRate;

    /** 数据来源: API/EBOOKING */
    private String dataSource;

    /** 对接平台 */
    private String platform;

    /** API地址JSON */
    private String apiUrls;

    /** IP白名单 */
    private String ipWhitelist;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @TableLogic
    private Integer isDeleted;
}

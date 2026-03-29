package com.ceair.hotel.pricing.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 房型报价DTO
 */
@Data
public class RoomQuoteDTO {

    /** 物理房型ID */
    private String roomTypeId;

    /** 物理房型名称 */
    private String roomTypeName;

    /** 房型图片 */
    private String roomImage;

    /** 床型描述 */
    private String bedType;

    /** 面积 */
    private String area;

    /** 最大入住人数 */
    private Integer maxOccupancy;

    /** 楼层信息 */
    private String floor;

    /** 窗户 */
    private String windowType;

    /** WiFi */
    private Boolean hasWifi;

    /** 该房型下的产品列表 */
    private List<SellRoomQuoteDTO> sellRooms;

    @Data
    public static class SellRoomQuoteDTO {
        /** 产品房型ID */
        private String sellRoomId;

        /** 产品房型名称 */
        private String sellRoomName;

        /** 供应商ID */
        private String supplierId;

        /** 供应商名称 */
        private String supplierName;

        /** 每晚售卖价(已加价) */
        private BigDecimal sellPrice;

        /** 总价(多夜) */
        private BigDecimal totalPrice;

        /** 币种 */
        private String currency;

        /** 支付方式: 1-预付 2-现付 3-担保 */
        private Integer paymentType;

        /** 早餐份数 */
        private Integer breakfastCount;

        /** 早餐描述 */
        private String breakfastDesc;

        /** 取消规则描述 */
        private String cancelRuleDesc;

        /** 是否免费取消 */
        private Boolean isFreeCancelable;

        /** 担保描述 */
        private String guaranteeDesc;

        /** 促销标签 */
        private List<String> promotionTags;

        /** 价格来源: NORMAL_REF / APPROX_REF / NO_PRICE */
        private String priceRef;

        /** 是否该房型最低价 */
        private Boolean isLowest;
    }
}

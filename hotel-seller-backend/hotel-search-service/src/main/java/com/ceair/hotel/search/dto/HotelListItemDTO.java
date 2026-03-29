package com.ceair.hotel.search.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 酒店搜索结果DTO - 列表页酒店卡片
 */
@Data
public class HotelListItemDTO {

    private String hotelId;
    private String hotelName;
    private String hotelNameEn;
    private String starLevel;        // LUXURY/HIGH/COMFORT/ECONOMY
    private BigDecimal rating;       // 评分
    private Integer reviewCount;     // 评价数
    private String ratingLabel;      // "很好"/"好"/"极好"
    private String mainImage;
    private List<String> images;
    private String address;
    private String cityName;
    private String districtName;

    /** 起步价(已加价后的最终售卖价) */
    private BigDecimal startingPrice;

    /** 币种 */
    private String currency;

    /** 价格来源标记: NORMAL_REF / APPROX_REF / NO_PRICE */
    private String priceRef;

    /** 促销标签 */
    private List<String> promotionTags;

    /** 早餐描述(起步价对应) */
    private String breakfastInfo;

    /** 距离(km) */
    private BigDecimal distance;

    /** 是否推荐酒店 */
    private Boolean isRecommended;

    /** 供应商ID */
    private String supplierId;
}

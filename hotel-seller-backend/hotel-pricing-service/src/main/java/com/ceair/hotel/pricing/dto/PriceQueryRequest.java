package com.ceair.hotel.pricing.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 报价查询请求DTO
 */
@Data
public class PriceQueryRequest {

    /** 酒店ID */
    private String hotelId;

    /** 入住日期 */
    private LocalDate checkInDate;

    /** 离店日期 */
    private LocalDate checkOutDate;

    /** 房间数 */
    private Integer roomCount = 1;

    /** 成人数 */
    private Integer adultCount = 2;

    /** 儿童数 */
    private Integer childCount = 0;

    /** 渠道: APP/H5/WEB */
    private String channel = "H5";
}

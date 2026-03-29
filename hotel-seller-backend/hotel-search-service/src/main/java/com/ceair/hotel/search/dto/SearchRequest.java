package com.ceair.hotel.search.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * 酒店搜索请求DTO
 */
@Data
public class SearchRequest {

    /** 目的地编码 */
    private String destinationCode;

    /** 目的地名称 */
    private String destinationName;

    /** 搜索关键词 */
    private String keyword;

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

    /** 儿童年龄 */
    private List<Integer> childAges;

    /** 星级筛选: ["LUXURY","HIGH","COMFORT","ECONOMY"] */
    private List<String> starLevels;

    /** 价格范围 min */
    private Integer priceMin;

    /** 价格范围 max */
    private Integer priceMax;

    /** 排序方式: RECOMMEND / PRICE_ASC / PRICE_DESC / RATING_DESC / DISTANCE_ASC */
    private String sortBy = "RECOMMEND";

    private int pageNo = 1;
    private int pageSize = 20;
}

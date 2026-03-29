package com.ceair.hotel.search.dto;

import lombok.Data;

/**
 * 搜索建议项DTO
 */
@Data
public class SuggestItemDTO {

    /** 关键词 */
    private String keyword;

    /** 类型: CITY/HOTEL/POI/BRAND/DISTRICT */
    private String type;

    /** 类型标签: "城市"/"酒店"/"景点"/"品牌"/"商圈" */
    private String typeLabel;

    /** 关联ID */
    private String refId;

    /** 副标题/描述 */
    private String subtitle;

    /** 热度值 */
    private Long heatScore;
}

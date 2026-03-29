package com.ceair.hotel.search.service;

import com.ceair.hotel.common.dto.PageResult;
import com.ceair.hotel.search.dto.HotelListItemDTO;
import com.ceair.hotel.search.dto.SearchRequest;

/**
 * 酒店搜索服务接口
 */
public interface HotelSearchService {

    /**
     * 搜索酒店列表
     * 流程: 关键词识别 -> 酒店匹配 -> 获取报价 -> 加价处理 -> 排序 -> 分页返回
     */
    PageResult<HotelListItemDTO> search(SearchRequest request);
}

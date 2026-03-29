package com.ceair.hotel.admin.service;

import com.ceair.hotel.common.dto.PageResult;
import com.ceair.hotel.common.entity.RecommendedHotel;

import java.util.List;

/**
 * 推荐酒店管理服务接口
 */
public interface RecommendedHotelService {

    /** 分页查询推荐酒店列表 */
    PageResult<RecommendedHotel> listRecommendations(String destinationCode, int pageNo, int pageSize);

    /** 添加推荐酒店 */
    Long addRecommendation(RecommendedHotel hotel);

    /** 批量删除推荐酒店 */
    void deleteRecommendations(List<Long> ids);

    /** 调整推荐排序 */
    void updateSort(List<RecommendedHotel> sortList);
}

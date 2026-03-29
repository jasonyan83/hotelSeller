package com.ceair.hotel.search.service;

import com.ceair.hotel.search.dto.SuggestItemDTO;

import java.util.List;

/**
 * 搜索建议服务接口
 */
public interface SuggestService {

    /**
     * 获取搜索建议(Suggest)
     * @param keyword 输入关键词
     * @param limit 返回条数
     * @return 建议列表
     */
    List<SuggestItemDTO> suggest(String keyword, int limit);
}

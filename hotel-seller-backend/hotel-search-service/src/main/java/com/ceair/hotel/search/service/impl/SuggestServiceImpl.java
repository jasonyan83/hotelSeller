package com.ceair.hotel.search.service.impl;

import com.ceair.hotel.search.dto.SuggestItemDTO;
import com.ceair.hotel.search.service.SuggestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 搜索建议服务实现
 * 一期使用内存Mock数据, 后续接入ES
 */
@Slf4j
@Service
public class SuggestServiceImpl implements SuggestService {

    private static final List<SuggestItemDTO> SUGGEST_DATA = initSuggestData();

    @Override
    public List<SuggestItemDTO> suggest(String keyword, int limit) {
        if (!StringUtils.hasText(keyword)) {
            // 返回热门推荐
            return SUGGEST_DATA.stream()
                    .sorted(Comparator.comparing(SuggestItemDTO::getHeatScore).reversed())
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        String kw = keyword.toLowerCase();
        return SUGGEST_DATA.stream()
                .filter(s -> s.getKeyword().toLowerCase().contains(kw)
                        || (s.getSubtitle() != null && s.getSubtitle().toLowerCase().contains(kw)))
                .sorted(Comparator.comparing(SuggestItemDTO::getHeatScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private static List<SuggestItemDTO> initSuggestData() {
        List<SuggestItemDTO> list = new ArrayList<>();

        list.add(buildSuggest("上海", "CITY", "城市", "SH", "中国·上海市", 100000L));
        list.add(buildSuggest("北京", "CITY", "城市", "BJ", "中国·北京市", 95000L));
        list.add(buildSuggest("广州", "CITY", "城市", "GZ", "中国·广州市", 80000L));
        list.add(buildSuggest("深圳", "CITY", "城市", "SZ", "中国·深圳市", 75000L));
        list.add(buildSuggest("杭州", "CITY", "城市", "HZ", "中国·杭州市", 70000L));
        list.add(buildSuggest("成都", "CITY", "城市", "CD", "中国·成都市", 68000L));
        list.add(buildSuggest("三亚", "CITY", "城市", "SY", "中国·海南省三亚市", 65000L));

        list.add(buildSuggest("外滩", "POI", "景点", "POI001", "上海市黄浦区", 50000L));
        list.add(buildSuggest("陆家嘴", "DISTRICT", "商圈", "D001", "上海市浦东新区", 45000L));
        list.add(buildSuggest("南京路步行街", "POI", "景点", "POI002", "上海市黄浦区", 42000L));
        list.add(buildSuggest("迪士尼乐园", "POI", "景点", "POI003", "上海市浦东新区", 55000L));

        list.add(buildSuggest("华尔道夫", "BRAND", "品牌", "BRAND001", "希尔顿集团", 30000L));
        list.add(buildSuggest("丽思卡尔顿", "BRAND", "品牌", "BRAND002", "万豪集团", 28000L));
        list.add(buildSuggest("香格里拉", "BRAND", "品牌", "BRAND003", "香格里拉集团", 25000L));
        list.add(buildSuggest("亚朵", "BRAND", "品牌", "BRAND004", "亚朵集团", 35000L));
        list.add(buildSuggest("全季", "BRAND", "品牌", "BRAND005", "华住集团", 33000L));

        list.add(buildSuggest("上海外滩华尔道夫酒店", "HOTEL", "酒店", "H001", "黄浦区 · 豪华型", 20000L));
        list.add(buildSuggest("上海浦东丽思卡尔顿酒店", "HOTEL", "酒店", "H002", "浦东新区 · 豪华型", 18000L));
        list.add(buildSuggest("北京王府井文华东方酒店", "HOTEL", "酒店", "H101", "东城区 · 豪华型", 15000L));

        return list;
    }

    private static SuggestItemDTO buildSuggest(String keyword, String type, String typeLabel,
                                                String refId, String subtitle, Long heatScore) {
        SuggestItemDTO item = new SuggestItemDTO();
        item.setKeyword(keyword);
        item.setType(type);
        item.setTypeLabel(typeLabel);
        item.setRefId(refId);
        item.setSubtitle(subtitle);
        item.setHeatScore(heatScore);
        return item;
    }
}

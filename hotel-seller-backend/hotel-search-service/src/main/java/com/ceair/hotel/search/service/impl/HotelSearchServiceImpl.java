package com.ceair.hotel.search.service.impl;

import com.ceair.hotel.common.dto.PageResult;
import com.ceair.hotel.search.dto.HotelListItemDTO;
import com.ceair.hotel.search.dto.SearchRequest;
import com.ceair.hotel.search.service.HotelSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 酒店搜索服务实现
 * 一期使用内存Mock数据, 后续接入ES + 供应商API
 */
@Slf4j
@Service
public class HotelSearchServiceImpl implements HotelSearchService {

    /** Mock酒店数据 */
    private static final List<HotelListItemDTO> MOCK_HOTELS = initMockData();

    @Override
    public PageResult<HotelListItemDTO> search(SearchRequest request) {
        log.info("酒店搜索: dest={}, keyword={}, checkin={}, checkout={}, sort={}",
                request.getDestinationCode(), request.getKeyword(),
                request.getCheckInDate(), request.getCheckOutDate(), request.getSortBy());

        List<HotelListItemDTO> filtered = new ArrayList<>(MOCK_HOTELS);

        // 1. 关键词过滤
        if (StringUtils.hasText(request.getKeyword())) {
            String kw = request.getKeyword().toLowerCase();
            filtered = filtered.stream()
                    .filter(h -> h.getHotelName().toLowerCase().contains(kw)
                            || (h.getHotelNameEn() != null && h.getHotelNameEn().toLowerCase().contains(kw))
                            || (h.getAddress() != null && h.getAddress().contains(kw)))
                    .collect(Collectors.toList());
        }

        // 2. 目的地过滤
        if (StringUtils.hasText(request.getDestinationCode())) {
            filtered = filtered.stream()
                    .filter(h -> request.getDestinationCode().equals(h.getCityName()))
                    .collect(Collectors.toList());
        }

        // 3. 星级筛选
        if (request.getStarLevels() != null && !request.getStarLevels().isEmpty()) {
            filtered = filtered.stream()
                    .filter(h -> request.getStarLevels().contains(h.getStarLevel()))
                    .collect(Collectors.toList());
        }

        // 4. 价格区间筛选
        if (request.getPriceMin() != null) {
            filtered = filtered.stream()
                    .filter(h -> h.getStartingPrice() != null
                            && h.getStartingPrice().intValue() >= request.getPriceMin())
                    .collect(Collectors.toList());
        }
        if (request.getPriceMax() != null) {
            filtered = filtered.stream()
                    .filter(h -> h.getStartingPrice() != null
                            && h.getStartingPrice().intValue() <= request.getPriceMax())
                    .collect(Collectors.toList());
        }

        // 5. 排序
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "RECOMMEND";
        switch (sortBy) {
            case "PRICE_ASC":
                filtered.sort(Comparator.comparing(
                        h -> h.getStartingPrice() != null ? h.getStartingPrice() : BigDecimal.valueOf(999999)));
                break;
            case "PRICE_DESC":
                filtered.sort(Comparator.comparing(
                        (HotelListItemDTO h) -> h.getStartingPrice() != null ? h.getStartingPrice() : BigDecimal.ZERO).reversed());
                break;
            case "RATING_DESC":
                filtered.sort(Comparator.comparing(
                        (HotelListItemDTO h) -> h.getRating() != null ? h.getRating() : BigDecimal.ZERO).reversed());
                break;
            case "DISTANCE_ASC":
                filtered.sort(Comparator.comparing(
                        h -> h.getDistance() != null ? h.getDistance() : BigDecimal.valueOf(999)));
                break;
            default: // RECOMMEND - 推荐酒店优先 + 评分排序
                filtered.sort((a, b) -> {
                    if (Boolean.TRUE.equals(a.getIsRecommended()) && !Boolean.TRUE.equals(b.getIsRecommended()))
                        return -1;
                    if (!Boolean.TRUE.equals(a.getIsRecommended()) && Boolean.TRUE.equals(b.getIsRecommended()))
                        return 1;
                    return (b.getRating() != null ? b.getRating() : BigDecimal.ZERO)
                            .compareTo(a.getRating() != null ? a.getRating() : BigDecimal.ZERO);
                });
        }

        // 6. 分页
        int total = filtered.size();
        int from = (request.getPageNo() - 1) * request.getPageSize();
        int to = Math.min(from + request.getPageSize(), total);
        List<HotelListItemDTO> pageData = from < total ? filtered.subList(from, to) : Collections.emptyList();

        return PageResult.of(pageData, (long) total, request.getPageNo(), request.getPageSize());
    }

    private static List<HotelListItemDTO> initMockData() {
        List<HotelListItemDTO> list = new ArrayList<>();

        list.add(buildHotel("H001", "上海外滩华尔道夫酒店", "Waldorf Astoria Shanghai on the Bund",
                "LUXURY", new BigDecimal("4.8"), 2456, "极好",
                "上海市黄浦区中山东一路2号", "上海", "黄浦区",
                new BigDecimal("2580"), "NORMAL_REF", Arrays.asList("会员9折", "免费取消"),
                "含双早", new BigDecimal("1.2"), true, "BOOKING"));

        list.add(buildHotel("H002", "上海浦东丽思卡尔顿酒店", "The Ritz-Carlton Shanghai Pudong",
                "LUXURY", new BigDecimal("4.7"), 1823, "极好",
                "上海市浦东新区陆家嘴世纪大道8号", "上海", "浦东新区",
                new BigDecimal("2180"), "NORMAL_REF", Arrays.asList("限时特惠"),
                "含双早", new BigDecimal("3.5"), true, "EXPEDIA"));

        list.add(buildHotel("H003", "上海静安香格里拉大酒店", "Jing An Shangri-La Shanghai",
                "LUXURY", new BigDecimal("4.6"), 1567, "很好",
                "上海市静安区延安中路1218号", "上海", "静安区",
                new BigDecimal("1680"), "NORMAL_REF", Arrays.asList("连住优惠"),
                "含双早", new BigDecimal("2.8"), false, "BOOKING"));

        list.add(buildHotel("H004", "上海新天地安达仕酒店", "Andaz Xintiandi Shanghai",
                "HIGH", new BigDecimal("4.5"), 987, "很好",
                "上海市黄浦区嵩山路88号", "上海", "黄浦区",
                new BigDecimal("1280"), "NORMAL_REF", Arrays.asList("会员8折"),
                "含单早", new BigDecimal("1.5"), false, "CTRIP"));

        list.add(buildHotel("H005", "上海虹桥雅高美爵酒店", "Grand Mercure Shanghai Hongqiao",
                "HIGH", new BigDecimal("4.3"), 765, "好",
                "上海市闵行区申虹路9号", "上海", "闵行区",
                new BigDecimal("680"), "APPROX_REF", Collections.emptyList(),
                "含双早", new BigDecimal("8.2"), false, "MEITUAN"));

        list.add(buildHotel("H006", "上海静安亚朵酒店", "Atour Hotel Shanghai Jing'an",
                "COMFORT", new BigDecimal("4.4"), 543, "很好",
                "上海市静安区江宁路669号", "上海", "静安区",
                new BigDecimal("450"), "NORMAL_REF", Arrays.asList("新客立减"),
                "不含早", new BigDecimal("2.0"), false, "TONGCHENG"));

        list.add(buildHotel("H007", "上海浦东假日酒店", "Holiday Inn Shanghai Pudong",
                "COMFORT", new BigDecimal("4.1"), 432, "好",
                "上海市浦东新区东方路899号", "上海", "浦东新区",
                null, "NO_PRICE", Collections.emptyList(),
                null, new BigDecimal("4.0"), false, "AGODA"));

        list.add(buildHotel("H008", "全季酒店(上海南京路步行街店)", "JI Hotel Shanghai Nanjing Road",
                "ECONOMY", new BigDecimal("4.2"), 321, "好",
                "上海市黄浦区福建中路525号", "上海", "黄浦区",
                new BigDecimal("380"), "NORMAL_REF", Collections.emptyList(),
                "不含早", new BigDecimal("0.8"), false, "CTRIP"));

        // 北京酒店
        list.add(buildHotel("H101", "北京王府井文华东方酒店", "Mandarin Oriental Wangfujing Beijing",
                "LUXURY", new BigDecimal("4.9"), 1234, "极好",
                "北京市东城区王府井大街269号", "北京", "东城区",
                new BigDecimal("3200"), "NORMAL_REF", Arrays.asList("限时特惠", "免费取消"),
                "含双早", new BigDecimal("0.5"), true, "BOOKING"));

        list.add(buildHotel("H102", "北京国贸大酒店", "China World Hotel Beijing",
                "LUXURY", new BigDecimal("4.6"), 2100, "很好",
                "北京市朝阳区建国门外大街1号", "北京", "朝阳区",
                new BigDecimal("1880"), "NORMAL_REF", Arrays.asList("连住优惠"),
                "含双早", new BigDecimal("5.0"), false, "EXPEDIA"));

        return list;
    }

    private static HotelListItemDTO buildHotel(String hotelId, String name, String nameEn,
                                                String starLevel, BigDecimal rating, int reviewCount, String ratingLabel,
                                                String address, String city, String district,
                                                BigDecimal price, String priceRef, List<String> promotions,
                                                String breakfast, BigDecimal distance, boolean recommended, String supplierId) {
        HotelListItemDTO h = new HotelListItemDTO();
        h.setHotelId(hotelId);
        h.setHotelName(name);
        h.setHotelNameEn(nameEn);
        h.setStarLevel(starLevel);
        h.setRating(rating);
        h.setReviewCount(reviewCount);
        h.setRatingLabel(ratingLabel);
        h.setMainImage("https://via.placeholder.com/400x300?text=" + hotelId);
        h.setImages(Arrays.asList(
                "https://via.placeholder.com/400x300?text=" + hotelId + "-1",
                "https://via.placeholder.com/400x300?text=" + hotelId + "-2"
        ));
        h.setAddress(address);
        h.setCityName(city);
        h.setDistrictName(district);
        h.setStartingPrice(price);
        h.setCurrency("CNY");
        h.setPriceRef(priceRef);
        h.setPromotionTags(promotions);
        h.setBreakfastInfo(breakfast);
        h.setDistance(distance);
        h.setIsRecommended(recommended);
        h.setSupplierId(supplierId);
        return h;
    }
}

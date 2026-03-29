package com.ceair.hotel.pricing.service.impl;

import com.ceair.hotel.pricing.dto.PriceQueryRequest;
import com.ceair.hotel.pricing.dto.RoomQuoteDTO;
import com.ceair.hotel.pricing.dto.RoomQuoteDTO.SellRoomQuoteDTO;
import com.ceair.hotel.pricing.service.PricingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 报价服务实现
 * 一期使用Mock数据模拟三级降级逻辑
 */
@Slf4j
@Service
public class PricingServiceImpl implements PricingService {

    @Override
    public List<RoomQuoteDTO> getHotelPricing(PriceQueryRequest request) {
        log.info("获取酒店报价: hotelId={}, checkin={}, checkout={}, channel={}",
                request.getHotelId(), request.getCheckInDate(), request.getCheckOutDate(), request.getChannel());

        long nights = 1;
        if (request.getCheckInDate() != null && request.getCheckOutDate() != null) {
            nights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
            if (nights <= 0) nights = 1;
        }

        // Mock不同酒店的报价数据
        Map<String, List<RoomQuoteDTO>> hotelPrices = buildMockPricingData(nights);
        List<RoomQuoteDTO> result = hotelPrices.get(request.getHotelId());
        if (result == null) {
            result = buildDefaultPricing(request.getHotelId(), nights);
        }

        return result;
    }

    private Map<String, List<RoomQuoteDTO>> buildMockPricingData(long nights) {
        Map<String, List<RoomQuoteDTO>> map = new HashMap<>();

        // H001 上海外滩华尔道夫
        List<RoomQuoteDTO> h001Rooms = new ArrayList<>();
        h001Rooms.add(buildRoom("RT001", "豪华大床房", "大床1.8m", "45㎡", 2, "高楼层", "落地窗",
                Arrays.asList(
                        buildSellRoom("SR001", "含双早·限时特惠", "BOOKING", "Booking.com",
                                new BigDecimal("2580"), nights, 1, 2, "含双早", "入住前1天18:00前免费取消",
                                true, null, Arrays.asList("限时特惠", "免费取消"), "NORMAL_REF", true),
                        buildSellRoom("SR002", "含双早·不可取消", "BOOKING", "Booking.com",
                                new BigDecimal("2280"), nights, 1, 2, "含双早", "不可取消",
                                false, null, Collections.emptyList(), "NORMAL_REF", false)
                )));
        h001Rooms.add(buildRoom("RT002", "外滩景观套房", "大床1.8m", "72㎡", 2, "高楼层", "全景落地窗",
                Arrays.asList(
                        buildSellRoom("SR003", "含双早·外滩景观", "EXPEDIA", "Expedia",
                                new BigDecimal("4880"), nights, 1, 2, "含双早", "入住前3天免费取消",
                                true, null, Arrays.asList("会员9折"), "NORMAL_REF", true)
                )));
        h001Rooms.add(buildRoom("RT003", "行政豪华双床房", "双床1.2m", "48㎡", 3, "中楼层", "景观窗",
                Arrays.asList(
                        buildSellRoom("SR004", "含双早·可取消", "CTRIP", "携程分销",
                                new BigDecimal("2980"), nights, 1, 2, "含双早", "入住当天18:00前免费取消",
                                true, null, Collections.emptyList(), "NORMAL_REF", true),
                        buildSellRoom("SR005", "不含早·参考价", "MEITUAN", "美团",
                                new BigDecimal("2680"), nights, 1, 0, "不含早", "不可取消",
                                false, null, Collections.emptyList(), "APPROX_REF", false)
                )));
        map.put("H001", h001Rooms);

        // H002 上海浦东丽思卡尔顿
        List<RoomQuoteDTO> h002Rooms = new ArrayList<>();
        h002Rooms.add(buildRoom("RT004", "豪华客房", "大床1.8m", "50㎡", 2, "高楼层", "城市景观",
                Arrays.asList(
                        buildSellRoom("SR006", "含双早·限时优惠", "EXPEDIA", "Expedia",
                                new BigDecimal("2180"), nights, 1, 2, "含双早", "入住前2天免费取消",
                                true, null, Arrays.asList("限时特惠"), "NORMAL_REF", true)
                )));
        h002Rooms.add(buildRoom("RT005", "浦江景观房", "大床2.0m", "65㎡", 2, "高楼层", "全景落地窗",
                Arrays.asList(
                        buildSellRoom("SR007", "含双早·浦江景观", "BOOKING", "Booking.com",
                                new BigDecimal("3580"), nights, 1, 2, "含双早", "入住前1天免费取消",
                                true, null, Arrays.asList("连住优惠"), "NORMAL_REF", true)
                )));
        map.put("H002", h002Rooms);

        return map;
    }

    private List<RoomQuoteDTO> buildDefaultPricing(String hotelId, long nights) {
        List<RoomQuoteDTO> rooms = new ArrayList<>();
        rooms.add(buildRoom("RT_DEF_1", "标准大床房", "大床1.8m", "35㎡", 2, "中楼层", "有窗",
                Arrays.asList(
                        buildSellRoom("SR_DEF_1", "含早·标准价", "BOOKING", "Booking.com",
                                new BigDecimal("680"), nights, 1, 1, "含单早", "入住前1天免费取消",
                                true, null, Collections.emptyList(), "NORMAL_REF", true)
                )));
        rooms.add(buildRoom("RT_DEF_2", "标准双床房", "双床1.2m", "38㎡", 3, "中楼层", "有窗",
                Arrays.asList(
                        buildSellRoom("SR_DEF_2", "含早·标准价", "CTRIP", "携程分销",
                                new BigDecimal("720"), nights, 1, 1, "含单早", "不可取消",
                                false, null, Collections.emptyList(), "NORMAL_REF", true)
                )));
        return rooms;
    }

    private RoomQuoteDTO buildRoom(String roomTypeId, String roomTypeName, String bedType, String area,
                                    int maxOccupancy, String floor, String windowType,
                                    List<SellRoomQuoteDTO> sellRooms) {
        RoomQuoteDTO room = new RoomQuoteDTO();
        room.setRoomTypeId(roomTypeId);
        room.setRoomTypeName(roomTypeName);
        room.setRoomImage("https://via.placeholder.com/400x300?text=" + roomTypeId);
        room.setBedType(bedType);
        room.setArea(area);
        room.setMaxOccupancy(maxOccupancy);
        room.setFloor(floor);
        room.setWindowType(windowType);
        room.setHasWifi(true);
        room.setSellRooms(sellRooms);
        return room;
    }

    private SellRoomQuoteDTO buildSellRoom(String sellRoomId, String sellRoomName,
                                            String supplierId, String supplierName,
                                            BigDecimal pricePerNight, long nights,
                                            int paymentType, int breakfastCount, String breakfastDesc,
                                            String cancelRuleDesc, boolean isFreeCancelable,
                                            String guaranteeDesc, List<String> promotionTags,
                                            String priceRef, boolean isLowest) {
        SellRoomQuoteDTO sr = new SellRoomQuoteDTO();
        sr.setSellRoomId(sellRoomId);
        sr.setSellRoomName(sellRoomName);
        sr.setSupplierId(supplierId);
        sr.setSupplierName(supplierName);
        sr.setSellPrice(pricePerNight);
        sr.setTotalPrice(pricePerNight.multiply(BigDecimal.valueOf(nights)));
        sr.setCurrency("CNY");
        sr.setPaymentType(paymentType);
        sr.setBreakfastCount(breakfastCount);
        sr.setBreakfastDesc(breakfastDesc);
        sr.setCancelRuleDesc(cancelRuleDesc);
        sr.setIsFreeCancelable(isFreeCancelable);
        sr.setGuaranteeDesc(guaranteeDesc);
        sr.setPromotionTags(promotionTags);
        sr.setPriceRef(priceRef);
        sr.setIsLowest(isLowest);
        return sr;
    }
}

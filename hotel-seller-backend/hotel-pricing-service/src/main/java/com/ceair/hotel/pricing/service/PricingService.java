package com.ceair.hotel.pricing.service;

import com.ceair.hotel.pricing.dto.PriceQueryRequest;
import com.ceair.hotel.pricing.dto.RoomQuoteDTO;

import java.util.List;

/**
 * 报价服务接口
 * 三级降级: L1 Redis缓存 → L2 供应商API → L3 DB报价快照
 */
public interface PricingService {

    /**
     * 获取酒店详情页报价(房型+产品列表)
     */
    List<RoomQuoteDTO> getHotelPricing(PriceQueryRequest request);
}

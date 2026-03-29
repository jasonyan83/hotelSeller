package com.ceair.hotel.pricing.controller;

import com.ceair.hotel.common.dto.R;
import com.ceair.hotel.pricing.dto.PriceQueryRequest;
import com.ceair.hotel.pricing.dto.RoomQuoteDTO;
import com.ceair.hotel.pricing.service.PricingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报价接口 Controller
 */
@Api(tags = "酒店报价")
@RestController
@RequestMapping("/api/v1/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final PricingService pricingService;

    @ApiOperation("获取酒店房型报价列表")
    @PostMapping("/hotel-quotes")
    public R<List<RoomQuoteDTO>> getHotelPricing(@RequestBody PriceQueryRequest request) {
        return R.ok(pricingService.getHotelPricing(request));
    }
}

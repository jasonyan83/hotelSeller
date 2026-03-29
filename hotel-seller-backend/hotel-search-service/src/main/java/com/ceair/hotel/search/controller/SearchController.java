package com.ceair.hotel.search.controller;

import com.ceair.hotel.common.dto.PageResult;
import com.ceair.hotel.common.dto.R;
import com.ceair.hotel.search.dto.HotelListItemDTO;
import com.ceair.hotel.search.dto.SearchRequest;
import com.ceair.hotel.search.dto.SuggestItemDTO;
import com.ceair.hotel.search.service.HotelSearchService;
import com.ceair.hotel.search.service.SuggestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 搜索接口 Controller
 */
@Api(tags = "酒店搜索")
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final HotelSearchService hotelSearchService;
    private final SuggestService suggestService;

    @ApiOperation("搜索酒店列表")
    @PostMapping("/hotels")
    public R<PageResult<HotelListItemDTO>> search(@RequestBody SearchRequest request) {
        return R.ok(hotelSearchService.search(request));
    }

    @ApiOperation("搜索建议(Suggest)")
    @GetMapping("/suggest")
    public R<List<SuggestItemDTO>> suggest(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "10") int limit) {
        return R.ok(suggestService.suggest(keyword, limit));
    }
}

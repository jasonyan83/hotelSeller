package com.ceair.hotel.admin.controller;

import com.ceair.hotel.admin.service.RecommendedHotelService;
import com.ceair.hotel.common.dto.PageResult;
import com.ceair.hotel.common.dto.R;
import com.ceair.hotel.common.entity.RecommendedHotel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 推荐酒店管理 Controller
 */
@Api(tags = "推荐酒店管理")
@RestController
@RequestMapping("/api/v1/admin/recommendations")
@RequiredArgsConstructor
public class RecommendedHotelController {

    private final RecommendedHotelService recommendedHotelService;

    @ApiOperation("分页查询推荐酒店")
    @GetMapping
    public R<PageResult<RecommendedHotel>> list(
            @RequestParam(required = false) String destinationCode,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        return R.ok(recommendedHotelService.listRecommendations(destinationCode, pageNo, pageSize));
    }

    @ApiOperation("添加推荐酒店")
    @PostMapping
    public R<Long> add(@RequestBody RecommendedHotel hotel) {
        Long id = recommendedHotelService.addRecommendation(hotel);
        return R.ok("添加成功", id);
    }

    @ApiOperation("批量删除推荐酒店")
    @DeleteMapping
    public R<Void> delete(@RequestBody DeleteCmd cmd) {
        recommendedHotelService.deleteRecommendations(cmd.getIds());
        return R.ok("删除成功", null);
    }

    @ApiOperation("调整推荐排序")
    @PutMapping("/sort")
    public R<Void> updateSort(@RequestBody List<RecommendedHotel> sortList) {
        recommendedHotelService.updateSort(sortList);
        return R.ok("排序更新成功", null);
    }

    @Data
    public static class DeleteCmd {
        private List<Long> ids;
    }
}

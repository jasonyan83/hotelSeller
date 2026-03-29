package com.ceair.hotel.admin.controller;

import com.ceair.hotel.common.dto.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 数据统计 Controller
 * 提供仪表盘所需的统计数据(当前为Mock数据)
 */
@Api(tags = "数据统计")
@RestController
@RequestMapping("/api/v1/admin/stats")
public class StatisticsController {

    @ApiOperation("首页统计概览")
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("supplierTotal", 8);
        data.put("supplierOnline", 6);
        data.put("supplierOffline", 2);
        data.put("recommendedHotels", 24);
        data.put("priceStrategies", 15);
        data.put("todaySearchCount", 12580);
        data.put("todayBookingCount", 345);
        data.put("cacheHitRate", 0.87);
        return R.ok(data);
    }

    @ApiOperation("搜索趋势统计")
    @GetMapping("/search-trend")
    public R<List<TrendItem>> searchTrend(@RequestParam(defaultValue = "7") int days) {
        List<TrendItem> list = new ArrayList<>();
        Random random = new Random(42);
        Calendar cal = Calendar.getInstance();
        for (int i = days - 1; i >= 0; i--) {
            Calendar day = (Calendar) cal.clone();
            day.add(Calendar.DAY_OF_MONTH, -i);
            TrendItem item = new TrendItem();
            item.setDate(String.format("%d-%02d-%02d",
                    day.get(Calendar.YEAR), day.get(Calendar.MONTH) + 1, day.get(Calendar.DAY_OF_MONTH)));
            item.setSearchCount(8000 + random.nextInt(5000));
            item.setBookingCount(200 + random.nextInt(200));
            list.add(item);
        }
        return R.ok(list);
    }

    @ApiOperation("供应商报价占比")
    @GetMapping("/supplier-ratio")
    public R<List<Map<String, Object>>> supplierRatio() {
        List<Map<String, Object>> list = new ArrayList<>();
        String[] names = {"Booking.com", "Expedia", "携程分销", "美团", "同程艺龙", "Agoda"};
        int[] values = {35, 25, 15, 12, 8, 5};
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", names[i]);
            item.put("value", values[i]);
            list.add(item);
        }
        return R.ok(list);
    }

    @Data
    public static class TrendItem {
        private String date;
        private int searchCount;
        private int bookingCount;
    }
}

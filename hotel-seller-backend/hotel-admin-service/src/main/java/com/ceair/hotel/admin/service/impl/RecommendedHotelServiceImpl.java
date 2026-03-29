package com.ceair.hotel.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ceair.hotel.admin.mapper.RecommendedHotelMapper;
import com.ceair.hotel.admin.service.RecommendedHotelService;
import com.ceair.hotel.common.dto.PageResult;
import com.ceair.hotel.common.entity.RecommendedHotel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendedHotelServiceImpl implements RecommendedHotelService {

    private final RecommendedHotelMapper recommendedHotelMapper;

    @Override
    public PageResult<RecommendedHotel> listRecommendations(String destinationCode, int pageNo, int pageSize) {
        LambdaQueryWrapper<RecommendedHotel> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(destinationCode)) {
            wrapper.eq(RecommendedHotel::getDestinationCode, destinationCode);
        }
        wrapper.orderByAsc(RecommendedHotel::getSortOrder);

        Page<RecommendedHotel> page = recommendedHotelMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        return PageResult.of(page.getRecords(), page.getTotal(), pageNo, pageSize);
    }

    @Override
    public Long addRecommendation(RecommendedHotel hotel) {
        recommendedHotelMapper.insert(hotel);
        log.info("新增推荐酒店, destinationCode={}, hotelId={}", hotel.getDestinationCode(), hotel.getHotelId());
        return hotel.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRecommendations(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            recommendedHotelMapper.deleteBatchIds(ids);
            log.info("批量删除推荐酒店, ids={}", ids);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSort(List<RecommendedHotel> sortList) {
        if (sortList != null) {
            for (RecommendedHotel item : sortList) {
                RecommendedHotel update = new RecommendedHotel();
                update.setId(item.getId());
                update.setSortOrder(item.getSortOrder());
                recommendedHotelMapper.updateById(update);
            }
        }
    }
}

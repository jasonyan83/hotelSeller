package com.ceair.hotel.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ceair.hotel.common.entity.SupplierWorkSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SupplierWorkScheduleMapper extends BaseMapper<SupplierWorkSchedule> {
    void deleteBySupplierIdAndType(@Param("supplierId") Long supplierId, @Param("scheduleType") String scheduleType);
}

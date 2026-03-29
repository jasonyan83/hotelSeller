package com.ceair.hotel.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalTime;

/**
 * 供应商工作时间配置
 */
@Data
@TableName("t_supplier_work_schedule")
public class SupplierWorkSchedule implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long supplierId;

    /** 时间类型: WORK/ORDER_CONFIRM */
    private String scheduleType;

    /** 是否7*24小时 */
    private Integer is24x7;

    /** 星期几: 1-7 */
    private Integer dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;
}

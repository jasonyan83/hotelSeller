package com.ceair.hotel.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 推荐酒店配置表
 */
@Data
@TableName("t_recommended_hotel")
public class RecommendedHotel implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 目的地编码 */
    private String destinationCode;
    private String destinationName;
    private String destinationType;

    /** 酒店ID */
    private String hotelId;
    private String hotelName;

    /** 排序序号(越小越前) */
    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}

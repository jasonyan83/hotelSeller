package com.ceair.hotel.common.dto;

import lombok.Data;
import javax.validation.constraints.Min;

/**
 * 分页请求基类
 */
@Data
public class PageRequest {

    @Min(value = 1, message = "页码不能小于1")
    private int pageNo = 1;

    @Min(value = 1, message = "每页数量不能小于1")
    private int pageSize = 20;
}

package com.ceair.hotel.common.dto;

import lombok.Data;
import java.util.List;

/**
 * 分页响应
 */
@Data
public class PageResult<T> {

    private long totalCount;
    private int pageNo;
    private int pageSize;
    private List<T> list;

    public static <T> PageResult<T> of(List<T> list, long total, int pageNo, int pageSize) {
        PageResult<T> result = new PageResult<>();
        result.setList(list);
        result.setTotalCount(total);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        return result;
    }
}

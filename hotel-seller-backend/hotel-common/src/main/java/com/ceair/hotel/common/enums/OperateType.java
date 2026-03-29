package com.ceair.hotel.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperateType {
    CREATE("新增"),
    EDIT("编辑"),
    DELETE("删除"),
    ONLINE("上线"),
    OFFLINE("下线");

    private final String desc;
}

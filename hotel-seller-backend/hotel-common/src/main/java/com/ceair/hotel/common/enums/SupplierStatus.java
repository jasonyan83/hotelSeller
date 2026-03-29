package com.ceair.hotel.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 供应商状态
 */
@Getter
@AllArgsConstructor
public enum SupplierStatus {
    ONLINE(1, "上线"),
    OFFLINE(0, "下线");

    private final int code;
    private final String desc;

    public static SupplierStatus of(int code) {
        for (SupplierStatus s : values()) {
            if (s.code == code) return s;
        }
        return OFFLINE;
    }
}

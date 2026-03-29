package com.ceair.hotel.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentType {
    PREPAY(1, "预付", "在线付"),
    PAY_AT_HOTEL(2, "现付(非担保)", "到店付"),
    GUARANTEE(3, "担保", "担保");

    private final int code;
    private final String label;
    private final String buttonText;
}

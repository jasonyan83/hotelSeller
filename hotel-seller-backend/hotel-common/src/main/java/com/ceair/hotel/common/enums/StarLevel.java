package com.ceair.hotel.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StarLevel {
    LUXURY("豪华型", 50),
    HIGH("高档型", 40),
    COMFORT("舒适型", 30),
    ECONOMY("经济型", 20);

    private final String label;
    private final int rankScore;
}

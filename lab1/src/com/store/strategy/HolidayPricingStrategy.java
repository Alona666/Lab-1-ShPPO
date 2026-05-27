package com.store.strategy;

import java.math.BigDecimal;

public class HolidayPricingStrategy implements PricingStrategy {
    private static final BigDecimal HOLIDAY_DISCOUNT_MULTIPLIER = new BigDecimal("0.85");

    @Override
    public BigDecimal calculateFinalCost(BigDecimal baseCostValue) {
        if (baseCostValue == null) {
            return BigDecimal.ZERO;
        }
        return baseCostValue.multiply(HOLIDAY_DISCOUNT_MULTIPLIER);
    }
}
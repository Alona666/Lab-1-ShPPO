package com.store.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component("holidayStrategy") // Синглтон для праздничной цены
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
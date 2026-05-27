package com.store.strategy;

import java.math.BigDecimal;

public class RegularPricingStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculateFinalCost(BigDecimal baseCostValue) {
        return baseCostValue;
    }
}
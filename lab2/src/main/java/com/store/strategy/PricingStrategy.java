package com.store.strategy;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculateFinalCost(BigDecimal baseCostValue);
}
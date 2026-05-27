package com.store.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component("regularStrategy") // Синглтон для обычной цены
public class RegularPricingStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculateFinalCost(BigDecimal baseCostValue) {
        return baseCostValue;
    }
}
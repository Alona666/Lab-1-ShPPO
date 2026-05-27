package com.store.visitor;

import com.store.model.BundleItem;
import com.store.model.ProductItem;
import com.store.strategy.PricingStrategy;
import java.math.BigDecimal;

public final class CostVisitor implements PurchasableItemVisitor {
    private final PricingStrategy pricingStrategy;
    private BigDecimal totalCostValue = BigDecimal.ZERO;

    public CostVisitor(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    @Override
    public void visitProduct(ProductItem productItem) {
        BigDecimal finalCost = pricingStrategy.calculateFinalCost(productItem.getBaseCostValue());
        totalCostValue = totalCostValue.add(finalCost);
    }

    @Override
    public void visitBundle(BundleItem bundleItem) {
        // За сам факт упаковки набора плата не взимается
    }

    public BigDecimal getTotalCostValue() {
        return totalCostValue;
    }
}
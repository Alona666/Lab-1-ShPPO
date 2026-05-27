package com.store.model;

import com.store.visitor.PurchasableItemVisitor;
import java.math.BigDecimal;

public final class ProductItem implements PurchasableItem {
    private final String productId;
    private final String productName;
    private final BigDecimal baseCostValue;
    private final double weightInKilograms;

    // Пакетный доступ — конструирование только через Builder
    ProductItem(String productId, String productName, BigDecimal baseCostValue, double weightInKilograms) {
        this.productId = productId;
        this.productName = productName;
        this.baseCostValue = baseCostValue;
        this.weightInKilograms = weightInKilograms;
    }

    public BigDecimal getBaseCostValue() { return baseCostValue; }
    public double getWeightInKilograms() { return weightInKilograms; }
    public String getProductName() { return productName; }

    @Override
    public void accept(PurchasableItemVisitor itemVisitor) {
        itemVisitor.visitProduct(this);
    }
}
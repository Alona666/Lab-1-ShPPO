package com.store.visitor;

import com.store.model.BundleItem;
import com.store.model.ProductItem;

public final class WeightVisitor implements PurchasableItemVisitor {
    private double totalWeightValue = 0.0;

    @Override
    public void visitProduct(ProductItem productItem) {
        totalWeightValue += productItem.getWeightInKilograms();
    }

    @Override
    public void visitBundle(BundleItem bundleItem) {
        // Упаковка набора добавляет 100 грамм к общему весу посылки
        totalWeightValue += 0.1;
    }

    public double getTotalWeightValue() {
        return totalWeightValue;
    }
}
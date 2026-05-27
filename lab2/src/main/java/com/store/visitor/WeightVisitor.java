package com.store.visitor;

import com.store.model.BundleItem;
import com.store.model.ProductItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype") // Бин-прототип
public final class WeightVisitor implements PurchasableItemVisitor {
    private double totalWeightValue = 0.0;

    @Override
    public void visitProduct(ProductItem productItem) {
        totalWeightValue += productItem.getWeightInKilograms();
    }

    @Override
    public void visitBundle(BundleItem bundleItem) {
        totalWeightValue += 0.1; // Вес коробки бандла
    }

    public double getTotalWeightValue() { return totalWeightValue; }
}
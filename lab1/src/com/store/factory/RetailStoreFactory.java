package com.store.factory;

import com.store.model.BundleItem;
import com.store.model.PurchasableItem;
import com.store.model.ProductItemBuilder;
import java.math.BigDecimal;
import java.util.List;

public final class RetailStoreFactory implements StoreFactory {
    @Override
    public PurchasableItem createGroceryBundle() {
        var apple = new ProductItemBuilder()
                .withProductId("R_FOOD_1")
                .withProductName("Яблоки (Розница)")
                .withBaseCostValue(new BigDecimal("150.00"))
                .withWeightInKilograms(1.0)
                .build();
        return new BundleItem("RETAIL_FOOD_BUNDLE", List.of(apple));
    }

    @Override
    public PurchasableItem createElectronicsBundle() {
        var mouse = new ProductItemBuilder()
                .withProductId("R_ELEC_1")
                .withProductName("Мышь беспроводная")
                .withBaseCostValue(new BigDecimal("1200.00"))
                .withWeightInKilograms(0.2)
                .build();
        return new BundleItem("RETAIL_ELEC_BUNDLE", List.of(mouse));
    }
}
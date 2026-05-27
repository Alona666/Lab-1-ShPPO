package com.store.factory;

import com.store.model.BundleItem;
import com.store.model.PurchasableItem;
import com.store.model.ProductItemBuilder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;

@Component("wholesaleFactory") // Тоже Бин-Синглтон
public final class WholesaleStoreFactory implements StoreFactory {
    @Override
    public PurchasableItem createGroceryBundle() {
        var appleBox = new ProductItemBuilder()
                .withProductId("W_FOOD_1")
                .withProductName("Коробка яблок (Опт)")
                .withBaseCostValue(new BigDecimal("1000.00"))
                .withWeightInKilograms(10.0)
                .build();
        return new BundleItem("WHOLESALE_FOOD_BUNDLE", List.of(appleBox));
    }

    @Override
    public PurchasableItem createElectronicsBundle() {
        var mouseBox = new ProductItemBuilder()
                .withProductId("W_ELEC_1")
                .withProductName("Партия мышей (10 шт)")
                .withBaseCostValue(new BigDecimal("8000.00"))
                .withWeightInKilograms(2.0)
                .build();
        return new BundleItem("WHOLESALE_ELEC_BUNDLE", List.of(mouseBox));
    }
}
package com.store.model;

import com.store.exception.PurchaseProcessingException;
import java.math.BigDecimal;

public final class ProductItemBuilder {
    private String productId;
    private String productName;
    private BigDecimal baseCostValue;
    private double weightInKilograms;

    public ProductItemBuilder withProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public ProductItemBuilder withProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public ProductItemBuilder withBaseCostValue(BigDecimal baseCostValue) {
        this.baseCostValue = baseCostValue;
        return this;
    }

    public ProductItemBuilder withWeightInKilograms(double weightInKilograms) {
        this.weightInKilograms = weightInKilograms;
        return this;
    }

    public ProductItem build() {

        if (productId == null || productId.isBlank()) {
            throw new PurchaseProcessingException("ID продукта не может быть пустым.");
        }
        if (baseCostValue == null || baseCostValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new PurchaseProcessingException("Стоимость товара '" + productName + "' не может быть отрицательной.");
        }
        if (weightInKilograms <= 0) {
            throw new PurchaseProcessingException("Вес товара '" + productName + "' должен быть больше нуля.");
        }
        return new ProductItem(productId, productName, baseCostValue, weightInKilograms);
    }
}
package com.store.visitor;

import com.store.model.BundleItem;
import com.store.model.ProductItem;
import com.store.strategy.PricingStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@Scope("prototype") // При каждом запросе создается НОВЫЙ чистый калькулятор стоимости
public final class CostVisitor implements PurchasableItemVisitor {
    private final PricingStrategy pricingStrategy;
    private BigDecimal totalCostValue = BigDecimal.ZERO;

    // Вручную передаем стратегию при создании через фабричный метод/конструктор в сервисе
    public CostVisitor(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    @Override
    public void visitProduct(ProductItem productItem) {
        BigDecimal finalCost = pricingStrategy.calculateFinalCost(productItem.getBaseCostValue());
        totalCostValue = totalCostValue.add(finalCost);
    }

    @Override
    public void visitBundle(BundleItem bundleItem) {}

    public BigDecimal getTotalCostValue() { return totalCostValue; }
}
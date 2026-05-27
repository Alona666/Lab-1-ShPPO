package com.store.visitor;

import com.store.model.BundleItem;
import com.store.model.ProductItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@Scope("prototype") // Бин-прототип
public final class TaxVisitor implements PurchasableItemVisitor {
    private static final BigDecimal TAX_RATE = new BigDecimal("0.20");
    private BigDecimal totalTax = BigDecimal.ZERO;

    @Override
    public void visitProduct(ProductItem productItem) {
        BigDecimal tax = productItem.getBaseCostValue().multiply(TAX_RATE);
        totalTax = totalTax.add(tax);
    }

    @Override
    public void visitBundle(BundleItem bundleItem) {}

    public BigDecimal getTotalTax() { return totalTax; }
}
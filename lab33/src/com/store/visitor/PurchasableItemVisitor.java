package com.store.visitor;

import com.store.model.BundleItem;
import com.store.model.ProductItem;

public interface PurchasableItemVisitor {
    void visitProduct(ProductItem productItem);
    void visitBundle(BundleItem bundleItem);
}
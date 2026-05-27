package com.store.model;

import com.store.visitor.PurchasableItemVisitor;

public interface PurchasableItem {
    void accept(PurchasableItemVisitor itemVisitor);
}
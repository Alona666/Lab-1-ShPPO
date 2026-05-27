package com.store.model;

import com.store.exception.PurchaseProcessingException;
import com.store.visitor.PurchasableItemVisitor;
import java.util.List;

public final class BundleItem implements PurchasableItem {
    private final String bundleId;
    private final List<PurchasableItem> purchasableItemList;

    public BundleItem(String bundleId, List<PurchasableItem> inputItemList) {
        if (inputItemList == null || inputItemList.isEmpty()) {
            throw new PurchaseProcessingException("Нельзя создать пустой набор товаров: " + bundleId);
        }
        this.bundleId = bundleId;
        this.purchasableItemList = List.copyOf(inputItemList);
    }

    @Override
    public void accept(PurchasableItemVisitor itemVisitor) {
        itemVisitor.visitBundle(this);
        for (PurchasableItem item : purchasableItemList) {
            item.accept(itemVisitor);
        }
    }
    public String getBundleId() {
        return this.bundleId; // или return this.id; (в зависимости от того, как названо поле в ЛР1)
    }
}
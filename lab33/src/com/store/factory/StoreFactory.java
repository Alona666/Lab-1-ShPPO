package com.store.factory;

import com.store.model.PurchasableItem;

public interface StoreFactory {
    PurchasableItem createGroceryBundle();
    PurchasableItem createElectronicsBundle();
}
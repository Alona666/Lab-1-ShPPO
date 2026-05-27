package com.store.concurrency;

import com.store.factory.StoreFactory;
import com.store.model.BundleItem;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Shopper implements Runnable {
    private final String shopperName;
    private final BlockingQueue<BundleItem> checkoutQueue;
    private final StoreFactory storeFactory;
    private final int itemsToBuy;

    public Shopper(String shopperName, BlockingQueue<BundleItem> checkoutQueue, StoreFactory storeFactory, int itemsToBuy) {
        this.shopperName = shopperName;
        this.checkoutQueue = checkoutQueue;
        this.storeFactory = storeFactory;
        this.itemsToBuy = itemsToBuy;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= itemsToBuy; i++) {
                // Имитируем сборку товаров покупателем
                Thread.sleep(ThreadLocalRandom.current().nextInt(300, 700));

                BundleItem cart = (BundleItem) (ThreadLocalRandom.current().nextBoolean()
                        ? storeFactory.createGroceryBundle()
                        : storeFactory.createElectronicsBundle());

                System.out.printf("[%s] собрал корзину и встал в очередь (%d/%d)%n", shopperName, i, itemsToBuy);

                // Поток засыпает сам, если очередь (лента) заполнена
                checkoutQueue.put(cart);
            }
            System.out.printf("[%s] закончил покупки и покинул торговый зал.%n", shopperName);
        } catch (InterruptedException e) {
            System.err.printf("[%s] был прерван.%n", shopperName);
            Thread.currentThread().interrupt();
        }
    }
}
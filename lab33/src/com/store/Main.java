package com.store;

import com.store.concurrency.Cashier;
import com.store.concurrency.Shopper;
import com.store.factory.RetailStoreFactory;
import com.store.factory.StoreFactory;
import com.store.model.BundleItem;
import com.store.model.ProductItemBuilder;
import com.store.strategy.RegularPricingStrategy;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== МНОГОПОТОЧНЫЙ МАГАЗИН ОТКРЫТ ===");

        // Очередь запросов (Потокобезопасная лента кассы на 5 мест)
        BlockingQueue<BundleItem> checkoutQueue = new ArrayBlockingQueue<>(5);

        StoreFactory factory = new RetailStoreFactory();
        RegularPricingStrategy strategy = new RegularPricingStrategy();

        int shoppersCount = 4;
        int cashiersCount = 2;

        // Использование простых массивов Thread вместо высокоуровневого ExecutorService
        Thread[] shopperThreads = new Thread[shoppersCount];
        Thread[] cashierThreads = new Thread[cashiersCount];

        // 1. Инициализируем и запускаем исполнителей (Кассиров)
        for (int i = 0; i < cashiersCount; i++) {
            cashierThreads[i] = new Thread(new Cashier(i + 1, checkoutQueue, strategy));
            cashierThreads[i].start();
        }

        // 2. Инициализируем и запускаем генераторы (Покупателей)
        for (int i = 0; i < shoppersCount; i++) {
            shopperThreads[i] = new Thread(new Shopper("Покупатель-" + (i + 1), checkoutQueue, factory, 2));
            shopperThreads[i].start();
        }

        try {
            // 3. Ждем реального завершения работы ВСЕХ генераторов (Покупателей)
            // Метод join() заставит main-поток ждать, пока покупатели не отработают свои циклы
            for (Thread shopper : shopperThreads) {
                shopper.join();
            }
            System.out.println("\n[SYSTEM] Все генераторы отключены. Покупатели покинули зал.");

// 4. ГАРАНТИРОВАННЫЙ СИГНАЛ: Отправляем маркеры завершения (Poison Pills)
            // Создаем один фейковый товар с ВАЛИДНЫМИ данными, чтобы пройти строгие проверки ЛР1
            var dummyItem = new ProductItemBuilder()
                    .withProductId("SERVICE_PILL")
                    .withProductName("PoisonPillComponent")
                    .withBaseCostValue(BigDecimal.ONE) // На всякий случай цена > 0
                    .withWeightInKilograms(0.001)      // ИСПРАВЛЕНИЕ: Вес строго больше нуля
                    .build();

            // Кладем в очередь ровно столько пилюль, сколько кассиров работают в системе
            for (int i = 0; i < cashiersCount; i++) {
                checkoutQueue.put(new BundleItem("POISON_PILL", List.of(dummyItem)));
            }
            // 5. Ожидаем, пока исполнители (Кассиры) вычитают оставшиеся заказы, дойдут до пилюль и остановятся
            for (Thread cashier : cashierThreads) {
                cashier.join();
            }

        } catch (InterruptedException e) {
            System.err.println("[SYSTEM] Главный поток оркестратора был прерван.");
            Thread.currentThread().interrupt();
        } finally {
            
            System.out.println("\n=== [SYSTEM] Работа магазина завершена. Все потоки остановлены. Очередь чиста ===");
        }
    }
}
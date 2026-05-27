package com.store.concurrency;

import com.store.model.BundleItem;
import com.store.strategy.PricingStrategy;
import com.store.visitor.CostVisitor;
import com.store.visitor.TaxVisitor;
import com.store.visitor.WeightVisitor;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Cashier implements Runnable {
    private final int cashierId;
    private final BlockingQueue<BundleItem> checkoutQueue;
    private final PricingStrategy pricingStrategy;

    public Cashier(int cashierId, BlockingQueue<BundleItem> checkoutQueue, PricingStrategy pricingStrategy) {
        this.cashierId = cashierId;
        this.checkoutQueue = checkoutQueue;
        this.pricingStrategy = pricingStrategy;
    }

    @Override
    public void run() {
        System.out.println("[Касса " + cashierId + "] открыта и ожидает заказы.");
        try {
            while (true) {
                // Извлекаем корзину. Если очередь пуста — кассир спит без холостого хода процессора
                BundleItem cart = checkoutQueue.take();

                // НАДЕЖНЫЙ МЕХАНИЗМ СИГНАЛА: Проверяем, не является ли заказ "ядовитой пилюлей"
                if ("POISON_PILL".equals(cart.getBundleId())) {
                    System.out.println("[Касса " + cashierId + "] Получен явный сигнал закрытия. Завершаю работу.");
                    break; // Элегантный и безопасный выход из цикла, когда очередь пуста
                }

                System.out.println("[Касса " + cashierId + "] обслуживает корзину: " + cart.getBundleId());
                Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1000));

                // Локальные посетители гарантируют потокобезопасность (Thread-Safety) сумм
                CostVisitor costVisitor = new CostVisitor(pricingStrategy);
                WeightVisitor weightVisitor = new WeightVisitor();
                TaxVisitor taxVisitor = new TaxVisitor();

                cart.accept(costVisitor);
                cart.accept(weightVisitor);
                cart.accept(taxVisitor);

                System.out.printf("[Касса %d] Чек пробит -> Сумма: %.2f руб | Вес: %.2f кг | НДС: %.2f руб%n",
                        cashierId, costVisitor.getTotalCostValue(), weightVisitor.getTotalWeightValue(), taxVisitor.getTotalTax());
            }
        } catch (InterruptedException e) {
            System.out.println("[Касса " + cashierId + "] Аварийно прервана.");
            Thread.currentThread().interrupt();
        }
    }
}
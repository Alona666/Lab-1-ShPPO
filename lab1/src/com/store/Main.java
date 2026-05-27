package com.store;

import com.store.exception.PurchaseProcessingException;
import com.store.factory.RetailStoreFactory;
import com.store.factory.WholesaleStoreFactory;
import com.store.factory.StoreFactory;
import com.store.model.BundleItem;
import com.store.model.PurchasableItem;
import com.store.strategy.HolidayPricingStrategy;
import com.store.strategy.RegularPricingStrategy;
import com.store.visitor.CostVisitor;
import com.store.visitor.TaxVisitor;
import com.store.visitor.WeightVisitor;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Демонстрация работы Розничной фабрики
        System.out.println("=== СИМУЛЯЦИЯ РОЗНИЧНОГО МАГАЗИНА ===");
        runSimulation(new RetailStoreFactory());

        System.out.println("\n-------------------------------------------\n");

        // 2. Демонстрация работы Оптовой фабрики (теперь она ОЖИЛА и используется!)
        System.out.println("=== СИМУЛЯЦИЯ ОПТОВОГО МАГАЗИНА ===");
        runSimulation(new WholesaleStoreFactory());

        System.out.println("\n-------------------------------------------\n");

        // 3. Оживляем PurchaseProcessingException (намеренно передаем некорректные данные)
        System.out.println("=== ТЕСТИРОВАНИЕ ОШИБОК И ВАЛИДАЦИИ ===");
        try {
            System.out.println("Попытка создать товар с отрицательной стоимостью...");
            new com.store.model.ProductItemBuilder()
                    .withProductId("ERR_404")
                    .withProductName("Бракованный товар")
                    .withBaseCostValue(new java.math.BigDecimal("-150.00")) // <--- Вызовет ошибку в Builder
                    .withWeightInKilograms(0.5)
                    .build();
        } catch (PurchaseProcessingException e) {
            // Перехватываем наше кастомное исключение
            System.err.println("Успешно перехвачена бизнес-ошибка: " + e.getMessage());
        }
    }

    /**
     * Универсальный метод симуляции, демонстрирующий мощь интерфейсов.
     * Сюда можно передать любую фабрику, и код отработает корректно.
     */
    private static void runSimulation(StoreFactory factory) {
        // Порождающие паттерны (Abstract Factory + Builder) создают структуру Компоновщика (Composite)
        PurchasableItem groceries = factory.createGroceryBundle();
        PurchasableItem electronics = factory.createElectronicsBundle();

        // Главная корзина покупателя
        BundleItem shoppingCart = new BundleItem("MAIN_CART", List.of(groceries, electronics));

        // Используем RegularPricingStrategy (Обычная цена)
        CostVisitor regularCostVisitor = new CostVisitor(new RegularPricingStrategy());
        shoppingCart.accept(regularCostVisitor);

        // Используем HolidayPricingStrategy (Праздничная цена со скидкой)
        CostVisitor holidayCostVisitor = new CostVisitor(new HolidayPricingStrategy());
        shoppingCart.accept(holidayCostVisitor);

        // Собираем вес через Посетителя
        WeightVisitor weightVisitor = new WeightVisitor();
        shoppingCart.accept(weightVisitor);

        // Рассчитываем новое свойство (НДС) через Посетителя без изменения классов модели
        TaxVisitor taxVisitor = new TaxVisitor();
        shoppingCart.accept(taxVisitor);

        // Вывод результатов работы всех паттернов в консоль
        System.out.println("Базовая стоимость корзины: " + regularCostVisitor.getTotalCostValue() + " руб.");
        System.out.println("Стоимость со скидкой (Holiday Strategy): " + holidayCostVisitor.getTotalCostValue() + " руб.");
        System.out.printf("Общий вес всех товаров в корзине: %.2f кг.%n", weightVisitor.getTotalWeightValue());
        System.out.println("Сумма НДС (20%% от базовой стоимости): " + taxVisitor.getTotalTax() + " руб.");
    }
}
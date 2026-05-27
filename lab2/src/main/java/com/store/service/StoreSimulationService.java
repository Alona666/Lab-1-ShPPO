package com.store.service;

import com.store.factory.StoreFactory;
import com.store.model.BundleItem;
import com.store.model.PurchasableItem;
import com.store.strategy.PricingStrategy;
import com.store.visitor.CostVisitor;
import com.store.visitor.TaxVisitor;
import com.store.visitor.WeightVisitor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // Делает класс Сервисом (Синглтон)
public class StoreSimulationService {

    // Внедряем Синглтоны через интерфейсы с использованием @Qualifier
    private final StoreFactory retailFactory;
    private final StoreFactory wholesaleFactory;
    private final PricingStrategy regularStrategy;
    private final PricingStrategy holidayStrategy;

    // Внедряем ObjectProvider для безопасного создания Прототипов на лету
    private final ObjectProvider<WeightVisitor> weightVisitorProvider;
    private final ObjectProvider<TaxVisitor> taxVisitorProvider;

    @Autowired // Рекомендуемый способ DI — через конструктор
    public StoreSimulationService(
            @Qualifier("retailFactory") StoreFactory retailFactory,
            @Qualifier("wholesaleFactory") StoreFactory wholesaleFactory,
            @Qualifier("regularStrategy") PricingStrategy regularStrategy,
            @Qualifier("holidayStrategy") PricingStrategy holidayStrategy,
            ObjectProvider<WeightVisitor> weightVisitorProvider,
            ObjectProvider<TaxVisitor> taxVisitorProvider) {

        this.retailFactory = retailFactory;
        this.wholesaleFactory = wholesaleFactory;
        this.regularStrategy = regularStrategy;
        this.holidayStrategy = holidayStrategy;
        this.weightVisitorProvider = weightVisitorProvider;
        this.taxVisitorProvider = taxVisitorProvider;
    }

    public void runSimulation() {
        System.out.println("--- Шаг 1: Работаем с Розничной Фабрикой ---");
        processCart(retailFactory);

        System.out.println("\n--- Шаг 2: Работаем с Оптовой Фабрикой ---");
        processCart(wholesaleFactory);
    }

    private void processCart(StoreFactory factory) {
        PurchasableItem groceries = factory.createGroceryBundle();
        PurchasableItem electronics = factory.createElectronicsBundle();
        BundleItem cart = new BundleItem("CART_ID", List.of(groceries, electronics));

        // Создаем посетителей стоимости вручную, передавая им внедренные стратегии
        CostVisitor regularCostVisitor = new CostVisitor(regularStrategy);
        CostVisitor holidayCostVisitor = new CostVisitor(holidayStrategy);

        // Получаем новые чистые экземпляры прототипов через ObjectProvider
        WeightVisitor weightVisitor = weightVisitorProvider.getObject();
        TaxVisitor taxVisitor = taxVisitorProvider.getObject();

        // Применяем паттерн Посетитель
        cart.accept(regularCostVisitor);
        cart.accept(holidayCostVisitor);
        cart.accept(weightVisitor);
        cart.accept(taxVisitor);

        // Вывод
        System.out.println("  Базовая стоимость: " + regularCostVisitor.getTotalCostValue() + " руб.");
        System.out.println("  Цена со скидкой:   " + holidayCostVisitor.getTotalCostValue() + " руб.");
        System.out.printf("  Общий вес:         %.2f кг.%n", weightVisitor.getTotalWeightValue());
        System.out.println("  Сумма НДС (20%%):   " + taxVisitor.getTotalTax() + " руб.");
    }
}
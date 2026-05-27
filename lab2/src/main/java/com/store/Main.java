package com.store;

import com.store.config.AppConfig;
import com.store.exception.PurchaseProcessingException;
import com.store.service.StoreSimulationService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        // Контекст инициализируется, используется и закрывается строго в одном месте!
        System.out.println("[СИСТЕМА] Запуск и инициализация Spring-контекста...");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        try {
            // Достаем из IoC-контейнера наш управляющий сервис
            StoreSimulationService simulationService = context.getBean(StoreSimulationService.class);

            // Запускаем симуляцию
            simulationService.runSimulation();

            // Демонстрация обработки исключений (валидация билдера)
            System.out.println("\n--- Шаг 3: Тестирование валидации и бизнес-исключений ---");
            new com.store.model.ProductItemBuilder()
                    .withProductId("ERR_ID")
                    .withProductName("Сломанный продукт")
                    .withBaseCostValue(new java.math.BigDecimal("-50.00")) // Отрицательная цена
                    .build();

        } catch (PurchaseProcessingException e) {
            System.err.println("[ОШИБКА БИЗНЕС-ЛОГИКИ]: " + e.getMessage());
        } finally {
            // Закрываем контекст, уничтожая бины
            context.close();
            System.out.println("\n[СИСТЕМА] Контекст Spring успешно закрыт.");
        }
    }
}
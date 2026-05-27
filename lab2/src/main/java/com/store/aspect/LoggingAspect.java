package com.store.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component // Делаем аспект Spring-бином
public class LoggingAspect {

    // Срез (Pointcut): перехватывает выполнение любых методов в пакете factory
    @Before("execution(* com.store.factory.*.*(..))")
    public void logFactoryMethods(JoinPoint joinPoint) {
        System.out.println("[AOP LOG] Фабрика выполняет метод: -> " + joinPoint.getSignature().getName());
    }

    // Срез (Pointcut): перехватывает запуск симуляции в сервисе
    @Before("execution(* com.store.service.StoreSimulationService.runSimulation(..))")
    public void logSimulationStart(JoinPoint joinPoint) {
        System.out.println("\n[AOP LOG] === Сквозной функционал: Старт бизнес-логики сервиса ===");
    }
}
package com.store.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "com.store") // Указываем Spring сканировать наш проект в поисках аннотаций
@EnableAspectJAutoProxy // Включаем поддержку АОП (проксирование для аспектов)
public class AppConfig {
}
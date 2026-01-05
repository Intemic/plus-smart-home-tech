package ru.yandex.practicum.commerce.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(basePackages = "com.yourcompany.common.advice")
@SpringBootApplication
public class ShoppingStoreApp {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingStoreApp.class, args);
    }
}

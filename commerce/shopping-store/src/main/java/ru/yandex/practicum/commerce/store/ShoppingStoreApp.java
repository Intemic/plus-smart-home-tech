package ru.yandex.practicum.commerce.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.ControllerAdvice;

@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.commerce.store",
        "ru.yandex.practicum.commerce.interaction.api.logging"
})
@EnableAspectJAutoProxy
public class ShoppingStoreApp {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingStoreApp.class, args);
    }
}

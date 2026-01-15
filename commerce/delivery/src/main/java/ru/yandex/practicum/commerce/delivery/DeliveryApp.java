package ru.yandex.practicum.commerce.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.commerce.delivery",
        "ru.yandex.practicum.commerce.interaction.api.logging"
})
@EnableFeignClients(basePackages = "ru.yandex.practicum.commerce.interaction.api.client")
@EnableAspectJAutoProxy
public class DeliveryApp {
    public static void main(String[] args) {
        SpringApplication.run(DeliveryApp.class, args);
    }
}

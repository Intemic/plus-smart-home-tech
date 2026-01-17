package ru.yandex.practicum.commerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.commerce.order",
        "ru.yandex.practicum.commerce.interaction.api"
})
@EnableFeignClients(basePackages = "ru.yandex.practicum.commerce.interaction.api.client")
@EnableAspectJAutoProxy
public class OrderApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class, args);
    }
}

package ru.yandex.practicum.commerce.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.commerce.warehouse",
        "ru.yandex.practicum.commerce.interaction.api"
})
@EnableAspectJAutoProxy
public class WareHouseApp {
    public static void main(String[] args) {
        SpringApplication.run(WareHouseApp.class, args);
    }
}

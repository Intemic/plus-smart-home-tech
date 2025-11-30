package ru.yandex.practicum.telemetry.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.config.KafaClientImp;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CollectorApp {
    public static void main(String[] args) {
        SpringApplication.run(CollectorApp.class, args);
    }
}

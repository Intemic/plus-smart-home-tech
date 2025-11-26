package ru.yandex.practicum.telemetry.collector.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.config.KafaClientImp;
import ru.yandex.practicum.telemetry.collector.config.KafkaClient;

@Import({CollectorConfig.class,  KafaClientImp.class})
@SpringBootApplication
//@ConfigurationPropertiesScan
public class CollectorApp {
    public static void main(String[] args) {
        SpringApplication.run(CollectorApp.class, args);
    }
}

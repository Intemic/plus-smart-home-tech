package ru.yandex.practicum.telemetry.collector.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.config.KafaClientImp;

@Import({CollectorConfig.class,  KafaClientImp.class})
//@ConfigurationPropertiesScan
@SpringBootApplication
public class CollectorGrpcApp {
    public static void main(String[] args) {
        SpringApplication.run(CollectorGrpcApp.class, args);
    }
}

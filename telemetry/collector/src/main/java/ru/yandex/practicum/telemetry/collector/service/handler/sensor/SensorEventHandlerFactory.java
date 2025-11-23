package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.collector.config.CollectorConfig;
import ru.yandex.practicum.telemetry.collector.config.KafkaClient;
import ru.yandex.practicum.telemetry.collector.dto.sensor.*;
import org.apache.kafka.clients.producer.Producer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorEventHandlerFactory {
    private final KafkaClient client;
    private final CollectorConfig config;

    private static final List<Class<? extends SensorEventHandler>> sensorClasses =
            List.of(ClimateEventHandler.class, LightEventHandler.class, MotionEventHandler.class,
                    SwitchEventHandler.class, TemperatureEventHandler.class);

    private SensorEventHandler create(Class<? extends SensorEventHandler> handlerClass) {
        try {
            Class<?>[] classParam = new Class[]{Producer.class, CollectorConfig.class};
            Constructor<?> classConstructor = handlerClass.getDeclaredConstructor(classParam);
            classConstructor.setAccessible(true);
            Object[] args = new Object[]{client.getProducer(), config};
            return (SensorEventHandler) classConstructor.newInstance(client.getProducer(), config);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            log.error("Ошибка создания обработчика класса %s - ".formatted(handlerClass));
            throw new RuntimeException("Ошибка создания обработчика класса %s - \".formatted(handlerClass)");
        }
    }

    public List<SensorEventHandler> getHandlers() {
        return sensorClasses.stream()
                .map(handlerClass -> create(handlerClass))
                .toList();
    }
}

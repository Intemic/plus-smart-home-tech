package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.collector.config.KafkaClient;
import ru.yandex.practicum.telemetry.collector.service.handler.sensor.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventHandlerFactory {
    private final KafkaClient client;

    private static final List<Class<? extends HubEventHandler>> hubClasses =
            List.of(DeviceAddedEventHandler.class, DeviceRemovedEventHandler.class,
                    ScenarioAddedEventHandler.class, ScenarioRemovedEventHandler.class);

    private HubEventHandler create(Class<? extends HubEventHandler> handlerClass) {
        try {
            Constructor<?> classConstructor = handlerClass.getDeclaredConstructor(Producer.class);
            classConstructor.setAccessible(true);
            return (HubEventHandler) classConstructor.newInstance(client.getProducer());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            log.error("Ошибка создания обработчика класса %s - ".formatted(handlerClass));
            throw new RuntimeException("Ошибка создания обработчика класса %s - \".formatted(handlerClass)");
        }
    }

    public List<HubEventHandler> getHandlers() {
        return hubClasses.stream()
                .map(handlerClass -> create(handlerClass))
                .toList();
    }
}

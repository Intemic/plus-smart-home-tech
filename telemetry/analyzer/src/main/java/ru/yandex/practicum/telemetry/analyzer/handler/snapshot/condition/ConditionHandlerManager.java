package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition;


import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ConditionHandlerManager {
    Map<Class<? extends SpecificRecordBase>,
            ConditionHandler<Class<? extends SpecificRecordBase>, ? extends SpecificRecordBase>> handlerMap;

    public ConditionHandlerManager(
            @Autowired Set<ConditionHandler<Class<? extends SpecificRecordBase>, ? extends SpecificRecordBase>> handlers) {
       handlerMap = handlers.stream()
               .collect(Collectors.toMap(ConditionHandler::getType, Function.identity()));
    }

    public ConditionHandler getHandler(Class<? extends SpecificRecordBase> classType) {
        return handlerMap.get(classType);
    }
}

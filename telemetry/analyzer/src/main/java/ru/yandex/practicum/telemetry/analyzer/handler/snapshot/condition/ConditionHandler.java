package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition;

import org.apache.avro.specific.SpecificRecordBase;

//public interface ConditionHandler<C, O extends SpecificRecordBase>  {
public interface ConditionHandler<C, O>  {
    Class<O> getType();

    boolean check(C condition, O object);
}

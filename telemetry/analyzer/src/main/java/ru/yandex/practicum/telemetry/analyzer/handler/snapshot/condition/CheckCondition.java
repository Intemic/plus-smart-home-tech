package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.condition;

@FunctionalInterface
public interface CheckCondition<C, V>  {
    boolean check(C condition, V value);
}

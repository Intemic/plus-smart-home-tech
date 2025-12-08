package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.operation;

@FunctionalInterface
public interface CompareOperation<R, V> {
    boolean compare(R reference , V value);
}

package ru.yandex.practicum.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.yandex.practicum.telemetry.analyzer.model.Conditon;


public interface ConditionRepository extends JpaRepository<Conditon, Long> {
}

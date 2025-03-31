package ru.yandex.practicum.sht.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.sht.telemetry.analyzer.model.Condition;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
}

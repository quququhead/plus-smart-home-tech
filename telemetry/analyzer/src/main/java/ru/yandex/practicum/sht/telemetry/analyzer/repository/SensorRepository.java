package ru.yandex.practicum.sht.telemetry.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.sht.telemetry.analyzer.model.Sensor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, String> {
    boolean existsByIdInAndHubId(Collection<String> ids, String hubId);

    Optional<Sensor> findByIdAndHubId(String id, String hubId);

    List<Sensor> findAllByIdInAndHubId(Collection<String> ids, String hubId);
}

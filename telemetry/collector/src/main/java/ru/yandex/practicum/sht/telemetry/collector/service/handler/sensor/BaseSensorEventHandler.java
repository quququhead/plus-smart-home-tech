package ru.yandex.practicum.sht.telemetry.collector.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.sht.telemetry.collector.service.KafkaEventProducer;

import java.time.Instant;

import static ru.yandex.practicum.sht.telemetry.collector.configuration.KafkaConfig.TopicType.SENSORS_EVENTS;

@RequiredArgsConstructor
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {

    private final KafkaEventProducer producer;

    protected abstract T mapToAvro(SensorEventProto event);

    @Override
    public void handle(SensorEventProto event) {
        T payload = mapToAvro(event);
        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();

        producer.send(eventAvro, event.getHubId(), Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()), SENSORS_EVENTS);
    }
}

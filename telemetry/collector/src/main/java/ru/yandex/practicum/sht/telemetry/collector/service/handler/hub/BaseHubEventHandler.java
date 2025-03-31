package ru.yandex.practicum.sht.telemetry.collector.service.handler.hub;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.sht.telemetry.collector.service.KafkaEventProducer;

import java.time.Instant;

import static ru.yandex.practicum.sht.telemetry.collector.configuration.KafkaConfig.TopicType.HUBS_EVENTS;

@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {

    private final KafkaEventProducer producer;

    protected abstract T mapToAvro(HubEventProto event);

    @Override
    public void handle(HubEventProto event) {
        T payload = mapToAvro(event);
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();

        producer.send(eventAvro, event.getHubId(), Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()), HUBS_EVENTS);
    }
}

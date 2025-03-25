package ru.yandex.practicum.sht.telemetry.analyzer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.sht.telemetry.analyzer.configuration.KafkaConfig;
import ru.yandex.practicum.sht.telemetry.analyzer.configuration.KafkaConfig.TopicType;
import ru.yandex.practicum.sht.telemetry.analyzer.service.handler.snapshot.SensorsSnapshotHandler;

import java.time.Duration;
import java.util.*;

import static ru.yandex.practicum.sht.telemetry.analyzer.configuration.KafkaConfig.TopicType.SNAPSHOTS_EVENTS;

@Component
public class SnapshotProcessor {

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration CLOSE_TIMEOUT = Duration.ofMillis(10);

    private final KafkaConsumer<String, SensorsSnapshotAvro> kafkaSnapshotConsumer;
    private final EnumMap<TopicType, String> snapshotConsumerTopics;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets;
    private final SensorsSnapshotHandler sensorsSnapshotHandler;

    public SnapshotProcessor(KafkaConsumer<String, SensorsSnapshotAvro> kafkaSnapshotConsumer, KafkaConfig kafka, SensorsSnapshotHandler sensorsSnapshotHandler) {
        this.kafkaSnapshotConsumer = kafkaSnapshotConsumer;
        this.snapshotConsumerTopics = kafka.getSnapshotConsumer().getTopics();
        this.currentOffsets = new HashMap<>();
        this.sensorsSnapshotHandler = sensorsSnapshotHandler;
    }

    public void start() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(kafkaSnapshotConsumer::wakeup));
            kafkaSnapshotConsumer.subscribe(List.of(snapshotConsumerTopics.get(SNAPSHOTS_EVENTS)));
            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = kafkaSnapshotConsumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    sensorsSnapshotHandler.handle(record.value());
                    manageOffsets(record);
                }
                kafkaSnapshotConsumer.commitAsync();
            }
        } catch (Exception ignored) {
        } finally {
            try {
                kafkaSnapshotConsumer.commitSync(currentOffsets);
            } finally {
                kafkaSnapshotConsumer.close(CLOSE_TIMEOUT);
            }
        }
    }

    private void manageOffsets(ConsumerRecord<String, SensorsSnapshotAvro> record) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );
    }
}

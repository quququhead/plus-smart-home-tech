package ru.yandex.practicum.sht.telemetry.aggregator.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.sht.telemetry.aggregator.configuration.KafkaConfig;
import ru.yandex.practicum.sht.telemetry.aggregator.configuration.KafkaConfig.TopicType;

import java.time.Duration;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ru.yandex.practicum.sht.telemetry.aggregator.configuration.KafkaConfig.TopicType.SNAPSHOTS_EVENTS;

@Component
public class AggregationStarter {

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration CLOSE_TIMEOUT = Duration.ofMillis(10);

    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SpecificRecordBase> producer;
    private final EnumMap<TopicType, String> consumerTopics;
    private final EnumMap<TopicType, String> producerTopics;
    private final AggregatorService aggregatorService;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public AggregationStarter(KafkaConfig kafka, KafkaConsumer<String, SensorEventAvro> kafkaConsumer, KafkaProducer<String, SpecificRecordBase> kafkaProducer, AggregatorService aggregatorService) {
        this.consumer = kafkaConsumer;
        this.producer = kafkaProducer;
        this.consumerTopics = kafka.getConsumer().getTopics();
        this.producerTopics = kafka.getConsumer().getTopics();
        this.aggregatorService = aggregatorService;
    }

    public void start() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(consumerTopics.values());
            while (true) {
                ConsumerRecords<String, SensorEventAvro> consumerRecords = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                for (ConsumerRecord<String, SensorEventAvro> consumerRecord : consumerRecords) {
                    Optional<SensorsSnapshotAvro> snapshot = aggregatorService.updateState(consumerRecord.value());
                    if (snapshot.isPresent()) {
                        ProducerRecord<String, SpecificRecordBase> producerRecord = new ProducerRecord<>(
                                producerTopics.get(SNAPSHOTS_EVENTS),
                                null,
                                snapshot.get().getTimestamp().toEpochMilli(),
                                snapshot.get().getHubId(),
                                snapshot.get()
                        );
                        producer.send(producerRecord);
                    }
                    manageOffsets(consumerRecord);
                }
                consumer.commitAsync();
            }
        } catch (Exception ignored) {
        } finally {
            try {
                consumer.commitSync(currentOffsets);
                producer.flush();
            } finally {
                consumer.close(CLOSE_TIMEOUT);
                producer.close(CLOSE_TIMEOUT);
            }
        }
    }

    private void manageOffsets(ConsumerRecord<String, SensorEventAvro> record) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );
    }
}

package ru.yandex.practicum.sht.telemetry.analyzer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.sht.telemetry.analyzer.configuration.KafkaConfig;
import ru.yandex.practicum.sht.telemetry.analyzer.configuration.KafkaConfig.TopicType;
import ru.yandex.practicum.sht.telemetry.analyzer.service.handler.hub.HubEventHandler;

import static ru.yandex.practicum.sht.telemetry.analyzer.configuration.KafkaConfig.TopicType.HUBS_EVENTS;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class HubEventProcessor implements Runnable {

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration CLOSE_TIMEOUT = Duration.ofMillis(10);

    private final KafkaConsumer<String, HubEventAvro> kafkaHubConsumer;
    private final EnumMap<TopicType, String> hubConsumerTopics;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets;
    private final Map<String, HubEventHandler> hubEventHandlers;

    public HubEventProcessor(KafkaConsumer<String, HubEventAvro> kafkaHubConsumer, KafkaConfig kafka, Set<HubEventHandler> hubEventHandlers) {
        this.kafkaHubConsumer = kafkaHubConsumer;
        this.hubConsumerTopics = kafka.getHubConsumer().getTopics();
        this.currentOffsets = new HashMap<>();
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getType,
                        Function.identity()
                ));
    }

    @Override
    public void run() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(kafkaHubConsumer::wakeup));
            kafkaHubConsumer.subscribe(List.of(hubConsumerTopics.get(HUBS_EVENTS)));
            while (true) {
                ConsumerRecords<String, HubEventAvro> records = kafkaHubConsumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    HubEventHandler handler = hubEventHandlers.get(record.value().getPayload().getClass().getName());
                    if (handler != null) {
                        handler.handle(record.value());
                    }
                    manageOffsets(record);
                }
                kafkaHubConsumer.commitAsync();
            }
        } catch (Exception ignored) {
        } finally {
            try {
                kafkaHubConsumer.commitSync(currentOffsets);
            } finally {
                kafkaHubConsumer.close(CLOSE_TIMEOUT);
            }
        }
    }

    private void manageOffsets(ConsumerRecord<String, HubEventAvro> record) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );
    }
}

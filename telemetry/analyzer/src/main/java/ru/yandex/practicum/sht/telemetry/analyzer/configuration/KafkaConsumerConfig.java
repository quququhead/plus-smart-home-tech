package ru.yandex.practicum.sht.telemetry.analyzer.configuration;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public KafkaConsumer<String, HubEventAvro> kafkaHubConsumer(KafkaConfig kafkaConfig) {
        return new KafkaConsumer<>(kafkaConfig.getHubConsumer().getProperties());
    }

    @Bean
    public KafkaConsumer<String, SensorsSnapshotAvro> kafkaSnapshotConsumer(KafkaConfig kafkaConfig) {
        return new KafkaConsumer<>(kafkaConfig.getSnapshotConsumer().getProperties());
    }
}

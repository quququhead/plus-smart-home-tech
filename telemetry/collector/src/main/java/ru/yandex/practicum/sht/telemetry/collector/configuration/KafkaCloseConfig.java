package ru.yandex.practicum.sht.telemetry.collector.configuration;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class KafkaCloseConfig {
    private final KafkaProducer<String, SpecificRecordBase> kafkaProducer;

    @PreDestroy
    public void stop() {
        kafkaProducer.flush();
        kafkaProducer.close(Duration.ofMillis(10));
    }
}

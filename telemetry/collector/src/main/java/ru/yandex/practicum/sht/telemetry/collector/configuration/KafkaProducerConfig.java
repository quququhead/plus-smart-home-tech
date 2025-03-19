package ru.yandex.practicum.sht.telemetry.collector.configuration;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaProducer<String, SpecificRecordBase> kafkaProducer(KafkaConfig kafkaConfig) {
        return new KafkaProducer<>(kafkaConfig.getProducer().getProperties());
    }
}

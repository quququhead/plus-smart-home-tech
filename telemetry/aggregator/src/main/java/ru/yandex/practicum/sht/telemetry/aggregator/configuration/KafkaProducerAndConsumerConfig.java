package ru.yandex.practicum.sht.telemetry.aggregator.configuration;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Configuration
public class KafkaProducerAndConsumerConfig {

    @Bean
    public KafkaConsumer<String, SensorEventAvro> kafkaConsumer(KafkaConfig kafka) {
        return new KafkaConsumer<>(kafka.getConsumer().getProperties());
    }

    @Bean
    public KafkaProducer<String, SpecificRecordBase> kafkaProducer(KafkaConfig kafka) {
        return new KafkaProducer<>(kafka.getProducer().getProperties());
    }
}

package ru.yandex.practicum.sht.telemetry.collector.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.sht.telemetry.collector.configuration.KafkaConfig;
import ru.yandex.practicum.sht.telemetry.collector.configuration.KafkaConfig.TopicType;

import java.time.Instant;
import java.util.EnumMap;

@Component
public class KafkaEventProducer {

    private final KafkaProducer<String, SpecificRecordBase> producer;
    private final EnumMap<TopicType, String> topics;

    public KafkaEventProducer(KafkaConfig kafkaConfig, KafkaProducer<String, SpecificRecordBase> kafkaProducer) {
        this.topics = kafkaConfig.getProducer().getTopics();
        this.producer = kafkaProducer;
    }

    public void send(SpecificRecordBase event, String hubId, Instant timestamp, TopicType topicType) {
        String topic = topics.get(topicType);
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                topic,
                null,
                timestamp.toEpochMilli(),
                hubId,
                event
        );
        producer.send(record);
    }
}

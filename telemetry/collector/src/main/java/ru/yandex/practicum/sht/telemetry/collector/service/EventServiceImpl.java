package ru.yandex.practicum.sht.telemetry.collector.service;

import jakarta.annotation.PreDestroy;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.sht.telemetry.collector.mapper.HubEventMapper;
import ru.yandex.practicum.sht.telemetry.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.sht.telemetry.collector.model.*;

import java.time.Duration;
import java.util.Properties;

@Service
public class EventServiceImpl implements EventService {

    private final String sensorTopic;
    private final String hubTopic;
    private final Producer<Void, SpecificRecordBase> producer;

    public EventServiceImpl(@Value("${kafka.url}") String bootstrapServer, @Value("${kafka.sensor-topic}") String sensorTopic, @Value("${kafka.hub-topic}") String hubTopic) {
        this.sensorTopic = sensorTopic;
        this.hubTopic = hubTopic;
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.VoidSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "ru.yandex.practicum.sht.telemetry.collector.serialization.GeneralAvroSerializer");
        this.producer = new KafkaProducer<>(config);
    }

    @PreDestroy
    public void stop() {
        producer.flush();
        producer.close(Duration.ofMillis(10));
    }

    @Override
    public void collectSensorEvent(SensorEvent event) {
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(switch (event.getType()) {
                    case SensorEventType.CLIMATE_SENSOR_EVENT ->
                            SensorEventMapper.mapToClimateSensorAvro((ClimateSensorEvent) event);
                    case SensorEventType.LIGHT_SENSOR_EVENT ->
                            SensorEventMapper.mapToLightSensorAvro((LightSensorEvent) event);
                    case SensorEventType.MOTION_SENSOR_EVENT ->
                            SensorEventMapper.mapToMotionSensorAvro((MotionSensorEvent) event);
                    case SensorEventType.SWITCH_SENSOR_EVENT ->
                            SensorEventMapper.mapToSwitchSensorAvro((SwitchSensorEvent) event);
                    case SensorEventType.TEMPERATURE_SENSOR_EVENT ->
                            SensorEventMapper.mapToTemperatureSensorAvro((TemperatureSensorEvent) event);
                })
                .build();
        producer.send(new ProducerRecord<>(sensorTopic, sensorEventAvro));
    }

    @Override
    public void collectHubEvent(HubEvent event) {
        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(switch (event.getType()) {
                    case HubEventType.DEVICE_ADDED ->
                            HubEventMapper.mapToDeviceAddedEventAvro((DeviceAddedEvent) event);
                    case HubEventType.DEVICE_REMOVED ->
                            HubEventMapper.mapToDeviceRemovedEventAvro((DeviceRemovedEvent) event);
                    case HubEventType.SCENARIO_ADDED ->
                            HubEventMapper.mapToScenarioAddedEventAvro((ScenarioAddedEvent) event);
                    case HubEventType.SCENARIO_REMOVED ->
                            HubEventMapper.mapToScenarioRemovedEventAvro((ScenarioRemovedEvent) event);
                })
                .build();
        producer.send(new ProducerRecord<>(hubTopic, hubEventAvro));
    }
}

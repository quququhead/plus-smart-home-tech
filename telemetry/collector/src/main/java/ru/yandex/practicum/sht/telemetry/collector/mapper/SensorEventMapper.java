package ru.yandex.practicum.sht.telemetry.collector.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.sht.telemetry.collector.model.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SensorEventMapper {
    public static ClimateSensorAvro mapToClimateSensorAvro(ClimateSensorEvent event) {
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(event.getTemperatureC())
                .setHumidity(event.getHumidity())
                .setCo2Level(event.getCo2Level())
                .build();
    }

    public static LightSensorAvro mapToLightSensorAvro(LightSensorEvent event) {
        return LightSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setLuminosity(event.getLuminosity())
                .build();
    }

    public static MotionSensorAvro mapToMotionSensorAvro(MotionSensorEvent event) {
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setMotion(event.isMotion())
                .setVoltage(event.getVoltage())
                .build();
    }

    public static SwitchSensorAvro mapToSwitchSensorAvro(SwitchSensorEvent event) {
        return SwitchSensorAvro.newBuilder()
                .setState(event.isState())
                .build();
    }

    public static TemperatureSensorAvro mapToTemperatureSensorAvro(TemperatureSensorEvent event) {
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(event.getTemperatureC())
                .setTemperatureF(event.getTemperatureF())
                .build();
    }
}

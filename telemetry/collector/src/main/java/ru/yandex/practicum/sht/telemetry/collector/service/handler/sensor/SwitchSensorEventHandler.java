package ru.yandex.practicum.sht.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.sht.telemetry.collector.service.KafkaEventProducer;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {

    public SwitchSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEventProto event) {
        SwitchSensorProto switchSensorProto = event.getSwitchSensorEvent();
        return SwitchSensorAvro.newBuilder()
                .setState(switchSensorProto.getState())
                .build();
    }

    @Override
    public PayloadCase getMessageType() {
        return PayloadCase.SWITCH_SENSOR_EVENT;
    }
}

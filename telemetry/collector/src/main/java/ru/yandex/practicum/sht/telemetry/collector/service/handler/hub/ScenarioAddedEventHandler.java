package ru.yandex.practicum.sht.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto.PayloadCase;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.sht.telemetry.collector.service.KafkaEventProducer;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEventProto event) {
        ScenarioAddedEventProto scenarioAddedEventProto = event.getScenarioAdded();
        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEventProto.getName())
                .setConditions(scenarioAddedEventProto.getConditionList().stream()
                        .map(scenarioCondition -> ScenarioConditionAvro.newBuilder()
                                .setSensorId(scenarioCondition.getSensorId())
                                .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                                .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()))
                                .setValue(switch (scenarioCondition.getValueCase()) {
                                    case INT_VALUE -> scenarioCondition.getIntValue();
                                    case BOOL_VALUE -> scenarioCondition.getBoolValue();
                                    case VALUE_NOT_SET -> null;
                                })
                                .build())
                        .toList())
                .setActions(scenarioAddedEventProto.getActionList().stream()
                        .map(deviceAction -> DeviceActionAvro.newBuilder()
                                .setSensorId(deviceAction.getSensorId())
                                .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                                .setValue(deviceAction.getValue())
                                .build())
                        .toList())
                .build();
    }

    @Override
    public PayloadCase getMessageType() {
        return PayloadCase.SCENARIO_ADDED;
    }
}

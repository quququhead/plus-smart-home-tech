package ru.yandex.practicum.sht.telemetry.analyzer.service;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;
import ru.yandex.practicum.sht.telemetry.analyzer.model.Action;

import java.time.Instant;

@Service
@Slf4j
public class HubActionSenderImpl implements HubActionSender {

    private final HubRouterControllerBlockingStub hubRouterClient;

    public HubActionSenderImpl(@GrpcClient("hub-router") HubRouterControllerBlockingStub hubRouterClient) {
        this.hubRouterClient = hubRouterClient;
    }

    public void sendAction(String hubId, String scenarioName, String sensorId, Action action) {
        Instant now = Instant.now();
        DeviceActionRequest deviceActionRequest = DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenarioName)
                .setAction(DeviceActionProto.newBuilder()
                        .setSensorId(sensorId)
                        .setType(ActionTypeProto.valueOf(action.getType().name()))
                        .setValue(action.getValue())
                        .build())
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(now.getEpochSecond())
                        .setNanos(now.getNano())
                        .build())
                .build();
        log.info("Send action to Hub Router: {} sensor: {} action: {}",
                hubId, sensorId, action.getType().name());
        try {
            hubRouterClient.handleDeviceAction(deviceActionRequest);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }
}

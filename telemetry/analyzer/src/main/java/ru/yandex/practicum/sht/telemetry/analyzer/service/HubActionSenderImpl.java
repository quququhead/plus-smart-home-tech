package ru.yandex.practicum.sht.telemetry.analyzer.service;

import com.google.protobuf.Timestamp;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;
import ru.yandex.practicum.sht.telemetry.analyzer.model.Action;

import java.time.Instant;

@Service
public class HubActionSenderImpl implements HubActionSender {

    private final HubRouterControllerBlockingStub hubRouterClient;

    public HubActionSenderImpl(@GrpcClient("hub-router") HubRouterControllerBlockingStub hubRouterClient) {
        this.hubRouterClient = hubRouterClient;
    }

    public void sendAction(String hubId, String scenarioName, Action action) {
        Instant now = Instant.now();
        DeviceActionRequest deviceActionRequest = DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenarioName)
                .setAction(DeviceActionProto.newBuilder()
                        .setSensorId(action.getSensor().getId())
                        .setType(ActionTypeProto.valueOf(action.getType().name()))
                        .setValue(action.getValue()))
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(now.getEpochSecond())
                        .setNanos(now.getNano())
                        .build())
                .build();
        hubRouterClient.handleDeviceAction(deviceActionRequest);
    }
}

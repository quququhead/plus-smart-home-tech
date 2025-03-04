package ru.yandex.practicum.sht.telemetry.collector.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank
    @Size(min = 3, message = "Length of field 'name' should be in the range from 3 to 2147483647")
    private String name;
    @Valid
    private List<ScenarioCondition> conditions;
    @Valid
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}

package ru.yandex.practicum.sht.telemetry.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.sht.telemetry.analyzer.service.HubEventProcessor;
import ru.yandex.practicum.sht.telemetry.analyzer.service.SnapshotProcessor;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AnalyzerApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AnalyzerApplication.class, args);
        final HubEventProcessor eventProcessor = context.getBean(HubEventProcessor.class);
        SnapshotProcessor snapshotProcessor = context.getBean(SnapshotProcessor.class);
        Thread hubThread = new Thread(eventProcessor);
        hubThread.setName("HubEventHandlerThread");
        hubThread.start();
        snapshotProcessor.start();
    }
}

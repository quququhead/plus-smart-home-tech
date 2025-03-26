package ru.yandex.practicum.sht.telemetry.analyzer.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

@Getter @Setter @ToString
@ConfigurationProperties("analyzer.kafka")
public class KafkaConfig {
    private HubConsumerConfig hubConsumer;
    private SnapshotConsumerConfig snapshotConsumer;

    @Getter
    public static class HubConsumerConfig {
        private final Properties properties;
        private final EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);

        public HubConsumerConfig(Properties properties, Map<String, String> topics) {
            this.properties = properties;
            for (Map.Entry<String, String> entry : topics.entrySet()) {
                this.topics.put(TopicType.from(entry.getKey()), entry.getValue());
            }
        }
    }

    @Getter
    public static class SnapshotConsumerConfig {
        private final Properties properties;
        private final EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);

        public SnapshotConsumerConfig(Properties properties, Map<String, String> topics) {
            this.properties = properties;
            for (Map.Entry<String, String> entry : topics.entrySet()) {
                this.topics.put(TopicType.from(entry.getKey()), entry.getValue());
            }
        }
    }

    public enum TopicType {
        HUBS_EVENTS, SNAPSHOTS_EVENTS;

        public static TopicType from(String type) {
            for (TopicType value : values()) {
                if (value.name().equalsIgnoreCase(type.replace("-","_"))) {
                    return value;
                }
            }
            return null;
        }
    }
}

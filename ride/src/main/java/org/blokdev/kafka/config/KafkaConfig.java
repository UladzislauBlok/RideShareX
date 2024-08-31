package org.blokdev.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.producer.trip-topic.name}")
    private String tripTopicName;
    @Value("${kafka.producer.trip-topic.partitions}")
    private Integer tripTopicPartitions;
    @Value("${kafka.producer.trip-topic.replicas}")
    private Integer tripTopicReplicas;

    @Value("${kafka.producer.rating-topic.name}")
    private String ratingTopicName;
    @Value("${kafka.producer.rating-topic.partitions}")
    private Integer ratingTopicPartitions;
    @Value("${kafka.producer.rating-topic.replicas}")
    private Integer ratingTopicReplicas;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createTripTopic() {
        return TopicBuilder.name(tripTopicName)
                .partitions(tripTopicPartitions)
                .replicas(tripTopicReplicas)
                .build();
    }

    @Bean
    public NewTopic createRatingTopic() {
        return TopicBuilder.name(ratingTopicName)
                .partitions(ratingTopicPartitions)
                .replicas(ratingTopicReplicas)
                .build();
    }
}

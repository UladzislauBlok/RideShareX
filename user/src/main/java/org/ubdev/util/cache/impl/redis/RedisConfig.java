package org.ubdev.util.cache.impl.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.ubdev.user.model.User;

import java.util.UUID;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<UUID, User> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<UUID, User> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        template.setKeySerializer(new GenericToStringSerializer<>(UUID.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, User.class));
        return template;
    }
}

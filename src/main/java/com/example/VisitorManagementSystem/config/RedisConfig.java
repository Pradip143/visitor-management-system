package com.example.VisitorManagementSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.VisitorManagementSystem.dto.VisitorDTO;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, VisitorDTO> productRedisTemplate (RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,VisitorDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<VisitorDTO>(VisitorDTO.class));
        return template;
    }
}

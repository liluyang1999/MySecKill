package com.example.lly.module.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;

@Configuration
@PropertySource("classpath:application.properties")
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setStringSerializer(redisSerializer);
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(factory);
        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setStringSerializer(redisSerializer);
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisSentinelConfiguration redisSentinelConfiguration() {
        RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
        String[] sentinelNodes = {"127.0.0.1:26379", "127.0.0.1:36379", "127.0.0.1:46379"};
        for (String each : sentinelNodes) {
            String[] item = each.split(":");
            String ipAddress = item[0];
            String port = item[1];
            configuration.addSentinel(new RedisNode(ipAddress, Integer.parseInt(port)));
        }
        configuration.setMaster("master");
        return configuration;
    }


    private final Duration livingTime = Duration.ofMinutes(30L);
    private final Jackson2JsonRedisSerializer<Serializable> jackson2JsonRedisSerializer;
    private final RedisSerializer<String> redisSerializer;

    public RedisConfig() {
        redisSerializer = new StringRedisSerializer();
        //查询缓存配置 👇   默认是JDK Serializer，出现的是乱码无法阅读
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        this.jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Serializable.class);
        this.jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    }


    @Bean
    public KeyGenerator keyGenerator() {
        //Define the existing policy of cache data
        //Lambda expression
        return (target, method, params) -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(target.getClass().getName());
            stringBuilder.append(method.getName());
            for (Object object : params) {
                stringBuilder.append(object.toString());
            }
            return stringBuilder.toString();
        };
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        //redis缓存属性配置
        RedisCacheConfiguration redisCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                                            .entryTtl(this.livingTime)
                                            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(this.redisSerializer))
                                            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(this.jackson2JsonRedisSerializer))
                                            .disableCachingNullValues();
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(redisCacheConfig).build();
    }

}

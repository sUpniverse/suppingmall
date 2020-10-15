package com.supshop.suppingmall.config;


//@Configuration
//@EnableCaching
public class RedisCacheConfig {

//    @Bean
//    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory);
//        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(2))
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//
//        return builder.cacheDefaults(configuration).build();
//    }
}

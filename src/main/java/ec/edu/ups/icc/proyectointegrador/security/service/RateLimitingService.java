package ec.edu.ups.icc.proyectointegrador.security.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitingService {

    private final StringRedisTemplate redisTemplate;
    private static final String RATE_LIMIT_PREFIX = "rate-limit:";

    public RateLimitingService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String operationKey, String identifier, long maxLimit, Duration windowTime) {
        String key = RATE_LIMIT_PREFIX + operationKey + ":" + identifier;
        
        Long currentCount = redisTemplate.opsForValue().increment(key);

        if (currentCount != null && currentCount == 1) {
            redisTemplate.expire(key, windowTime);
        }

        return currentCount != null && currentCount <= maxLimit;
    }
}
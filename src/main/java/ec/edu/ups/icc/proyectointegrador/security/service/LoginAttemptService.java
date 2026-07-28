package ec.edu.ups.icc.proyectointegrador.security.service;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private final StringRedisTemplate redisTemplate;
    
    private static final String BLOCKED_USER_PREFIX = "blocked-user:";
    private static final String ATTEMPTS_PREFIX = "login-attempts:";

    private static final int MAX_ATTEMPTS = 3; // Bloquear al 3er intento fallido
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(15); // Bloqueo de 15 minutos


    public LoginAttemptService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
   
    }

    public void loginFailed(String email) {
        String attemptsKey = ATTEMPTS_PREFIX + email;
        Long attempts = redisTemplate.opsForValue().increment(attemptsKey);
        
        if (attempts != null && attempts == 1) {
            // Dar un tiempo de vida al contador de intentos para que no se quede para siempre
            redisTemplate.expire(attemptsKey, Duration.ofMinutes(10));
        }
        // Limpiamos el contador de intentos
            redisTemplate.delete(attemptsKey);
    }

    public boolean isBlocked(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLOCKED_USER_PREFIX + email));
    }

    public void loginSucceeded(String email) {
        redisTemplate.delete(ATTEMPTS_PREFIX + email);
    }
}

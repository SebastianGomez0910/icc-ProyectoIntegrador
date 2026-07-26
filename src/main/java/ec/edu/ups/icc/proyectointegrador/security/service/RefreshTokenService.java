package ec.edu.ups.icc.proyectointegrador.security.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ec.edu.ups.icc.proyectointegrador.security.entities.RefreshToken;
import ec.edu.ups.icc.proyectointegrador.security.repository.RefreshTokenRepository;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import ec.edu.ups.icc.proyectointegrador.user.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {
    
    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        
        refreshToken.setTokenId(UUID.randomUUID());
        refreshToken.setTokenHash(UUID.randomUUID().toString());

        Instant now = Instant.now();
        refreshToken.setCreatedAt(now);
        
        long secondsToAdd = refreshTokenDurationMs / 1000;
        refreshToken.setExpiresAt(now.plusSeconds(secondsToAdd));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token); 
            throw new RuntimeException("El Refresh Token ha expirado.");
        }
        
        if (token.getRevokedAt() != null) {
            throw new RuntimeException("Este Refresh Token ha sido revocado.");
        }
        
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByTokenHash(token);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUser_Id(userId);
    }
}

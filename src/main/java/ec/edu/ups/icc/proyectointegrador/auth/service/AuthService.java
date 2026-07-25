package ec.edu.ups.icc.proyectointegrador.auth.service;

import ec.edu.ups.icc.proyectointegrador.auth.dto.AuthResponseDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.LoginRequestDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.RefreshTokenRequestDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.RegisterRequestDto;
import ec.edu.ups.icc.proyectointegrador.user.entity.Role;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import ec.edu.ups.icc.proyectointegrador.user.repositories.RoleRepository;
import ec.edu.ups.icc.proyectointegrador.user.repositories.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public AuthResponseDto login(LoginRequestDto loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!user.getPasswordHash().equals(loginRequest.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String accessToken = "dummy-access-token";
        String refreshToken = "dummy-refresh-token";

        return buildAuthResponse(accessToken, refreshToken, user);
    }

    @Transactional
    public AuthResponseDto register(RegisterRequestDto registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(registerRequest.getPassword());
        user.setStatus("ACTIVE");

        Role userRole = roleRepository.findByName("PARTICIPANT")
                .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        String accessToken = "dummy-access-token";
        String refreshToken = "dummy-refresh-token";

        return buildAuthResponse(accessToken, refreshToken, savedUser);
    }

    @Transactional
    public AuthResponseDto refresh(RefreshTokenRequestDto request) {
        User user = userRepository.findByEmail("admin@ejemplo.com")
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String newAccessToken = "new-dummy-access-token";
        String newRefreshToken = request.getRefreshToken();

        return buildAuthResponse(newAccessToken, newRefreshToken, user);
    }

    @Transactional
    public void logout(RefreshTokenRequestDto request) {
    }

    private AuthResponseDto buildAuthResponse(String accessToken, String refreshToken, User user) {
        Set<String> roles = user.getRoles() != null
                ? user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
                : Set.of();

        String fullName = user.getFirstName() + " " + user.getLastName();

        return new AuthResponseDto(
                accessToken,
                refreshToken,
                user.getId(), // <-- Agregamos el ID del usuario aquí
                fullName,
                user.getEmail(),
                roles);
    }
}
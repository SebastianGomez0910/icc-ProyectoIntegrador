package ec.edu.ups.icc.proyectointegrador.auth.service;

import ec.edu.ups.icc.proyectointegrador.auth.dto.AuthResponseDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.LoginRequestDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.RefreshTokenRequestDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.RegisterRequestDto;
import ec.edu.ups.icc.proyectointegrador.security.JwtService;
import ec.edu.ups.icc.proyectointegrador.security.UserDetailsImpl;
import ec.edu.ups.icc.proyectointegrador.security.entities.RefreshToken;
import ec.edu.ups.icc.proyectointegrador.security.service.RefreshTokenService;
import ec.edu.ups.icc.proyectointegrador.user.entity.Role;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import ec.edu.ups.icc.proyectointegrador.user.repositories.RoleRepository;
import ec.edu.ups.icc.proyectointegrador.user.repositories.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository, 
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthResponseDto login(LoginRequestDto loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
                UserDetails userDetails = UserDetailsImpl.build(user);
                String accessToken = jwtService.generateToken(userDetails);
                RefreshToken refreshTokenEntity = refreshTokenService.createRefreshToken(user.getId());
                String refreshToken = refreshTokenEntity.getTokenHash(); 

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
        // implementamos el bycript para encriptar la contraseña antes de guardarla en la base de datos
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setStatus("ACTIVE");

        Role userRole = roleRepository.findByName("PARTICIPANT")
                .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        UserDetails userDetails = UserDetailsImpl.build(savedUser);
        String accessToken = jwtService.generateToken(userDetails);
        
        RefreshToken refreshTokenEntity = refreshTokenService.createRefreshToken(savedUser.getId());
        String refreshToken = refreshTokenEntity.getTokenHash();

        return buildAuthResponse(accessToken, refreshToken, savedUser);
    }

    @Transactional
    public AuthResponseDto refresh(RefreshTokenRequestDto request) {
        RefreshToken tokenEntity=refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(()-> new RuntimeException("Refres token no encontrado en la base de datos"));

        refreshTokenService.verifyExpiration(tokenEntity);
        User user=tokenEntity.getUser();
        UserDetails userDetails = UserDetailsImpl.build(user);
        String newAccessToken = jwtService.generateToken(userDetails);

        return buildAuthResponse(newAccessToken, tokenEntity.getTokenHash(), user);
    }

    @Transactional
    public void logout(RefreshTokenRequestDto request) {
        RefreshToken tokenEntity=refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(()->new RuntimeException("Refresh Token no encontrado"));
        refreshTokenService.deleteByUserId(tokenEntity.getUser().getId());
    }

    private AuthResponseDto buildAuthResponse(String accessToken, String refreshToken, User user) {
        Set<String> roles = user.getRoles() != null
                ? user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
                : Set.of();

        String fullName = user.getFirstName() + " " + user.getLastName();

        return new AuthResponseDto(
                accessToken,
                refreshToken,
                user.getId(), 
                fullName,
                user.getEmail(),
                roles);
    }
}
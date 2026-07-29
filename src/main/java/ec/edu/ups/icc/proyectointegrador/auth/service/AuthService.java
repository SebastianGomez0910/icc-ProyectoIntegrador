package ec.edu.ups.icc.proyectointegrador.auth.service;

import ec.edu.ups.icc.proyectointegrador.auth.dto.AuthResponseDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.LoginRequestDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.RefreshTokenRequestDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.RegisterRequestDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.UserProfileDto;
import ec.edu.ups.icc.proyectointegrador.common.exception.domain.BusinessRuleException;
import ec.edu.ups.icc.proyectointegrador.common.exception.domain.ConflictException;
import ec.edu.ups.icc.proyectointegrador.common.exception.domain.ForbiddenOperationException;
import ec.edu.ups.icc.proyectointegrador.common.exception.domain.ResourceNotFoundException;
import ec.edu.ups.icc.proyectointegrador.common.exception.domain.TooManyRequestsException;
import ec.edu.ups.icc.proyectointegrador.security.JwtService;
import ec.edu.ups.icc.proyectointegrador.security.UserDetailsImpl;
import ec.edu.ups.icc.proyectointegrador.security.entities.RefreshToken;
import ec.edu.ups.icc.proyectointegrador.security.service.LoginAttemptService;
import ec.edu.ups.icc.proyectointegrador.security.service.RateLimitingService;
import ec.edu.ups.icc.proyectointegrador.security.service.RefreshTokenService;
import ec.edu.ups.icc.proyectointegrador.user.entity.Role;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import ec.edu.ups.icc.proyectointegrador.user.repositories.RoleRepository;
import ec.edu.ups.icc.proyectointegrador.user.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.AuthenticationException;

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

    private final LoginAttemptService loginAttemptService;
    private final RateLimitingService rateLimitingService;
    private final HttpServletRequest request;

    public AuthService(UserRepository userRepository, 
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       RefreshTokenService refreshTokenService,
                       LoginAttemptService loginAttemptService,
                       RateLimitingService rateLimitingService,
                       HttpServletRequest request) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.loginAttemptService = loginAttemptService;
        this.rateLimitingService = rateLimitingService;
        this.request = request;
    }

    @Transactional
    public AuthResponseDto login(LoginRequestDto loginRequest) {
        String email = loginRequest.getEmail();
        String clientIp = getClientIp(request);

        // Rate Limiting por IP + Correo (5 peticiones por minuto)
        String rateLimitIdentifier = clientIp + ":" + email;
        boolean isAllowed = rateLimitingService.isAllowed("login", rateLimitIdentifier, 5, Duration.ofMinutes(1));
        if (!isAllowed) {
            throw new TooManyRequestsException("Has superado el límite de peticiones de inicio de sesión."); 
        }
        //Verificar si el usuario está bloqueado por intentos fallidos
        if (loginAttemptService.isBlocked(email)) {
            throw new ForbiddenOperationException("Cuenta temporalmente bloqueada por múltiples intentos fallidos. Intenta en 15 minutos.");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            loginAttemptService.loginFailed(email);
            throw new BusinessRuleException("Credenciales incorrectas");
        }

        loginAttemptService.loginSucceeded(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        UserDetails userDetails = UserDetailsImpl.build(user);
        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshTokenEntity = refreshTokenService.createRefreshToken(user.getId());
        String refreshToken = refreshTokenEntity.getTokenHash(); 

        return buildAuthResponse(accessToken, refreshToken, user);
    }
    
        private String getClientIp(HttpServletRequest request) {
            String xfHeader = request.getHeader("X-Forwarded-For");
            if (xfHeader == null) {
                return request.getRemoteAddr();
            }
            return xfHeader.split(",")[0];
        }

    @Transactional
    public AuthResponseDto register(RegisterRequestDto registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException("El email ya está registrado");
        }

        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        // implementamos el bycript para encriptar la contraseña antes de guardarla en la base de datos
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setStatus("ACTIVE");

        Role userRole = roleRepository.findByNameIgnoreCase("PARTICIPANT")
        .orElseThrow(() -> new ResourceNotFoundException("Rol por defecto no encontrado"));

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
                .orElseThrow(()-> new ResourceNotFoundException("Refres token no encontrado en la base de datos"));

        refreshTokenService.verifyExpiration(tokenEntity);
        User user=tokenEntity.getUser();
        UserDetails userDetails = UserDetailsImpl.build(user);
        String newAccessToken = jwtService.generateToken(userDetails);

        return buildAuthResponse(newAccessToken, tokenEntity.getTokenHash(), user);
    }

    @Transactional
    public void logout(RefreshTokenRequestDto request) {
        RefreshToken tokenEntity=refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(()->new ResourceNotFoundException("Refresh Token no encontrado"));
        refreshTokenService.deleteByUserId(tokenEntity.getUser().getId());
    }

    @Transactional(readOnly = true)
    public UserProfileDto getAuthenticatedUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        Set<String> roles = user.getRoles() != null
                ? user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
                : Set.of();
                
        String fullName = user.getFirstName() + " " + user.getLastName();
        
        return new UserProfileDto(user.getId(), fullName, user.getEmail(), roles);
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
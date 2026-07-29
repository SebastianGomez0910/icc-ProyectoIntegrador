package ec.edu.ups.icc.proyectointegrador.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.proyectointegrador.auth.dto.AuthResponseDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.LoginRequestDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.RefreshTokenRequestDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.RegisterRequestDto;
import ec.edu.ups.icc.proyectointegrador.auth.dto.UserProfileDto;
import ec.edu.ups.icc.proyectointegrador.auth.service.AuthService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Autenticación", description = "Endpoints para registro, inicio de sesión y gestión de tokens de seguridad")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Registrar nuevo usuario", description = "Crea una nueva cuenta de usuario en el sistema y devuelve los tokens de acceso iniciales.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o el correo ya está registrado")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        AuthResponseDto response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica a un usuario con sus credenciales y devuelve un token JWT para acceder a las rutas protegidas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
        @ApiResponse(responseCode = "400", description = "Formato de credenciales incorrecto"),
        @ApiResponse(responseCode = "401", description = "Correo o contraseña incorrectos")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        AuthResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }
 
    @Operation(summary = "Refrescar token", description = "Genera un nuevo token JWT de acceso utilizando un Refresh Token válido, sin necesidad de volver a iniciar sesión.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nuevo token generado exitosamente"),
        @ApiResponse(responseCode = "400", description = "El refresh token no fue enviado"),
        @ApiResponse(responseCode = "403", description = "El refresh token es inválido o ha expirado")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@Valid @RequestBody RefreshTokenRequestDto request) {
        AuthResponseDto response = authService.refresh(request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Cerrar sesión", description = "Invalida el Refresh Token proporcionado, cerrando la sesión del usuario en el servidor.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sesión cerrada exitosamente (No Content)"),
        @ApiResponse(responseCode = "400", description = "El refresh token proporcionado no es válido")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequestDto request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener perfil de usuario", description = "Devuelve la información del usuario actualmente autenticado a partir de su token JWT.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil recuperado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado o token inválido")
    })
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getMe(Authentication authentication) {
        UserProfileDto profile = authService.getAuthenticatedUser(authentication.getName());
        return ResponseEntity.ok(profile);
    }
}

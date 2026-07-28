package ec.edu.ups.icc.proyectointegrador.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "Objeto que contiene el token de acceso y los datos básicos del usuario tras un inicio de sesión exitoso")
public class AuthResponseDto {

    @Schema(description = "Token JWT de acceso principal para consumir la API", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Token de refresco para obtener un nuevo token cuando el principal expire", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private String refreshToken;

    @Schema(description = "Tipo de esquema de autenticación", example = "Bearer")
    private String type = "Bearer";
    
    @Schema(description = "Identificador único del usuario en la base de datos", example = "1")
    private Long userId;
    
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String name;
    
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    private String email;

    @Schema(description = "Conjunto de roles asignados al usuario", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private Set<String> roles;

    public AuthResponseDto() {
    }

    public AuthResponseDto(String token, String refreshToken, Long userId, String name, String email,
            Set<String> roles) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.type = "Bearer";
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
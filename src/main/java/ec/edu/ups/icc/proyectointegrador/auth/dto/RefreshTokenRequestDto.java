package ec.edu.ups.icc.proyectointegrador.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto utilizado para solicitar un nuevo token de acceso cuando el actual ha expirado")
public class RefreshTokenRequestDto {

    @Schema(description = "Token de refresco (Refresh Token) obtenido durante el inicio de sesión original", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    @NotBlank(message = "El refresh token es obligatorio")
    private String refreshToken;

    public RefreshTokenRequestDto() {
    }

    public RefreshTokenRequestDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}

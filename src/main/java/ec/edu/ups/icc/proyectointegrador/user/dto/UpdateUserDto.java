package ec.edu.ups.icc.proyectointegrador.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto que contiene los datos permitidos para actualizar la información personal o el estado de un usuario")
public class UpdateUserDto {

    @Schema(description = "Nombre actualizado del usuario", example = "Carlos")
    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @Schema(description = "Apellido actualizado del usuario", example = "Andrade")
    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @Schema(description = "Correo electrónico actualizado", example = "carlos.andrade@ejemplo.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ingresar un email válido")
    private String email;

    @Schema(description = "Estado operativo de la cuenta de usuario (ej. ACTIVO, INACTIVO, SUSPENDIDO)", example = "ACTIVO")
    private String status; // Ej: "ACTIVE", "INACTIVE"

    // Getters y Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
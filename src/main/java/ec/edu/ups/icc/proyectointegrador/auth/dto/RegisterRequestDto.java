package ec.edu.ups.icc.proyectointegrador.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto que contiene los datos requeridos para registrar un nuevo usuario en el sistema")
public class RegisterRequestDto {

    @Schema(description = "Nombre del usuario", example = "Juan")
    @NotBlank(message = "Nombre es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String firstName;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    @NotBlank(message = "Apellido es obligatorio")
    @Size(min = 3, max = 150, message = "El apellido debe tener entre 3 y 150 caracteres")
    private String lastName;

    @Schema(description = "Correo electrónico que servirá como identificador de inicio de sesión", example = "juan.perez@ejemplo.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ingresar un email válido")
    @Size(max = 150, message = "El email no debe superar los 150 caracteres")
    private String email;

    @Schema(description = "Contraseña segura de la cuenta", example = "MiClaveSegura123!")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    public RegisterRequestDto() {
    }

    public RegisterRequestDto(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
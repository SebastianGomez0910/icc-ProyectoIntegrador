package ec.edu.ups.icc.proyectointegrador.user.dto;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto que representa la información pública y perfil de un usuario registrado")
public class UserResponseDto {

    @Schema(description = "Identificador único del usuario en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Carlos")
    private String firstName;

    @Schema(description = "Apellido del usuario", example = "Andrade")
    private String lastName;

    @Schema(description = "Correo electrónico del usuario", example = "carlos.andrade@ejemplo.com")
    private String email;

    @Schema(description = "Estado actual de la cuenta en el sistema", example = "ACTIVO")
    private String status;

    @Schema(description = "Conjunto de roles o permisos asignados al usuario", example = "[\"PARTICIPANT\", \"ORGANIZER\"]")
    private Set<String> roles;

    public UserResponseDto() {
    }

    public UserResponseDto(Long id, String firstName, String lastName, String email, String status, Set<String> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
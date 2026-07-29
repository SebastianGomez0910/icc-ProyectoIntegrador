package ec.edu.ups.icc.proyectointegrador.auth.dto;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto que contiene los datos del perfil del usuario autenticado")
public class UserProfileDto {
    
    @Schema(description = "Identificador único del usuario", example = "1")
    private Long id;
    
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String name;
    
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
    private String email;

    @Schema(description = "Conjunto de roles asignados al usuario", example = "[\"PARTICIPANT\"]")
    private Set<String> roles;

    public UserProfileDto(Long id, String name, String email, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

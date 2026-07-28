package ec.edu.ups.icc.proyectointegrador.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Objeto utilizado para solicitar la asignación de un nuevo rol a un usuario existente")
public class AssignRoleDto {

    @Schema(description = "Identificador único del rol en la base de datos (por ejemplo: 1 para ADMIN, 2 para ORGANIZADOR, 3 para PARTICIPANTE)", example = "2")
    @NotNull(message = "El ID del rol es obligatorio")
    private Long roleId;

    // Getter y Setter
    public Long getRoleId() { 
        return roleId; 
    }
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
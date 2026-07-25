package ec.edu.ups.icc.proyectointegrador.user.dto;

import jakarta.validation.constraints.NotNull;

public class AssignRoleDto {
    @NotNull(message = "El ID del rol es obligatorio")
    private Long roleId;

    // Getter y Setter
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
}
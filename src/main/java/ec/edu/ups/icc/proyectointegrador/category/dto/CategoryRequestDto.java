package ec.edu.ups.icc.proyectointegrador.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequestDto {
    
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String description;

    public CategoryRequestDto() {
    }

    public CategoryRequestDto(
            @NotBlank(message = "El nombre de la categoría es obligatorio") @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres") String name,
            @Size(max = 200, message = "La descripción no puede superar los 200 caracteres") String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

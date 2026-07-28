package ec.edu.ups.icc.proyectointegrador.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto que representa los datos necesarios para crear o actualizar una categoría de evento")
public class CategoryRequestDto {
    
    @Schema(description = "Nombre oficial de la categoría", example = "Inteligencia Artificial")
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;

    @Schema(description = "Breve descripción sobre el tipo de eventos que abarca esta categoría", example = "Eventos académicos relacionados con machine learning, redes neuronales y automatización.")
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

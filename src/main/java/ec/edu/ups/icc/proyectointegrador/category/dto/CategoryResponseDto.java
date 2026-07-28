package ec.edu.ups.icc.proyectointegrador.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto que representa los detalles de una categoría devuelta por el servidor")
public class CategoryResponseDto {
    
    @Schema(description = "Identificador único de la categoría en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Nombre oficial de la categoría", example = "Inteligencia Artificial")
    private String name;

    @Schema(description = "Breve descripción sobre el tipo de eventos que abarca esta categoría", example = "Eventos académicos relacionados con machine learning, redes neuronales y automatización.")
    private String description;
    
    public CategoryResponseDto() {
    }

    public CategoryResponseDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

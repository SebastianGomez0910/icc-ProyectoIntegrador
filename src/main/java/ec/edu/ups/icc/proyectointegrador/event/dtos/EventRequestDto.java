package ec.edu.ups.icc.proyectointegrador.event.dtos;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto que contiene los datos necesarios para organizar o actualizar un evento académico")
public class EventRequestDto {
    
    @Schema(description = "Título principal del evento", example = "Taller Avanzado de Spring Boot")
    private String title;

    @Schema(description = "Descripción detallada del evento, objetivos y temario", example = "Un taller práctico intensivo sobre cómo construir APIs modernas para proyectos integradores.")
    private String description;

    @Schema(description = "Modalidad de asistencia (ej. PRESENCIAL, VIRTUAL, HIBRIDO)", example = "PRESENCIAL")
    private String modality;
    
    @Schema(description = "Ubicación física del evento (obligatorio si es presencial o híbrido)", example = "Auditorio General - Universidad Politécnica Salesiana")
    private String location;

    @Schema(description = "Enlace de transmisión (obligatorio si es virtual o híbrido)", example = "https://zoom.us/j/123456789")
    private String virtualUrl;

    @Schema(description = "Capacidad máxima de asistentes permitidos", example = "50")
    private Integer capacity;

    @Schema(description = "Fecha y hora exacta en la que se abren las inscripciones (formato ISO-8601)", example = "2026-08-01T08:00:00Z")
    private Instant registrationStartAt;

    @Schema(description = "Fecha y hora exacta en la que se cierran las inscripciones (formato ISO-8601)", example = "2026-08-15T23:59:59Z")
    private Instant registrationEndAt;

    @Schema(description = "Fecha y hora de inicio del evento (formato ISO-8601)", example = "2026-08-20T09:00:00Z")
    private Instant startAt;

    @Schema(description = "Fecha y hora de finalización del evento (formato ISO-8601)", example = "2026-08-20T17:00:00Z")
    private Instant endAt;

    @Schema(description = "ID de la categoría temática a la que pertenece el evento", example = "1")
    private Long categoryId;

    public EventRequestDto(String title, String description, String modality, String location, String virtualUrl,
            Integer capacity, Instant registrationStartAt, Instant registrationEndAt, Instant startAt, Instant endAt,
            Long categoryId) {
        this.title = title;
        this.description = description;
        this.modality = modality;
        this.location = location;
        this.virtualUrl = virtualUrl;
        this.capacity = capacity;
        this.registrationStartAt = registrationStartAt;
        this.registrationEndAt = registrationEndAt;
        this.startAt = startAt;
        this.endAt = endAt;
        this.categoryId = categoryId;
    }

    public EventRequestDto() {
    }
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getModality() {
        return modality;
    }
    public void setModality(String modality) {
        this.modality = modality;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getVirtualUrl() {
        return virtualUrl;
    }
    public void setVirtualUrl(String virtualUrl) {
        this.virtualUrl = virtualUrl;
    }
    public Integer getCapacity() {
        return capacity;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    public Instant getRegistrationStartAt() {
        return registrationStartAt;
    }
    public void setRegistrationStartAt(Instant registrationStartAt) {
        this.registrationStartAt = registrationStartAt;
    }
    public Instant getRegistrationEndAt() {
        return registrationEndAt;
    }
    public void setRegistrationEndAt(Instant registrationEndAt) {
        this.registrationEndAt = registrationEndAt;
    }
    public Instant getStartAt() {
        return startAt;
    }
    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }
    public Instant getEndAt() {
        return endAt;
    }
    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // Getters and setters
    
}

package ec.edu.ups.icc.proyectointegrador.event.dtos;

import java.time.Instant;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto que representa los detalles completos de un evento devuelto por el servidor")
public class EventResponseDto {
 
    @Schema(description = "Identificador único del evento en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Título principal del evento", example = "Taller Avanzado de Spring Boot")
    private String title;

    @Schema(description = "Descripción detallada del evento, objetivos y temario", example = "Un taller práctico intensivo sobre cómo construir APIs modernas para proyectos integradores.")
    private String description;

    @Schema(description = "Modalidad de asistencia (ej. PRESENCIAL, VIRTUAL, HIBRIDO)", example = "PRESENCIAL")
    private String modality;

    @Schema(description = "Ubicación física del evento", example = "Auditorio General - Campus Sur")
    private String location;

    @Schema(description = "Enlace de transmisión para eventos virtuales o híbridos", example = "https://zoom.us/j/123456789")
    private String virtualUrl;

    @Schema(description = "Capacidad total máxima de asistentes permitidos", example = "50")
    private Integer capacity;

    @Schema(description = "Cupos disponibles actualmente (calculado dinámicamente)", example = "15")
    private Integer availableCapacity;

    @Schema(description = "Fecha y hora de inicio del evento (formato ISO-8601)", example = "2026-08-20T09:00:00Z")
    private Instant startAt;

    @Schema(description = "Fecha y hora de finalización del evento (formato ISO-8601)", example = "2026-08-20T17:00:00Z")
    private Instant endAt;

    @Schema(description = "Estado actual del evento (ej. PUBLICADO, EN_CURSO, FINALIZADO, CANCELADO)", example = "PUBLICADO")
    private String status;

    @Schema(description = "Nombre completo del usuario que organizó el evento", example = "Juan Pérez")
    private String organizerName; 

    @Schema(description = "Nombre oficial de la categoría temática a la que pertenece", example = "Inteligencia Artificial")
    private String categoryName;

    private List<SessionResponseDto> sessions;

    public EventResponseDto() {
    }

    public EventResponseDto(Long id, String title, String description, String modality, String location,
            String virtualUrl, Integer capacity, Integer availableCapacity, Instant startAt, Instant endAt,
            String status, String organizerName, String categoryName, List<SessionResponseDto> sessions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.modality = modality;
        this.location = location;
        this.virtualUrl = virtualUrl;
        this.capacity = capacity;
        this.availableCapacity = availableCapacity;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.organizerName = organizerName;
        this.categoryName = categoryName;
        this.sessions = sessions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(Integer availableCapacity) {
        this.availableCapacity = availableCapacity;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<SessionResponseDto> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionResponseDto> sessions) {
        this.sessions = sessions;
    }
}

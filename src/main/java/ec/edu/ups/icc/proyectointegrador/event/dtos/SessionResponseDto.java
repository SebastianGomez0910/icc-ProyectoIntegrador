package ec.edu.ups.icc.proyectointegrador.event.dtos;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto que representa los detalles de una sesión o charla programada dentro de un evento")
public class SessionResponseDto {
    
    @Schema(description = "Identificador único de la sesión en la base de datos", example = "10")
    private Long id;

    @Schema(description = "Título específico de la sesión o taller", example = "Introducción a los Controladores REST")
    private String title;

    @Schema(description = "Fecha y hora exacta de inicio de la sesión (formato ISO-8601)", example = "2026-08-20T10:00:00Z")
    private Instant startAt;

    @Schema(description = "Fecha y hora exacta de finalización de la sesión (formato ISO-8601)", example = "2026-08-20T12:00:00Z")
    private Instant endAt;

    @Schema(description = "Ubicación física específica de la sesión", example = "Laboratorio de Cómputo - UPS")
    private String location;

    @Schema(description = "Enlace de transmisión exclusivo para esta sesión", example = "https://zoom.us/j/987654321")
    private String virtualUrl;
    
    public SessionResponseDto() {
    }

    public SessionResponseDto(Long id, String title, Instant startAt, Instant endAt, String location,
            String virtualUrl) {
        this.id = id;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.location = location;
        this.virtualUrl = virtualUrl;
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
}

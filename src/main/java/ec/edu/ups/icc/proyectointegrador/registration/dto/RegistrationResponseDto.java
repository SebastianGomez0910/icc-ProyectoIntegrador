package ec.edu.ups.icc.proyectointegrador.registration.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto que contiene los detalles completos de la inscripción de un participante a un evento")
public class RegistrationResponseDto {

    @Schema(description = "Identificador único de la inscripción en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Código único de verificación o seguimiento de la inscripción", example = "a1b2c3d4-e5f6-7890-abcd-ef0123456789")
    private UUID registrationCode;

    @Schema(description = "Identificador único del evento al que pertenece la inscripción", example = "15")
    private Long eventId;

    @Schema(description = "Título o nombre del evento académico", example = "Taller Avanzado de Spring Boot")
    private String eventTitle;

    @Schema(description = "Identificador único del usuario participante", example = "3")
    private Long participantId;

    @Schema(description = "Nombre completo del participante inscrito", example = "Alexis Gomez")
    private String participantName;

    @Schema(description = "Estado actual de la inscripción", example = "CONFIRMED")
    private String status;

    @Schema(description = "Fecha y hora exacta en que se solicitó la inscripción", example = "2026-07-28T14:30:00")
    private LocalDateTime registeredAt;

    @Schema(description = "Fecha y hora exacta en que se confirmó la inscripción", example = "2026-07-28T14:30:02")
    private LocalDateTime confirmedAt;

    @Schema(description = "Fecha y hora exacta en que se canceló la inscripción (si aplica)")
    private LocalDateTime cancelledAt;

    public RegistrationResponseDto() {
    }

    public RegistrationResponseDto(Long id, UUID registrationCode, Long eventId, String eventTitle,
            Long participantId, String participantName, String status, LocalDateTime registeredAt,
            LocalDateTime confirmedAt, LocalDateTime cancelledAt) {

        this.id = id;
        this.registrationCode = registrationCode;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.participantId = participantId;
        this.participantName = participantName;
        this.status = status;
        this.registeredAt = registeredAt;
        this.confirmedAt = confirmedAt;
        this.cancelledAt = cancelledAt;
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(UUID registrationCode) {
        this.registrationCode = registrationCode;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

}

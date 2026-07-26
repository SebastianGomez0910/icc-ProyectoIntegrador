package ec.edu.ups.icc.proyectointegrador.registration.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class RegistrationResponseDto {

    private Long id;
    private UUID registrationCode;
    private Long eventId;
    private String eventTitle;
    private Long participantId;
    private String participantName;
    private String status;
    private LocalDateTime registeredAt;
    private LocalDateTime confirmedAt;
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

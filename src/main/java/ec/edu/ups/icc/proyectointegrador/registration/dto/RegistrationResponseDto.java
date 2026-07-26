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

    

}
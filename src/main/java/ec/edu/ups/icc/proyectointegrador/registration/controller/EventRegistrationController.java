package ec.edu.ups.icc.proyectointegrador.registration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.proyectointegrador.registration.dto.RegistrationResponseDto;
import ec.edu.ups.icc.proyectointegrador.registration.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Inscripciones a Eventos", description = "Endpoints para la inscripción de participantes en eventos académicos")
@RestController
@RequestMapping("/events/{eventId}/registrations")

public class EventRegistrationController {

    private final RegistrationService registrationService;

    public EventRegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Operation(summary = "Inscribirse a un evento", description = "Permite a un usuario con rol PARTICIPANT inscribirse en un evento publicado si hay cupos disponibles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Inscripción realizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "El evento no está disponible, ya finalizó, no hay cupos o ya existe una inscripción previa"),
        @ApiResponse(responseCode = "401", description = "No autorizado / Token inválido"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere rol PARTICIPANT)")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('PARTICIPANT')")
    public ResponseEntity<RegistrationResponseDto> register(
            @PathVariable Long eventId, Authentication authentication) {
        RegistrationResponseDto registration = registrationService.register(eventId, authentication.getName());
        return new ResponseEntity<>(registration, HttpStatus.CREATED);
    }
    
}

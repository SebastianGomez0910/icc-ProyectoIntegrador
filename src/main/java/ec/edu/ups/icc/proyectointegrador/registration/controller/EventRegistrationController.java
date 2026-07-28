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

@RestController
@RequestMapping("/events/{eventId}/registrations")

public class EventRegistrationController {

    private final RegistrationService registrationService;

    public EventRegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PARTICIPANT')")
    public ResponseEntity<RegistrationResponseDto> register(
            @PathVariable Long eventId, Authentication authentication) {
        RegistrationResponseDto registration = registrationService.register(eventId, authentication.getName());
        return new ResponseEntity<>(registration, HttpStatus.CREATED);
    }
    
}

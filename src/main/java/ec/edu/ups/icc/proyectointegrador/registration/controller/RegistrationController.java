package ec.edu.ups.icc.proyectointegrador.registration.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.proyectointegrador.registration.dto.RegistrationResponseDto;
import ec.edu.ups.icc.proyectointegrador.registration.service.RegistrationService;

public class RegistrationController {
    
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/my-registrations")
    @PreAuthorize("hasAuthority('PARTICIPANT')")
    public ResponseEntity<Page<RegistrationResponseDto>> getMyRegistrations(
            Pageable pageable, Authentication authentication) {
        Page<RegistrationResponseDto> registrations =
                registrationService.getMyRegistrations(authentication.getName(), pageable);
        return ResponseEntity.ok(registrations);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PARTICIPANT')")
    public ResponseEntity<Void> cancel(@PathVariable Long id, Authentication authentication) {
        registrationService.cancel(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

}

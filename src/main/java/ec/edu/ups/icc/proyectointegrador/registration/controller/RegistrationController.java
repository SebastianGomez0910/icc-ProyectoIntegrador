package ec.edu.ups.icc.proyectointegrador.registration.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.proyectointegrador.registration.dto.RegistrationResponseDto;
import ec.edu.ups.icc.proyectointegrador.registration.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Inscripciones", description = "Gestión de consultas y cancelación de inscripciones del participante")
@RestController
public class RegistrationController {
    
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Operation(summary = "Ver mis inscripciones", description = "Permite al participante autenticado consultar el listado paginado de los eventos a los que se ha inscrito.")
    @GetMapping("/my-registrations")
    @PreAuthorize("hasAuthority('PARTICIPANT')")
    public ResponseEntity<Page<RegistrationResponseDto>> getMyRegistrations(
            Pageable pageable, Authentication authentication) {
        Page<RegistrationResponseDto> registrations =
                registrationService.getMyRegistrations(authentication.getName(), pageable);
        return ResponseEntity.ok(registrations);
    }

    @Operation(summary = "Cancelar inscripción", description = "Permite al participante cancelar una inscripción activa de la cual es propietario.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PARTICIPANT')")
    public ResponseEntity<Void> cancel(@PathVariable Long id, Authentication authentication) {
        registrationService.cancel(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}

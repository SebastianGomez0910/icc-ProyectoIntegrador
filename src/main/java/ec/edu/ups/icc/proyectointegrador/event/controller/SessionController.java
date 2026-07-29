package ec.edu.ups.icc.proyectointegrador.event.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.proyectointegrador.common.exception.domain.ResourceNotFoundException;
import ec.edu.ups.icc.proyectointegrador.event.dtos.SessionRequestDto;
import ec.edu.ups.icc.proyectointegrador.event.dtos.SessionResponseDto;
import ec.edu.ups.icc.proyectointegrador.event.service.EventService;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import ec.edu.ups.icc.proyectointegrador.user.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Sesiones", description = "Gestión de las sesiones pertenecientes a los eventos")
@RestController
@RequestMapping("/sessions")
public class SessionController {
    
    private final EventService eventService;
    private final UserRepository userRepository;

    public SessionController(EventService eventService, UserRepository userRepository) {
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Listar sesiones de un evento", description = "Obtiene todas las sesiones ordenadas cronológicamente para un evento específico.")
    @GetMapping("/events/{eventId}/sessions")
    public ResponseEntity<List<SessionResponseDto>> getEventSessions(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventSessions(eventId));
    }

    @Operation(summary = "Actualizar sesión", description = "Modifica los detalles de una sesión. Solo el organizador propietario puede realizar esta acción.")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ORGANIZER')")
    @PutMapping("/sessions/{id}")
    public ResponseEntity<SessionResponseDto> updateSession(
            @PathVariable Long id, 
            @Valid @RequestBody SessionRequestDto request, 
            Authentication authentication) {
        
        String email = authentication.getName();
        
        User organizer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado"));
        Long organizerId = organizer.getId(); 
        
        SessionResponseDto response = eventService.updateSession(id, request, organizerId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar sesión", description = "Elimina permanentemente una sesión. Solo el organizador propietario puede realizar esta acción.")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ORGANIZER')")
    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable Long id, 
            Authentication authentication) {
        
        String email = authentication.getName();
        User organizer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado"));
        Long organizerId = organizer.getId();
        
        eventService.deleteSession(id, organizerId);
        return ResponseEntity.noContent().build();
    }
}

package ec.edu.ups.icc.proyectointegrador.event.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ec.edu.ups.icc.proyectointegrador.event.dtos.EventRequestDto;
import ec.edu.ups.icc.proyectointegrador.event.dtos.EventResponseDto;
import ec.edu.ups.icc.proyectointegrador.event.dtos.SessionRequestDto;
import ec.edu.ups.icc.proyectointegrador.event.dtos.SessionResponseDto;
import ec.edu.ups.icc.proyectointegrador.event.service.EventService;
import ec.edu.ups.icc.proyectointegrador.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Eventos", description = "Endpoints para la gestión, publicación y administración de eventos académicos y sus sesiones")
@RestController
@RequestMapping("/events")
public class EventController {
    
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Crear nuevo evento", description = "Registra un nuevo evento académico en el sistema. El usuario autenticado será asignado automáticamente como el organizador.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evento creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del evento inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado (Falta token JWT)")
    })
    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto request, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long organizerId = userDetails.getId();
        EventResponseDto response = eventService.createEvent(request, organizerId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener evento por ID", description = "Devuelve los detalles completos de un evento específico, incluyendo sus categorías y sesiones.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento encontrado"),
        @ApiResponse(responseCode = "404", description = "El evento no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
        
        EventResponseDto response = eventService.getEventById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar evento", description = "Modifica los datos de un evento existente. Solo el organizador original del evento tiene permiso para realizar esta acción.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado (El usuario no es el organizador)"),
        @ApiResponse(responseCode = "404", description = "El evento no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable Long id,
            @RequestBody EventRequestDto request,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long organizerId = userDetails.getId();

        EventResponseDto response = eventService.updateEvent(id, request, organizerId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar evento", description = "Cancela y elimina un evento del sistema. Solo el organizador original puede realizar esta acción.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Evento eliminado exitosamente (No Content)"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado (El usuario no es el organizador)"),
        @ApiResponse(responseCode = "404", description = "El evento no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long organizerId = userDetails.getId();

        eventService.deleteEvent(id, organizerId);
        return ResponseEntity.noContent().build();
    } 

    @Operation(summary = "Añadir sesión a un evento", description = "Crea una nueva sesión (ej. un taller, una charla) y la asocia a un evento existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sesión añadida exitosamente al evento"),
        @ApiResponse(responseCode = "400", description = "Conflicto de horarios o datos inválidos"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado (El usuario no es el organizador del evento)"),
        @ApiResponse(responseCode = "404", description = "El evento principal no existe")
    })
    @PostMapping("/{eventId}/sessions")
    public ResponseEntity<SessionResponseDto> addSessionToEvent(@PathVariable Long eventId, @RequestBody SessionRequestDto request, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long organizerId = userDetails.getId();
        SessionResponseDto response = eventService.addSessionToEvent(eventId, request, organizerId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Explorar eventos públicos", description = "Devuelve una lista paginada de los eventos disponibles. Permite filtrar por título, categoría o modalidad.")
    @ApiResponse(responseCode = "200", description = "Página de eventos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<Page<EventResponseDto>> getPublicEvents(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String modality,
            Pageable pageable) {
        
        Page<EventResponseDto> response = eventService.getPublicEvents(title, categoryId, modality, pageable);
        
        return ResponseEntity.ok(response);
    }
}

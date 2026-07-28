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

@RestController
@RequestMapping("/events")
public class EventController {
    
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto request, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long organizerId = userDetails.getId();
        EventResponseDto response = eventService.createEvent(request, organizerId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
        
        EventResponseDto response = eventService.getEventById(id);
        return ResponseEntity.ok(response);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long organizerId = userDetails.getId();

        eventService.deleteEvent(id, organizerId);
        return ResponseEntity.noContent().build();
    } 

    @PostMapping("/{eventId}/sessions")
    public ResponseEntity<SessionResponseDto> addSessionToEvent(@PathVariable Long eventId, @RequestBody SessionRequestDto request, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long organizerId = userDetails.getId();
        SessionResponseDto response = eventService.addSessionToEvent(eventId, request, organizerId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

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

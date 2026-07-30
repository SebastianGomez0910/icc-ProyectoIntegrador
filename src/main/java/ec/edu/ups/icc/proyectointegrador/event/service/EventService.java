package ec.edu.ups.icc.proyectointegrador.event.service;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;

import ec.edu.ups.icc.proyectointegrador.event.dtos.EventRequestDto;
import ec.edu.ups.icc.proyectointegrador.event.dtos.EventResponseDto;
import ec.edu.ups.icc.proyectointegrador.event.dtos.SessionRequestDto;
import ec.edu.ups.icc.proyectointegrador.event.dtos.SessionResponseDto;

public interface EventService {
    
    EventResponseDto createEvent(EventRequestDto request, Long organizerId);
    EventResponseDto getEventById(Long id);
    Page<EventResponseDto> getPublicEvents(String title, Long categoryId, String modality, Pageable pageable);
    void deleteEvent(Long id, Long organizerId);

    SessionResponseDto addSessionToEvent(Long eventId, SessionRequestDto request, Long organizerId);

    List<SessionResponseDto> getEventSessions(Long eventId);
    SessionResponseDto updateSession(Long sessionId, SessionRequestDto request, Long organizerId);
    void deleteSession(Long sessionId, Long organizerId);
    EventResponseDto updateEventStatus(Long id, String status, Long organizerId);
}

package ec.edu.ups.icc.proyectointegrador.event.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import ec.edu.ups.icc.proyectointegrador.category.entity.Category;
import ec.edu.ups.icc.proyectointegrador.category.repository.CategoryRepository;
import ec.edu.ups.icc.proyectointegrador.common.exception.domain.BusinessRuleException;
import ec.edu.ups.icc.proyectointegrador.common.exception.domain.ForbiddenOperationException;
import ec.edu.ups.icc.proyectointegrador.common.exception.domain.ResourceNotFoundException;
import ec.edu.ups.icc.proyectointegrador.event.dtos.EventRequestDto;
import ec.edu.ups.icc.proyectointegrador.event.dtos.EventResponseDto;
import ec.edu.ups.icc.proyectointegrador.event.dtos.SessionRequestDto;
import ec.edu.ups.icc.proyectointegrador.event.dtos.SessionResponseDto;
import ec.edu.ups.icc.proyectointegrador.event.entity.Event;
import ec.edu.ups.icc.proyectointegrador.event.entity.Session;
import ec.edu.ups.icc.proyectointegrador.event.mapper.EventMapper;
import ec.edu.ups.icc.proyectointegrador.event.mapper.SessionMapper;
import ec.edu.ups.icc.proyectointegrador.event.repositories.EventRepository;
import ec.edu.ups.icc.proyectointegrador.event.repositories.SessionRepository;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import ec.edu.ups.icc.proyectointegrador.user.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final SessionRepository sessionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final SessionMapper sessionMapper;

    public EventServiceImpl(EventRepository eventRepository, 
                            SessionRepository sessionRepository,
                            CategoryRepository categoryRepository, 
                            UserRepository userRepository, 
                            EventMapper eventMapper,
                            SessionMapper sessionMapper) {
        this.eventRepository = eventRepository;
        this.sessionRepository = sessionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.eventMapper = eventMapper;
        this.sessionMapper = sessionMapper;
    }

    @Override
    @Transactional
    public EventResponseDto createEvent(EventRequestDto request, Long organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new ResourceNotFoundException("Organizador no encontrado"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
        validateEventDates(request);
        Event event = eventMapper.toEntity(request, organizer, category);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDto(savedEvent, null);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponseDto getEventById(Long id) {
        Event event = eventRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado o eliminado"));
        List<Session> sessions = sessionRepository.findByEventIdOrderByStartAtAsc(id);
        return eventMapper.toDto(event, sessions);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDto> getPublicEvents(String title, Long categoryId, String modality, Pageable pageable) {
        Page<Event> events;
        String status = "PUBLISHED";
        if (title != null && !title.isEmpty()) {
            events = eventRepository.findByTitleContainingIgnoreCaseAndStatusAndDeletedFalse(title, status, pageable);
        } else if (categoryId != null) {
            events = eventRepository.findByCategoryIdAndStatusAndDeletedFalse(categoryId, status, pageable);
        } else if (modality != null && !modality.isEmpty()) {
            events = eventRepository.findByModalityAndStatusAndDeletedFalse(modality, status, pageable);
        } else {
            events = eventRepository.findByStatusAndDeletedFalse(status, pageable);
        }
        return events.map(event -> eventMapper.toDto(event, null));
    }

    @Override
    @Transactional
    public void deleteEvent(Long id, Long organizerId) {
        Event event = eventRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado o ya eliminado"));
        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new ForbiddenOperationException("No tienes permisos para eliminar este evento");
        }
        event.setDeleted(true);
        eventRepository.save(event);
    }
    
    @Override
    @Transactional
    public SessionResponseDto addSessionToEvent(Long eventId, SessionRequestDto request, Long organizerId) {
        Event event = eventRepository.findByIdAndDeletedFalse(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado o eliminado"));
        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new ForbiddenOperationException("No tienes permisos para agregar sesiones a este evento");
        }
        if (request.getStartAt().isAfter(request.getEndAt())) {
            throw new BusinessRuleException("El inicio de la sesión debe ser anterior a su finalización.");
        }
        if (request.getStartAt().isBefore(event.getStartAt()) || request.getEndAt().isAfter(event.getEndAt())) {
            throw new BusinessRuleException("Las fechas de la sesión deben estar dentro de las fechas del evento principal.");
        }
        Session session = new Session();
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setStartAt(request.getStartAt());
        session.setEndAt(request.getEndAt());
        session.setLocation(request.getLocation());
        session.setVirtualUrl(request.getVirtualUrl());
        session.setEvent(event);

        Session savedSession = sessionRepository.save(session);

        SessionResponseDto responseDto = new SessionResponseDto();
        responseDto.setId(savedSession.getId());
        responseDto.setTitle(savedSession.getTitle());
        responseDto.setStartAt(savedSession.getStartAt());
        responseDto.setEndAt(savedSession.getEndAt());
        responseDto.setLocation(savedSession.getLocation());
        responseDto.setVirtualUrl(savedSession.getVirtualUrl());

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponseDto> getEventSessions(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Evento no encontrado");
        }
        List<Session> sessions = sessionRepository.findByEventIdOrderByStartAtAsc(eventId);
        
        return sessions.stream().map(sessionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SessionResponseDto updateSession(Long sessionId, SessionRequestDto request, Long organizerId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada"));
        Event event = session.getEvent();

        //Validar propiedad del evento
        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new ForbiddenOperationException("No tienes permisos para editar las sesiones de este evento");
        }
        
        //Validar coherencia de fechas
        if (request.getStartAt().isAfter(request.getEndAt())) {
            throw new BusinessRuleException("El inicio de la sesión debe ser anterior a su finalización.");
        }
        if (request.getStartAt().isBefore(event.getStartAt()) || request.getEndAt().isAfter(event.getEndAt())) {
            throw new BusinessRuleException("Las fechas de la sesión deben estar estrictamente dentro de las fechas del evento principal.");
        }

       session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setStartAt(request.getStartAt());
        session.setEndAt(request.getEndAt());
        session.setLocation(request.getLocation());
        session.setVirtualUrl(request.getVirtualUrl());

        Session updatedSession = sessionRepository.save(session);
        
        return sessionMapper.toDto(updatedSession);
    }

    @Override
    @Transactional
    public void deleteSession(Long sessionId, Long organizerId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada"));
                
        if (!session.getEvent().getOrganizer().getId().equals(organizerId)) {
            throw new ForbiddenOperationException("No tienes permisos para eliminar las sesiones de este evento");
        }
        
        sessionRepository.delete(session);
    }

    private void validateEventDates(EventRequestDto request) {
        Instant now = Instant.now();

        if (request.getRegistrationStartAt() != null && now.isAfter(request.getRegistrationStartAt())) {
            throw new BusinessRuleException("El periodo de inscripción no puede ser en el pasado.");
        }
        if (request.getRegistrationStartAt() != null && request.getRegistrationEndAt() != null &&
            request.getRegistrationStartAt().isAfter(request.getRegistrationEndAt())) {
            throw new BusinessRuleException("El inicio de inscripción debe ser antes del fin de inscripción.");
        }
        if (request.getRegistrationEndAt() != null && request.getStartAt() != null &&
            request.getRegistrationEndAt().isAfter(request.getStartAt())) {
            throw new BusinessRuleException("Las inscripciones deben cerrar antes de que inicie el evento.");
        }
        if (request.getStartAt() != null && request.getEndAt() != null &&
            request.getStartAt().isAfter(request.getEndAt())) {
            throw new BusinessRuleException("La fecha de inicio del evento debe ser anterior a la de finalización.");
        }
    }

   @Override
    @Transactional
    public EventResponseDto updateEventStatus(Long id, String status, Long organizerId) {
        Event event = eventRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado o eliminado"));
        
        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new ForbiddenOperationException("No tienes permisos para modificar el estado de este evento");
        }

        // Validar opcionalmente que el status enviado sea válido (ej. DRAFT, PUBLISHED, etc.)
        event.setStatus(status);

        Event updatedEvent = eventRepository.save(event);
        List<Session> sessions = sessionRepository.findByEventIdOrderByStartAtAsc(id);
        return eventMapper.toDto(updatedEvent, sessions);
    }
}
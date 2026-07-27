package ec.edu.ups.icc.proyectointegrador.registration.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.edu.ups.icc.proyectointegrador.audit.entity.AuditLog;
import ec.edu.ups.icc.proyectointegrador.audit.repositories.AuditLogRepository;
import ec.edu.ups.icc.proyectointegrador.common.exception.BusinessRuleException;
import ec.edu.ups.icc.proyectointegrador.common.exception.ForbiddenOperationException;
import ec.edu.ups.icc.proyectointegrador.common.exception.ResourceNotFoundException;
import ec.edu.ups.icc.proyectointegrador.event.entity.Event;
import ec.edu.ups.icc.proyectointegrador.event.repositories.EventRepository;
import ec.edu.ups.icc.proyectointegrador.registration.dto.RegistrationResponseDto;
import ec.edu.ups.icc.proyectointegrador.registration.entity.Registration;
import ec.edu.ups.icc.proyectointegrador.registration.repositories.RegistrationRepository;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import ec.edu.ups.icc.proyectointegrador.user.repositories.UserRepository;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final List<String> ACTIVE_STATUSES = List.of("PENDING", "CONFIRMED");
    private static final String STATUS_CONFIRMED = "CONFIRMED";
    private static final String STATUS_CANCELLED = "CANCELLED";
    private static final String EVENT_STATUS_PUBLISHED = "PUBLISHED";

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository,
            EventRepository eventRepository,
            UserRepository userRepository,
            AuditLogRepository auditLogRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Transactional
    public RegistrationResponseDto register(Long eventId, String participantEmail) {
        User participant = getUserByEmail(participantEmail);

        Event event = eventRepository.findByIdForUpdate(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con ID: " + eventId));

        validateEventIsRegistrable(event);

        boolean alreadyRegistered = registrationRepository.existsByEventIdAndParticipantIdAndStatusIn(
                eventId, participant.getId(), ACTIVE_STATUSES);
        if (alreadyRegistered) {
            throw new BusinessRuleException("Ya existe una inscripción activa de este participante en el evento.");
        }

        if (event.getAvailableCapacity() == null || event.getAvailableCapacity() <= 0) {
            throw new BusinessRuleException("No hay cupos disponibles para este evento.");
        }

        event.setAvailableCapacity(event.getAvailableCapacity() - 1);
        eventRepository.save(event);

        Registration registration = new Registration();
        registration.setEvent(event);
        registration.setParticipant(participant);
        registration.setStatus(STATUS_CONFIRMED);
        registration.setConfirmedAt(LocalDateTime.now());

        Registration saved = registrationRepository.save(registration);

        logAudit(participant, "REGISTRATION_CREATED", saved.getId(), "SUCCESS");

        return toDto(saved);
    }

    @Override
    @Transactional
    public void cancel(Long registrationId, String participantEmail) {
        User participant = getUserByEmail(participantEmail);

        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inscripción no encontrada con ID: " + registrationId));

        if (!registration.getParticipant().getId().equals(participant.getId())) {
            throw new ForbiddenOperationException("No puede cancelar una inscripción que no le pertenece.");
        }

        if (STATUS_CANCELLED.equalsIgnoreCase(registration.getStatus())) {
            throw new BusinessRuleException("La inscripción ya se encuentra cancelada.");
        }

        Event event = eventRepository.findByIdForUpdate(registration.getEvent().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado."));

        registration.setStatus(STATUS_CANCELLED);
        registration.setCancelledAt(LocalDateTime.now());
        registrationRepository.save(registration);

        Integer currentCapacity = event.getAvailableCapacity() != null ? event.getAvailableCapacity() : 0;
        int updatedCapacity = Math.min(currentCapacity + 1, event.getCapacity());
        event.setAvailableCapacity(updatedCapacity);
        eventRepository.save(event);

        logAudit(participant, "REGISTRATION_CANCELLED", registration.getId(), "SUCCESS");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegistrationResponseDto> getMyRegistrations(String participantEmail, Pageable pageable) {
        User participant = getUserByEmail(participantEmail);
        return registrationRepository.findByParticipantId(participant.getId(), pageable)
                .map(this::toDto);
    }

    private void validateEventIsRegistrable(Event event) {
        if (Boolean.TRUE.equals(event.getDeleted())) {
            throw new ResourceNotFoundException("Evento no encontrado.");
        }

        if (!EVENT_STATUS_PUBLISHED.equalsIgnoreCase(event.getStatus())) {
            throw new BusinessRuleException("El evento no está publicado y no admite inscripciones.");
        }

        Instant now = Instant.now();

        if (event.getRegistrationStartAt() != null && now.isBefore(event.getRegistrationStartAt())) {
            throw new BusinessRuleException("El periodo de inscripción para este evento todavía no inicia.");
        }
        if (event.getRegistrationEndAt() != null && now.isAfter(event.getRegistrationEndAt())) {
            throw new BusinessRuleException("El periodo de inscripción para este evento ya finalizó.");
        }
        if (event.getEndAt() != null && now.isAfter(event.getEndAt())) {
            throw new BusinessRuleException("El evento ya finalizó.");
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado."));
    }

    private void logAudit(User actor, String action, Long registrationId, String result) {
        
        try {
            AuditLog log = new AuditLog(actor, action, "REGISTRATION", registrationId,
                    null, null, result, null, null, null, null);
            auditLogRepository.save(log);
        } catch (Exception ex) {
            
        }
    }

    private RegistrationResponseDto toDto(Registration r) {
        User participant = r.getParticipant();
        Event event = r.getEvent();
        String fullName = participant.getFirstName() + " " + participant.getLastName();

        return new RegistrationResponseDto(
                r.getId(),
                r.getRegistrationCode(),
                event.getId(),
                event.getTitle(),
                participant.getId(),
                fullName,
                r.getStatus(),
                r.getRegisteredAt(),
                r.getConfirmedAt(),
                r.getCancelledAt());
    }
}

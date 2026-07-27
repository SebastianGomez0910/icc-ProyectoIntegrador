package ec.edu.ups.icc.proyectointegrador.event.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ec.edu.ups.icc.proyectointegrador.category.entity.Category;
import ec.edu.ups.icc.proyectointegrador.event.dtos.EventRequestDto;
import ec.edu.ups.icc.proyectointegrador.event.dtos.EventResponseDto;
import ec.edu.ups.icc.proyectointegrador.event.dtos.SessionResponseDto;
import ec.edu.ups.icc.proyectointegrador.event.entity.Event;
import ec.edu.ups.icc.proyectointegrador.event.entity.Session;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;

@Component
public class EventMapper {
    
    public Event toEntity(EventRequestDto dto, User organizer, Category category) {
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setModality(dto.getModality());
        event.setLocation(dto.getLocation());
        event.setVirtualUrl(dto.getVirtualUrl());
        event.setCapacity(dto.getCapacity());
        event.setAvailableCapacity(dto.getCapacity()); 
        event.setRegistrationStartAt(dto.getRegistrationStartAt());
        event.setRegistrationEndAt(dto.getRegistrationEndAt());
        event.setStartAt(dto.getStartAt());
        event.setEndAt(dto.getEndAt());
        event.setOrganizer(organizer);
        event.setCategory(category);
        return event;
    }

    public EventResponseDto toDto(Event event, List<Session> sessions) {
         EventResponseDto dto = new EventResponseDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setModality(event.getModality());
        dto.setLocation(event.getLocation());
        dto.setVirtualUrl(event.getVirtualUrl());
        dto.setCapacity(event.getCapacity());
        dto.setAvailableCapacity(event.getAvailableCapacity());
        dto.setStartAt(event.getStartAt());
        dto.setEndAt(event.getEndAt());
        dto.setStatus(event.getStatus());

        dto.setOrganizerName(event.getOrganizer().getFirstName() + " " + event.getOrganizer().getLastName());
        dto.setCategoryName(event.getCategory().getName());
        if (sessions != null && !sessions.isEmpty()) {
            List<SessionResponseDto> sessionDtos = sessions.stream().map(session -> {
                SessionResponseDto sDto = new SessionResponseDto();
                sDto.setId(session.getId());
                sDto.setTitle(session.getTitle());
                sDto.setStartAt(session.getStartAt());
                sDto.setEndAt(session.getEndAt());
                sDto.setLocation(session.getLocation());
                sDto.setVirtualUrl(session.getVirtualUrl());
                return sDto;
            }).collect(Collectors.toList());
            
            dto.setSessions(sessionDtos);
        }

        return dto;
    }
}
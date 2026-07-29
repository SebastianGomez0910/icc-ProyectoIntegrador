package ec.edu.ups.icc.proyectointegrador.event.mapper;

import org.springframework.stereotype.Component;

import ec.edu.ups.icc.proyectointegrador.event.dtos.SessionResponseDto;
import ec.edu.ups.icc.proyectointegrador.event.entity.Session;

@Component
public class SessionMapper {
    
    public SessionResponseDto toDto(Session session) {
        if (session == null) {
            return null;
        }
        
        SessionResponseDto dto = new SessionResponseDto();
        dto.setId(session.getId());
        dto.setTitle(session.getTitle());
        dto.setStartAt(session.getStartAt());
        dto.setEndAt(session.getEndAt());
        dto.setLocation(session.getLocation());
        dto.setVirtualUrl(session.getVirtualUrl());
        return dto;
    }
}


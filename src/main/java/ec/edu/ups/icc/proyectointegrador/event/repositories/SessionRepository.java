package ec.edu.ups.icc.proyectointegrador.event.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.proyectointegrador.event.entity.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    
    List<Session> findByEventIdOrderByStartAtAsc(Long eventId);
}

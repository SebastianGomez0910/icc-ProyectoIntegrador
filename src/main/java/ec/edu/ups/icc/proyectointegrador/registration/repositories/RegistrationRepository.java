package ec.edu.ups.icc.proyectointegrador.registration.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.proyectointegrador.registration.entity.Registration;


@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    Page<Registration> findByParticipantId(Long participantId, Pageable pageable);

    List<Registration> findByEventIdAndStatusIn(Long eventId, List<String> statuses);

    boolean existsByEventIdAndParticipantIdAndStatusIn(Long eventId, Long participantId, List<String> statuses);
}

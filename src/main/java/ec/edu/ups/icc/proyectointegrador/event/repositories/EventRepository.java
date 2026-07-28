package ec.edu.ups.icc.proyectointegrador.event.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.proyectointegrador.event.entity.Event;
import jakarta.persistence.LockModeType;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Event e where e.id = :id")
    Optional<Event> findByIdForUpdate(@Param("id") Long id);

    Optional <Event> findByIdAndDeletedFalse(Long id);
    Page<Event> findByStatusAndDeletedFalse(String status, Pageable pageable);
    Page<Event> findByTitleContainingIgnoreCaseAndStatusAndDeletedFalse(String title, String status, Pageable pageable);
    Page<Event> findByOrganizerIdAndDeletedFalse(Long organizerId, Pageable pageable);

    Page<Event> findByCategoryIdAndStatusAndDeletedFalse(Long categoryId, String status, Pageable pageable);
        Page<Event> findByModalityAndStatusAndDeletedFalse(String modality, String status, Pageable pageable);

}

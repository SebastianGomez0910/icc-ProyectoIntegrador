package ec.edu.ups.icc.proyectointegrador.user.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.proyectointegrador.user.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT r FROM Role r WHERE UPPER(TRIM(r.name)) = UPPER(TRIM(:name))")
    Optional<Role> findByNameIgnoreCase(@Param("name") String name);
}
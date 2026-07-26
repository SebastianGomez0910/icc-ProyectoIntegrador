package ec.edu.ups.icc.proyectointegrador.user.service;

import ec.edu.ups.icc.proyectointegrador.user.dto.UpdateUserDto;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<User> findAllUsers(Pageable pageable);
    User findUserById(Long id);
    User updateUser(Long id, UpdateUserDto userDto);
    void assignRoleToUser(Long userId, Long roleId);
}
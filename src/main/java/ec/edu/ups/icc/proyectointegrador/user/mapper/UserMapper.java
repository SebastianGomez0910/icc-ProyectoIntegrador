package ec.edu.ups.icc.proyectointegrador.user.mapper;

import ec.edu.ups.icc.proyectointegrador.user.dto.UpdateUserDto;
import ec.edu.ups.icc.proyectointegrador.user.dto.UserResponseDto;
import ec.edu.ups.icc.proyectointegrador.user.entity.Role;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponseDto toResponseDto(User user) {
        if (user == null) {
            return null;
        }

        Set<String> roleNames = user.getRoles() != null
                ? user.getRoles().stream()
                      .map(Role::getName)
                      .collect(Collectors.toSet())
                : Set.of();

        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getStatus(),
                roleNames
        );
    }

    public void updateEntityFromDto(UpdateUserDto dto, User user) {
        if (dto == null || user == null) {
            return;
        }
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        if (dto.getStatus() != null) {
            user.setStatus(dto.getStatus());
        }
    }
}
package ec.edu.ups.icc.proyectointegrador.user.controllers;

import ec.edu.ups.icc.proyectointegrador.user.dto.AssignRoleDto;
import ec.edu.ups.icc.proyectointegrador.user.dto.UpdateUserDto;
import ec.edu.ups.icc.proyectointegrador.user.dto.UserResponseDto;
import ec.edu.ups.icc.proyectointegrador.user.entity.Role;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import ec.edu.ups.icc.proyectointegrador.user.service.UserService;
import jakarta.validation.Valid;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(required = false) String search,
            Pageable pageable) {
            
        Page<User> users = userService.findAllUsers(pageable);
        
        Page<UserResponseDto> dtoPage = users.map(user -> new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getStatus(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        ));
        
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        
        UserResponseDto response = new UserResponseDto(
                user.getId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getStatus(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateUserDto userDetails) {
            
        User updatedUser = userService.updateUser(id, userDetails);
        
        UserResponseDto response = new UserResponseDto(
                updatedUser.getId(), updatedUser.getFirstName(), updatedUser.getLastName(),
                updatedUser.getEmail(), updatedUser.getStatus(),
                updatedUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<Void> assignRole(
            @PathVariable Long id, 
            @Valid @RequestBody AssignRoleDto request) { 
            
        userService.assignRoleToUser(id, request.getRoleId());
        return ResponseEntity.ok().build();
    }
}
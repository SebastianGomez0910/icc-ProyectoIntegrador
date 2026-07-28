package ec.edu.ups.icc.proyectointegrador.user.controllers;

import ec.edu.ups.icc.proyectointegrador.user.dto.AssignRoleDto;
import ec.edu.ups.icc.proyectointegrador.user.dto.UpdateUserDto;
import ec.edu.ups.icc.proyectointegrador.user.dto.UserResponseDto;
import ec.edu.ups.icc.proyectointegrador.user.entity.Role;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import ec.edu.ups.icc.proyectointegrador.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuarios", description = "Endpoints para la gestión de cuentas de usuario, actualización de perfiles y administración de roles del sistema")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Listar todos los usuarios", description = "Devuelve una lista paginada de los usuarios registrados en la plataforma. Permite filtrado opcional por término de búsqueda.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
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

    @Operation(summary = "Obtener usuario por ID", description = "Busca y devuelve la información pública detallada de un usuario específico en base a su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "El usuario con el ID proporcionado no existe")
    })
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

    @Operation(summary = "Actualizar información de usuario", description = "Modifica los datos personales de un usuario (como nombre o apellido).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "El usuario a actualizar no existe")
    })
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

    @Operation(summary = "Asignar rol a un usuario", description = "Otorga un nuevo rol de sistema a un usuario existente (por ejemplo, promover a ORGANIZADOR o ADMIN).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol asignado exitosamente"),
        @ApiResponse(responseCode = "400", description = "El usuario ya posee este rol o los datos son inválidos"),
        @ApiResponse(responseCode = "404", description = "El usuario o el rol especificado no existen")
    })
    @PostMapping("/{id}/roles")
    public ResponseEntity<Void> assignRole(
            @PathVariable Long id, 
            @Valid @RequestBody AssignRoleDto request) { 
            
        userService.assignRoleToUser(id, request.getRoleId());
        return ResponseEntity.ok().build();
    }
}
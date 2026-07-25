package ec.edu.ups.icc.proyectointegrador.user.controllers;

import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import ec.edu.ups.icc.proyectointegrador.user.service.UserService;
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
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.findAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<Void> assignRole(@PathVariable Long id, @RequestParam Long roleId) {
        userService.assignRoleToUser(id, roleId);
        return ResponseEntity.ok().build();
    }
}
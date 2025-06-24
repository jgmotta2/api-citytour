package br.edu.atitus.api_citytour.controllers;

import br.edu.atitus.api_citytour.dtos.PasswordUpdateDTO;
import br.edu.atitus.api_citytour.dtos.UserUpdateDTO;
import br.edu.atitus.api_citytour.entities.UserEntity;
import br.edu.atitus.api_citytour.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ws/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserEntity> getMyProfile() {
        UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userAuth);
    }

    @PutMapping("/me")
    public ResponseEntity<UserEntity> updateMyProfile(@RequestBody UserUpdateDTO dto) throws Exception {
        UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity userToUpdate = new UserEntity();
        userToUpdate.setId(userAuth.getId());
        userToUpdate.setName(dto.name());
        userToUpdate.setBirthDate(dto.birthDate());

        UserEntity updatedUser = userService.updateProfile(userAuth.getId(), userToUpdate);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/me/password")
    public ResponseEntity<String> updateMyPassword(@Valid @RequestBody PasswordUpdateDTO dto) throws Exception {
        UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.updatePassword(userAuth, dto.oldPassword(), dto.newPassword());
        return ResponseEntity.ok("Password updated successfully.");
    }

}

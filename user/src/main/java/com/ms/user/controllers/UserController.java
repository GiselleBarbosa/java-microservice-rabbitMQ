package com.ms.user.controllers;

import com.ms.user.dtos.UserRecordDto;
import com.ms.user.models.UserModel;
import com.ms.user.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        log.info("Encontrado(s) {} usuários", users.size());
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserRecordDto userRecordDTO) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDTO, userModel);
        UserModel savedUser = userService.save(userModel);
        log.info("Usuário com email {} foi registrado com sucesso!", savedUser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable UUID id) {
        UserModel userModel = userService.getUserById(id);
        log.info("Usuário com ID {} encontrado com sucesso!", id);
        return ResponseEntity.ok(userModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        userService.deleteById(id);
        String message = "Usuário com ID " + id + " foi removido com sucesso.";
        log.info("Usuário com ID {} deletado com sucesso!", id);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable UUID id, @RequestBody @Valid UserRecordDto userRecordDTO) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDTO, userModel);
        userService.updateUser(id, userModel);
        String message = "Usuário com ID " + id + " foi atualizado com sucesso!";
        log.info("Usuário com ID {} atualizado com sucesso", id);
        return ResponseEntity.ok(message);
    }
}

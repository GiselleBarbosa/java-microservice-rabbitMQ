package com.ms.user.controllers;

import com.ms.user.dtos.UserRecordDto;
import com.ms.user.exceptions.EmailAlreadyExistsException;
import com.ms.user.exceptions.UserNotFoundException;
import com.ms.user.models.UserModel;
import com.ms.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser() {
        UserRecordDto userRecordDto = new UserRecordDto("Test User", "test@example.com");
        UserModel savedUser = new UserModel(UUID.randomUUID(), "Test User", "test@example.com");

        when(userService.save(any(UserModel.class))).thenReturn(savedUser);

        ResponseEntity<UserModel> response = userController.saveUser(userRecordDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("test@example.com", response.getBody().getEmail());
        verify(userService, times(1)).save(any(UserModel.class));
    }

    @Test
    void getAllUsers() {
        List<UserModel> userList = new ArrayList<>();
        userList.add(new UserModel(UUID.randomUUID(), "User1", "user1@example.com"));
        userList.add(new UserModel(UUID.randomUUID(), "User2", "user2@example.com"));

        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<UserModel>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList.size(), response.getBody().size());
        assertEquals(userList.get(0), response.getBody().get(0));
        assertEquals(userList.get(1), response.getBody().get(1));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void updateUser() {
        UUID userId = UUID.randomUUID();
        UserRecordDto userRecordDto = new UserRecordDto("Updated User", "updated@example.com");

        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Usuário com ID " + userId + " foi atualizado com sucesso!");

        ResponseEntity<String> response = userController.updateUser(userId, userRecordDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(userService, times(1)).updateUser(eq(userId), any(UserModel.class));
    }

    @Test
    void getUserById() {
        UUID userId = UUID.randomUUID();
        UserModel user = new UserModel(userId, "Test User", "test@example.com");

        when(userService.getUserById(userId)).thenReturn(user);

        ResponseEntity<UserModel> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userId, response.getBody().getUserId());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void deleteUser() {
        UUID userId = UUID.randomUUID();
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Usuário com ID " + userId + " foi removido com sucesso.");

        ResponseEntity<String> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(userService, times(1)).deleteById(userId);
    }

    @Test
    void saveUser_EmailAlreadyExistsException() {
        UserRecordDto userRecordDto = new UserRecordDto("Test User", "existing@example.com");

        when(userService.save(any(UserModel.class))).thenThrow(new EmailAlreadyExistsException("existing@example.com"));

        ResponseEntity<UserModel> response = userController.saveUser(userRecordDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        assertTrue(response.toString().contains("Email já existe: existing@example.com"));
        verify(userService, times(1)).save(any(UserModel.class));
    }

    @Test
    void deleteUser_UserNotFoundException() {
        UUID userId = UUID.randomUUID();

        doThrow(new UserNotFoundException(userId)).when(userService).deleteById(userId);

        ResponseEntity<String> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Usuário não encontrado com ID: " + userId));
        verify(userService, times(1)).deleteById(userId);
    }

    @Test
    void updateUser_UserNotFoundException() {
        UUID userId = UUID.randomUUID();
        UserRecordDto userRecordDto = new UserRecordDto("Updated User", "updated@example.com");

        when(userService.updateUser(eq(userId), any(UserModel.class))).thenThrow(new UserNotFoundException(userId));

        ResponseEntity<String> response = userController.updateUser(userId, userRecordDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Usuário não encontrado com ID: " + userId));
        verify(userService, times(1)).updateUser(eq(userId), any(UserModel.class));
    }

    @Test
    void getUserById_UserNotFoundException() {
        UUID userId = UUID.randomUUID();

        when(userService.getUserById(userId)).thenThrow(new UserNotFoundException(userId));

        ResponseEntity<UserModel> response = userController.getUserById(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        assertTrue(response.toString().contains("Usuário não encontrado com ID: " + userId));

        verify(userService, times(1)).getUserById(userId);
    }
}

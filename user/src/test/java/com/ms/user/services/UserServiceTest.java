package com.ms.user.services;

import com.ms.user.exceptions.EmailAlreadyExistsException;
import com.ms.user.exceptions.UserNotFoundException;
import com.ms.user.models.UserModel;
import com.ms.user.producers.UserProducer;
import com.ms.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProducer userProducer;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_NewUser_Success() {
        UserModel newUser = new UserModel(UUID.randomUUID(), "Test User", "test@example.com");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(newUser)).thenReturn(newUser);

        UserModel savedUser = userService.save(newUser);

        assertEquals(newUser, savedUser);
        verify(userRepository, times(1)).findByEmail(newUser.getEmail());
        verify(userRepository, times(1)).save(newUser);
        verify(userProducer, times(1)).publishMessageEmail(newUser);
    }

    @Test
    void save_ExistingUser_ExceptionThrown() {
        UserModel existingUser = new UserModel(UUID.randomUUID(), "Test User", "test@example.com");

        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.save(existingUser));
        verify(userRepository, times(1)).findByEmail(existingUser.getEmail());
        verify(userRepository, never()).save(existingUser);
        verify(userProducer, never()).publishMessageEmail(existingUser);
    }

    @Test
    void getAllUsers() {
        List<UserModel> userList = new ArrayList<>();
        userList.add(new UserModel(UUID.randomUUID(), "User1", "user1@example.com"));
        userList.add(new UserModel(UUID.randomUUID(), "User2", "user2@example.com"));

        when(userRepository.findAll()).thenReturn(userList);

        List<UserModel> result = userService.getAllUsers();

        assertEquals(userList.size(), result.size());
        assertEquals(userList.get(0), result.get(0));
        assertEquals(userList.get(1), result.get(1));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ExistingUser_Success() {
        UUID userId = UUID.randomUUID();
        UserModel existingUser = new UserModel(userId, "Test User", "test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        UserModel result = userService.getUserById(userId);

        assertEquals(existingUser, result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_NonExistingUser_ExceptionThrown() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void deleteById_ExistingUser_Success() {
        UUID userId = UUID.randomUUID();

        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteById(userId);

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteById_NonExistingUser_ExceptionThrown() {
        UUID userId = UUID.randomUUID();

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteById(userId));
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void updateUser_ExistingUser_Success() {
        UUID userId = UUID.randomUUID();
        UserModel userDetails = new UserModel(userId, "Updated User", "updated@example.com");
        UserModel existingUser = new UserModel(userId, "Test User", "test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserModel updatedUser = userService.updateUser(userId, userDetails);

        assertEquals(userDetails.getName(), updatedUser.getName());
        assertEquals(userDetails.getEmail(), updatedUser.getEmail());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_NonExistingUser_ExceptionThrown() {
        UUID userId = UUID.randomUUID();
        UserModel userDetails = new UserModel(userId, "Updated User", "updated@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, userDetails));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(UserModel.class));
    }
}

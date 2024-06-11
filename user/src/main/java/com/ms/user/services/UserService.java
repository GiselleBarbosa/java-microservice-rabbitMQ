package com.ms.user.services;

import com.ms.user.exceptions.EmailAlreadyExistsException;
import com.ms.user.exceptions.UserNotFoundException;
import com.ms.user.models.UserModel;
import com.ms.user.producers.UserProducer;
import com.ms.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private UserProducer userProducer;

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserModel save(UserModel userModel) {
        Optional<UserModel> existingUser = userRepository.findByEmail(userModel.getEmail());
        if (existingUser.isPresent()) {
            userProducer.publishMessageEmail(userModel);
            throw new EmailAlreadyExistsException(userModel.getEmail());
        }

        return userRepository.save(userModel);
    }

    public UserModel getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional
    public void deleteById(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public UserModel updateUser(UUID userId, UserModel userDetails) {
        UserModel existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());

        return userRepository.save(existingUser);
    }
}

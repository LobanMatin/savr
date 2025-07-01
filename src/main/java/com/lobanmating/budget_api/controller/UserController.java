package com.lobanmating.budget_api.controller;

import com.lobanmating.budget_api.dto.UserRequest;
import com.lobanmating.budget_api.exception.UserNotFoundException;
import com.lobanmating.budget_api.model.User;
import com.lobanmating.budget_api.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest userRequest) {
        User user = User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(savedUser);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    // TODO: Remove delete all functionality once testing is finished
    @DeleteMapping("/all")
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}

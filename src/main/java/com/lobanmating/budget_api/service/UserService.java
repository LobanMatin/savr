package com.lobanmating.budget_api.service;
import com.lobanmating.budget_api.dto.UserRequest;
import com.lobanmating.budget_api.exception.EmailAlreadyExistsException;
import com.lobanmating.budget_api.exception.UserNotFoundException;
import com.lobanmating.budget_api.model.User;
import com.lobanmating.budget_api.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}


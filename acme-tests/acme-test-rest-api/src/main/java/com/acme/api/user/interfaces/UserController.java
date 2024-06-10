package com.acme.api.user.interfaces;

import com.acme.api.user.domain.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserRepresentation> searchUsers() {
        return userRepository.findAll().stream()
                .map(UserRepresentation::fromDomain)
                .toList();
    }

    @PostMapping
    public void createUser(@RequestBody UserRepresentation newUser) {
        userRepository.save(newUser.toDomain());
    }
}

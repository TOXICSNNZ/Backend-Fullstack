package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(
            String firstName,
            String lastName,
            String email,
            String street,
            String region,
            String comuna,
            String password,
            String role
    ) {

        if (role == null || (!role.equals("USER") && !role.equals("ADMIN"))) {
            role = "USER";
        }

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .street(street)
                .region(region)
                .comuna(comuna)
                .username(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();

        return userRepository.save(user);
    }

    public User register(String email, String password) {
        return register(
                "Nombre",
                "Apellido",
                email,
                "Calle por definir",
                "Región por definir",
                "Comuna por definir",
                password,
                "USER"
        );
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

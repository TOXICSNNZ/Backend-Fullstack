package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.security.jwt.JwtService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager authManager, JwtService jwtService, UserService userService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            String firstName = body.get("firstName");
            String lastName  = body.get("lastName");
            String email     = body.get("email");
            String street    = body.get("street");
            String region    = body.get("region");
            String comuna    = body.get("comuna");
            String password  = body.get("password");
            String role      = body.getOrDefault("role", "USER");

            if (email == null || password == null ||
                    email.isBlank() || password.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email y password son requeridos"));
            }

            if (firstName == null || lastName == null ||
                    street == null || region == null || comuna == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Faltan datos obligatorios del usuario"));
            }

            userService.register(
                    firstName,
                    lastName,
                    email,
                    street,
                    region,
                    comuna,
                    password,
                    role
            );

            return ResponseEntity.ok(Map.of(
                    "message", "Usuario registrado correctamente",
                    "role", role
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El usuario ya existe o hubo un problema al registrar"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");

            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            if (auth.isAuthenticated()) {
                User user = userService.findByUsername(email)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                String token = jwtService.generateToken(email, user.getRole());

                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "username", email,
                        "role", user.getRole()
                ));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas"));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario o contraseña incorrectos"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar la solicitud"));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken() {
        return ResponseEntity.ok(Map.of("valid", true));
    }
}

package com.prosperi.argeo.auth.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import com.prosperi.argeo.auth.dto.LoginRequest;
import com.prosperi.argeo.auth.dto.LoginResponse;
import com.prosperi.argeo.auth.dto.RegisterRequest;
import com.prosperi.argeo.auth.util.JwtUtil;
import com.prosperi.argeo.model.User;
import com.prosperi.argeo.service.UserService;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class AuthController {

    private final UserService userService = new UserService();
    private final String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        // Login route
        app.post(apiVersionV1 + "/login", ctx -> {
            LoginRequest loginRequest = ctx.bodyAsClass(LoginRequest.class);

            Optional<User> userOpt = userService.findUserByEmail(loginRequest.getEmail());

            if (userOpt.isEmpty()) {
                ctx.status(HttpStatus.UNAUTHORIZED).result("Invalid email or password");
                return;
            }

            User user = userOpt.get();

            if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
                ctx.status(HttpStatus.UNAUTHORIZED).result("Invalid email or password");
                return;
            }

            String token = JwtUtil.generateToken(user.getId().toString());

            ctx.status(HttpStatus.OK).json(new LoginResponse(token));
        });

        app.post(apiVersionV1 + "/register", ctx -> {
            RegisterRequest registerRequest = ctx.bodyAsClass(RegisterRequest.class);

            // Check if user already exists
            Optional<User> existingUser = userService.findUserByEmail(registerRequest.getEmail());
            if (existingUser.isPresent()) {
                ctx.status(HttpStatus.BAD_REQUEST).result("User already exists");
                return;
            }

            // Hash the password
            String hashedPassword = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt());

            // Create new user
            User newUser = User.builder()
                    .id(UUID.randomUUID())
                    .email(registerRequest.getEmail())
                    .password(hashedPassword)
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .role(registerRequest.getRole())
                    .registrationDate(LocalDate.now())
                    .phone(registerRequest.getPhone())
                    .address(registerRequest.getAddress())
                    .lastAccess(registerRequest.getLastAccess() != null ? registerRequest.getLastAccess() : LocalDateTime.now())
                    .build();

            userService.createUser(newUser);

            ctx.status(HttpStatus.CREATED).json(newUser);
        });

        app.get(apiVersionV1 + "/user", ctx -> {
            String token = ctx.header("Authorization").replace("Bearer ", "");
            String userId = JwtUtil.validateToken(token);

            Optional<User> userOpt = userService.getUserById(UUID.fromString(userId));

            if (userOpt.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND).result("User not found");
                return;
            }

            ctx.status(HttpStatus.OK).json(userOpt.get());
        });
    }
}
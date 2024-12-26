package com.prosperi.argeo.auth.controller;

import com.prosperi.argeo.auth.util.JwtUtil;
import com.prosperi.argeo.model.User;
import com.prosperi.argeo.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

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
    }

    // DTO for login request
    public static class LoginRequest {
        private String email;
        private String password;

        // Getters and setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // DTO for login response
    public static class LoginResponse {
        private String token;

        public LoginResponse(String token) {
            this.token = token;
        }

        // Getter
        public String getToken() {
            return token;
        }
    }
}

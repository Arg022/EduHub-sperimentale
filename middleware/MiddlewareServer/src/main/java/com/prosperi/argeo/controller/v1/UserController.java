package com.prosperi.argeo.controller.v1;

import com.prosperi.argeo.model.User;
import com.prosperi.argeo.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class UserController {

    private UserService userService = new UserService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/users", ctx -> {
            List<User> users = userService.getAllUsers();
            ctx.json(users);
        });

        app.post(apiVersionV1 + "/users", ctx -> {
            User userToCreate = ctx.bodyAsClass(User.class);
            userService.createUser(userToCreate);

            ctx.status(HttpStatus.CREATED).json(userToCreate);
        });

        app.get(apiVersionV1 + "/users/{id}", ctx -> {
            UUID userId = UUID.fromString(ctx.pathParam("id"));
            userService.getUserById(userId)
                    .ifPresentOrElse(
                            user -> ctx.json(user),
                            () -> ctx.status(HttpStatus.NOT_FOUND).json("User not found")
                    );
        });

        app.delete(apiVersionV1 + "/users/{id}", ctx -> {
            UUID userId = UUID.fromString(ctx.pathParam("id"));
            boolean isDeleted = userService.deleteUserById(userId);

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("User deleted");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("User not found");
            }
        });
    }
}
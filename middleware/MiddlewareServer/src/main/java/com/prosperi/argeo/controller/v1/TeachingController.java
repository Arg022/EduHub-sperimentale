package com.prosperi.argeo.controller.v1;

import java.util.List;
import java.util.UUID;

import com.prosperi.argeo.model.Teaching;
import com.prosperi.argeo.service.TeachingService;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class TeachingController {

    private TeachingService teachingService = new TeachingService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/teachings", ctx -> {
            List<Teaching> teachings = teachingService.getAllTeachings();
            ctx.json(teachings);
        });

        app.post(apiVersionV1 + "/teachings", ctx -> {
            Teaching teachingToCreate = ctx.bodyAsClass(Teaching.class);
            teachingService.createTeaching(teachingToCreate);

            ctx.status(HttpStatus.CREATED).json(teachingToCreate);
        });

        app.get(apiVersionV1 + "/teachings/{id}", ctx -> {
            UUID teachingId = UUID.fromString(ctx.pathParam("id"));
            teachingService.getTeachingById(teachingId)
                    .ifPresentOrElse(
                            teaching -> ctx.json(teaching),
                            () -> ctx.status(HttpStatus.NOT_FOUND).json("Teaching not found")
                    );
        });

        app.delete(apiVersionV1 + "/teachings/{id}", ctx -> {
            UUID teachingId = UUID.fromString(ctx.pathParam("id"));
            boolean isDeleted = teachingService.deleteTeachingById(teachingId);

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("Teaching deleted");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("Teaching not found");
            }
        });
    }
}

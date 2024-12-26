package com.prosperi.argeo.controller.v1;

import com.prosperi.argeo.model.Enrollment;
import com.prosperi.argeo.service.EnrollmentService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class EnrollmentController {

    private EnrollmentService enrollmentService = new EnrollmentService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/enrollments", ctx -> {
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
            ctx.json(enrollments);
        });

        app.post(apiVersionV1 + "/enrollments", ctx -> {
            Enrollment enrollmentToCreate = ctx.bodyAsClass(Enrollment.class);
            enrollmentService.createEnrollment(enrollmentToCreate);

            ctx.status(HttpStatus.CREATED).json(enrollmentToCreate);
        });

        app.get(apiVersionV1 + "/enrollments/{id}", ctx -> {
            UUID enrollmentId = UUID.fromString(ctx.pathParam("id"));
            enrollmentService.getEnrollmentById(enrollmentId)
                    .ifPresentOrElse(
                            enrollment -> ctx.json(enrollment),
                            () -> ctx.status(HttpStatus.NOT_FOUND).json("Enrollment not found")
                    );
        });

        app.delete(apiVersionV1 + "/enrollments/{id}", ctx -> {
            UUID enrollmentId = UUID.fromString(ctx.pathParam("id"));
            boolean isDeleted = enrollmentService.deleteEnrollmentById(enrollmentId);

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("Enrollment deleted");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("Enrollment not found");
            }
        });
    }
}


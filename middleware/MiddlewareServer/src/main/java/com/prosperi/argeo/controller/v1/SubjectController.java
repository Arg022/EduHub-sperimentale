package com.prosperi.argeo.controller.v1;

import com.prosperi.argeo.model.Subject;
import com.prosperi.argeo.service.SubjectService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class SubjectController {

    private SubjectService subjectService = new SubjectService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/subjects", ctx -> {
            List<Subject> subjects = subjectService.getAllSubjects();
            ctx.json(subjects);
        });

        app.post(apiVersionV1 + "/subjects", ctx -> {
            Subject subjectToCreate = ctx.bodyAsClass(Subject.class);
            subjectService.createSubject(subjectToCreate);

            ctx.status(HttpStatus.CREATED).json(subjectToCreate);
        });

        app.get(apiVersionV1 + "/subjects/{id}", ctx -> {
            UUID subjectId = UUID.fromString(ctx.pathParam("id"));
            subjectService.getSubjectById(subjectId)
                    .ifPresentOrElse(
                            subject -> ctx.json(subject),
                            () -> ctx.status(HttpStatus.NOT_FOUND).json("Subject not found")
                    );
        });

        app.delete(apiVersionV1 + "/subjects/{id}", ctx -> {
            UUID subjectId = UUID.fromString(ctx.pathParam("id"));
            boolean isDeleted = subjectService.deleteSubjectById(subjectId);

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("Subject deleted");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("Subject not found");
            }
        });
    }
}


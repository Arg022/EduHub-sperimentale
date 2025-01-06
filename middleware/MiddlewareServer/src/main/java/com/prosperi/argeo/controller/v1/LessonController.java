package com.prosperi.argeo.controller.v1;

import java.util.List;
import java.util.UUID;

import com.prosperi.argeo.model.Lesson;
import com.prosperi.argeo.service.LessonService;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class LessonController {

    private LessonService lessonService = new LessonService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/lessons", ctx -> {
            String userId = ctx.queryParam("userId");
            String role = ctx.queryParam("role");

            List<Lesson> lessons;
            if ("STUDENT".equals(role)) {
                lessons = lessonService.getLessonsByStudentId(UUID.fromString(userId));
            } else if ("TEACHER".equals(role)) {
                lessons = lessonService.getLessonsByTeacherId(UUID.fromString(userId));
            } else {
                lessons = lessonService.getAllLessons();
            }

            ctx.json(lessons);
        });

        app.post(apiVersionV1 + "/lessons", ctx -> {
            Lesson lessonToCreate = ctx.bodyAsClass(Lesson.class);
            lessonService.createLesson(lessonToCreate);

            ctx.status(HttpStatus.CREATED).json(lessonToCreate);
        });

        app.get(apiVersionV1 + "/lessons/{id}", ctx -> {
            UUID lessonId = UUID.fromString(ctx.pathParam("id"));
            lessonService.getLessonById(lessonId)
                    .ifPresentOrElse(
                            lesson -> ctx.json(lesson),
                            () -> ctx.status(HttpStatus.NOT_FOUND).json("Lesson not found")
                    );
        });

        app.delete(apiVersionV1 + "/lessons/{id}", ctx -> {
            UUID lessonId = UUID.fromString(ctx.pathParam("id"));
            boolean isDeleted = lessonService.deleteLessonById(lessonId);

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("Lesson deleted");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("Lesson not found");
            }
        });
    }
}
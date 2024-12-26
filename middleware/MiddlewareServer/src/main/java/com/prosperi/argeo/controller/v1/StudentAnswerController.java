package com.prosperi.argeo.controller.v1;

import com.prosperi.argeo.model.StudentAnswer;
import com.prosperi.argeo.service.StudentAnswerService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class StudentAnswerController {

    private StudentAnswerService studentAnswerService = new StudentAnswerService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/student-answers", ctx -> {
            List<StudentAnswer> studentAnswers = studentAnswerService.getAllStudentAnswers();
            ctx.json(studentAnswers);
        });

        app.post(apiVersionV1 + "/student-answers", ctx -> {
            StudentAnswer studentAnswerToCreate = ctx.bodyAsClass(StudentAnswer.class);
            studentAnswerService.createStudentAnswer(studentAnswerToCreate);

            ctx.status(HttpStatus.CREATED).json(studentAnswerToCreate);
        });

        app.get(apiVersionV1 + "/student-answers/{id}", ctx -> {
            UUID studentAnswerId = UUID.fromString(ctx.pathParam("id"));
            studentAnswerService.getStudentAnswerById(studentAnswerId)
                    .ifPresentOrElse(
                            studentAnswer -> ctx.json(studentAnswer),
                            () -> ctx.status(HttpStatus.NOT_FOUND).json("StudentAnswer not found")
                    );
        });

        app.delete(apiVersionV1 + "/student-answers/{id}", ctx -> {
            UUID studentAnswerId = UUID.fromString(ctx.pathParam("id"));
            boolean isDeleted = studentAnswerService.deleteStudentAnswerById(studentAnswerId);

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("StudentAnswer deleted");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("StudentAnswer not found");
            }
        });
    }
}


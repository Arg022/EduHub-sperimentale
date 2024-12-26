package com.prosperi.argeo.controller.v1;

import com.prosperi.argeo.model.Answer;
import com.prosperi.argeo.service.AnswerService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class AnswerController {

    private AnswerService answerService = new AnswerService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/answers", ctx -> {
            List<Answer> answers = answerService.getAllAnswers();
            ctx.json(answers);
        });

        app.post(apiVersionV1 + "/answers", ctx -> {
            Answer answerToCreate = ctx.bodyAsClass(Answer.class);
            answerService.createAnswer(answerToCreate);

            ctx.status(HttpStatus.CREATED).json(answerToCreate);
        });

        app.get(apiVersionV1 + "/answers/{id}", ctx -> {
            UUID answerId = UUID.fromString(ctx.pathParam("id"));
            answerService.getAnswerById(answerId)
                    .ifPresentOrElse(
                            answer -> ctx.json(answer),
                            () -> ctx.status(HttpStatus.NOT_FOUND).json("Answer not found")
                    );
        });

        app.delete(apiVersionV1 + "/answers/{id}", ctx -> {
            UUID answerId = UUID.fromString(ctx.pathParam("id"));
            boolean isDeleted = answerService.deleteAnswerById(answerId);

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("Answer deleted");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("Answer not found");
            }
        });
    }
}


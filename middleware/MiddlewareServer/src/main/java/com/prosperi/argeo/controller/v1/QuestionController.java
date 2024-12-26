package com.prosperi.argeo.controller.v1;

import com.prosperi.argeo.model.Question;
import com.prosperi.argeo.service.QuestionService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class QuestionController {

    private QuestionService questionService = new QuestionService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/questions", ctx -> {
            List<Question> questions = questionService.getAllQuestions();
            ctx.json(questions);
        });

        app.post(apiVersionV1 + "/questions", ctx -> {
            Question questionToCreate = ctx.bodyAsClass(Question.class);
            questionService.createQuestion(questionToCreate);

            ctx.status(HttpStatus.CREATED).json(questionToCreate);
        });

        app.get(apiVersionV1 + "/questions/{id}", ctx -> {
            UUID questionId = UUID.fromString(ctx.pathParam("id"));
            questionService.getQuestionById(questionId)
                    .ifPresentOrElse(
                            question -> ctx.json(question),
                            () -> ctx.status(HttpStatus.NOT_FOUND).json("Question not found")
                    );
        });

        app.delete(apiVersionV1 + "/questions/{id}", ctx -> {
            UUID questionId = UUID.fromString(ctx.pathParam("id"));
            boolean isDeleted = questionService.deleteQuestionById(questionId);

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("Question deleted");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("Question not found");
            }
        });
    }
}


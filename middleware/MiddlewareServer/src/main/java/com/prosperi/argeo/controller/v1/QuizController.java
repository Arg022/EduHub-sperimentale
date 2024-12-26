package com.prosperi.argeo.controller.v1;

import com.prosperi.argeo.model.Quiz;
import com.prosperi.argeo.service.QuizService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class QuizController {

    private QuizService quizService = new QuizService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/quizzes", ctx -> {
            List<Quiz> quizzes = quizService.getAllQuizzes();
            ctx.json(quizzes);
        });

        app.post(apiVersionV1 + "/quizzes", ctx -> {
            Quiz quizToCreate = ctx.bodyAsClass(Quiz.class);
            quizService.createQuiz(quizToCreate);

            ctx.status(HttpStatus.CREATED).json(quizToCreate);
        });

        app.get(apiVersionV1 + "/quizzes/{id}", ctx -> {
            UUID quizId = UUID.fromString(ctx.pathParam("id"));
            quizService.getQuizById(quizId)
                    .ifPresentOrElse(
                            quiz -> ctx.json(quiz),
                            () -> ctx.status(HttpStatus.NOT_FOUND).json("Quiz not found")
                    );
        });

        app.delete(apiVersionV1 + "/quizzes/{id}", ctx -> {
            UUID quizId = UUID.fromString(ctx.pathParam("id"));
            boolean isDeleted = quizService.deleteQuizById(quizId);

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("Quiz deleted");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("Quiz not found");
            }
        });
    }
}


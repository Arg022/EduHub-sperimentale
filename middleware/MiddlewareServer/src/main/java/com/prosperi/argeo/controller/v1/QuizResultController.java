package com.prosperi.argeo.controller.v1;

import java.util.List;
import java.util.UUID;

import com.prosperi.argeo.model.QuizResult;
import com.prosperi.argeo.model.StudentAnswer;
import com.prosperi.argeo.service.QuizResultService;
import com.prosperi.argeo.service.StudentAnswerService;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class QuizResultController {

    private QuizResultService quizResultService = new QuizResultService();
    private StudentAnswerService studentAnswerService = new StudentAnswerService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/quiz-results", ctx -> {
            List<QuizResult> quizResults = quizResultService.getAllQuizResults();
            ctx.json(quizResults);
        });

        app.post(apiVersionV1 + "/quiz-attempts", ctx -> {
            QuizResult quizResult = ctx.bodyValidator(QuizResult.class).get();
            List<StudentAnswer> studentAnswers = ctx.bodyValidator(List.class)
                .check(answers -> !answers.isEmpty(), "Student answers cannot be empty")
                .get();

            QuizResult calculatedResult = quizResultService.calculateAndSaveQuizResult(quizResult, studentAnswers);
            ctx.status(HttpStatus.CREATED).json(calculatedResult);
        });

        app.post(apiVersionV1 + "/quiz-results", ctx -> {
            QuizResult quizResultToCreate = ctx.bodyAsClass(QuizResult.class);
            quizResultService.createQuizResult(quizResultToCreate);
            ctx.status(HttpStatus.CREATED).json(quizResultToCreate);
        });

        app.get(apiVersionV1 + "/quiz-results/{id}", ctx -> {
            UUID quizResultId = UUID.fromString(ctx.pathParam("id"));
            quizResultService.getQuizResultById(quizResultId)
                    .ifPresentOrElse(
                            quizResult -> ctx.json(quizResult),
                            () -> ctx.status(HttpStatus.NOT_FOUND).json("QuizResult not found")
                    );
        });

        app.delete(apiVersionV1 + "/quiz-results/{id}", ctx -> {
            UUID quizResultId = UUID.fromString(ctx.pathParam("id"));
            boolean isDeleted = quizResultService.deleteQuizResultById(quizResultId);

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("QuizResult deleted");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("QuizResult not found");
            }
        });
    }
}
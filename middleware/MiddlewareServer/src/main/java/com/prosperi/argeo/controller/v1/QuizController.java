package com.prosperi.argeo.controller.v1;

import java.util.List;
import java.util.UUID;

import com.prosperi.argeo.model.Question;
import com.prosperi.argeo.model.Quiz;
import com.prosperi.argeo.model.Answer;
import com.prosperi.argeo.service.QuestionService;
import com.prosperi.argeo.service.QuizService;
import com.prosperi.argeo.service.AnswerService;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class QuizController {

    private QuizService quizService = new QuizService();
    private QuestionService questionService = new QuestionService();
    private AnswerService answerService = new AnswerService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/quizzes", ctx -> {
            String studentId = ctx.queryParam("studentId");
            String teacherId = ctx.queryParam("teacherId");

            List<Quiz> quizzes;
            if (studentId != null) {
                quizzes = quizService.getQuizzesByStudentId(UUID.fromString(studentId));
            } else if (teacherId != null) {
                quizzes = quizService.getQuizzesByTeacherId(UUID.fromString(teacherId));
            } else {
                quizzes = quizService.getAllQuizzes();
            }

            ctx.json(quizzes);
        });

        app.post(apiVersionV1 + "/quizzes", ctx -> {
            Quiz quizToCreate = ctx.bodyAsClass(Quiz.class);
            List<Question> questions = quizToCreate.getQuestions();
            quizService.createQuiz(quizToCreate, questions);

            ctx.status(HttpStatus.CREATED).json(quizToCreate);
        });

        app.get(apiVersionV1 + "/quizzes/{id}", ctx -> {
            UUID quizId = UUID.fromString(ctx.pathParam("id"));
            quizService.getQuizById(quizId)
                .ifPresentOrElse(
                    quiz -> {
                        List<Question> questions = questionService.getQuestionsByQuizId(quizId);
                        for (Question question : questions) {
                            List<Answer> answers = answerService.getAnswersByQuestionId(question.getId());
                            question.setAnswers(answers);
                        }
                        quiz.setQuestions(questions); 
                        ctx.json(quiz);
                    },
                    () -> {
                        ctx.status(HttpStatus.NOT_FOUND).json("Quiz not found");
                    }
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
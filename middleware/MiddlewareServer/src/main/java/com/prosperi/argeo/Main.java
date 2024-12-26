package com.prosperi.argeo;

import com.prosperi.argeo.controller.v1.*;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> cors.add(it -> it.anyHost()));
        }).start(8082);

        new UserController().registerRoutes(app);
        new CourseController().registerRoutes(app);
        new SubjectController().registerRoutes(app);
        new LessonController().registerRoutes(app);
        new EnrollmentController().registerRoutes(app);
        new TeachingController().registerRoutes(app);
        new AttendanceController().registerRoutes(app);
        new QuizController().registerRoutes(app);
        new QuestionController().registerRoutes(app);
        new AnswerController().registerRoutes(app);
        new QuizResultController().registerRoutes(app);
        new StudentAnswerController().registerRoutes(app);
        new NotificationController().registerRoutes(app);


        System.out.println("Server is running on port 8082");
    }
}
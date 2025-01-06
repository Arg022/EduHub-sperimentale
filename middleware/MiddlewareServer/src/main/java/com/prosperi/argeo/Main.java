package com.prosperi.argeo;

import com.prosperi.argeo.auth.controller.AuthController;
import com.prosperi.argeo.controller.v1.AnswerController;
import com.prosperi.argeo.controller.v1.AttendanceController;
import com.prosperi.argeo.controller.v1.CourseController;
import com.prosperi.argeo.controller.v1.EnrollmentController;
import com.prosperi.argeo.controller.v1.LessonController;
import com.prosperi.argeo.controller.v1.NotificationController;
import com.prosperi.argeo.controller.v1.QuestionController;
import com.prosperi.argeo.controller.v1.QuizController;
import com.prosperi.argeo.controller.v1.QuizResultController;
import com.prosperi.argeo.controller.v1.StudentAnswerController;
import com.prosperi.argeo.controller.v1.SubjectController;
import com.prosperi.argeo.controller.v1.TeachingController;
import com.prosperi.argeo.controller.v1.UserController;
import com.prosperi.argeo.util.DataLoader;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        DataLoader.loadInitialData();

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> cors.add(it -> it.anyHost()));
        }).start(8082);

        new AuthController().registerRoutes(app);
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
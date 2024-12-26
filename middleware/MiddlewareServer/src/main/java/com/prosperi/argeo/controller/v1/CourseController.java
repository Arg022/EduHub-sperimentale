package com.prosperi.argeo.controller.v1;

import com.prosperi.argeo.model.Course;
import com.prosperi.argeo.service.CourseService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class CourseController {

    private CourseService courseService = new CourseService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/courses", ctx -> {
            List<Course> courses = courseService.getAllCourses();
            ctx.json(courses);
        });

        app.post(apiVersionV1 + "/courses", ctx -> {
            Course courseToCreate = ctx.bodyAsClass(Course.class);
            courseService.createCourse(courseToCreate);

            ctx.status(HttpStatus.CREATED).json(courseToCreate);
        });

        app.get(apiVersionV1 + "/courses/{id}", ctx -> {
            UUID courseId = UUID.fromString(ctx.pathParam("id"));
            courseService.getCourseById(courseId)
                    .ifPresentOrElse(
                            course -> ctx.json(course),
                            () -> ctx.status(HttpStatus.NOT_FOUND).json("Course not found")
                    );
        });

        app.delete(apiVersionV1 + "/courses/{id}", ctx -> {
            UUID courseId = UUID.fromString(ctx.pathParam("id"));
            boolean isDeleted = courseService.deleteCourseById(courseId);

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("Course deleted");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("Course not found");
            }
        });
    }
}

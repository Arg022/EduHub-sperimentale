package com.prosperi.argeo.controller.v1;

import com.prosperi.argeo.model.Attendance;
import com.prosperi.argeo.service.AttendanceService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class AttendanceController {

    private AttendanceService attendanceService = new AttendanceService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/attendances", ctx -> {
            List<Attendance> attendances = attendanceService.getAllAttendances();
            ctx.json(attendances);
        });

        app.post(apiVersionV1 + "/attendances", ctx -> {
            Attendance attendanceToCreate = ctx.bodyAsClass(Attendance.class);
            attendanceService.createAttendance(attendanceToCreate);

            ctx.status(HttpStatus.CREATED).json(attendanceToCreate);
        });

        app.get(apiVersionV1 + "/attendances/{id}", ctx -> {
            UUID attendanceId = UUID.fromString(ctx.pathParam("id"));
            attendanceService.getAttendanceById(attendanceId)
                    .ifPresentOrElse(
                            attendance -> ctx.json(attendance),
                            () -> ctx.status(HttpStatus.NOT_FOUND).json("Attendance not found")
                    );
        });

        app.delete(apiVersionV1 + "/attendances/{id}", ctx -> {
            UUID attendanceId = UUID.fromString(ctx.pathParam("id"));
            boolean isDeleted = attendanceService.deleteAttendanceById(attendanceId);

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("Attendance deleted");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("Attendance not found");
            }
        });
    }
}


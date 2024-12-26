package com.prosperi.argeo.controller.v1;

import com.prosperi.argeo.model.Notification;
import com.prosperi.argeo.service.NotificationService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.UUID;

public class NotificationController {

    private NotificationService notificationService = new NotificationService();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        app.get(apiVersionV1 + "/notifications", ctx -> {
            List<Notification> notifications = notificationService.getAllNotifications();
            ctx.json(notifications);
        });

        app.post(apiVersionV1 + "/notifications", ctx -> {
            Notification notificationToCreate = ctx.bodyAsClass(Notification.class);
            notificationService.createNotification(notificationToCreate);

            ctx.status(HttpStatus.CREATED).json(notificationToCreate);
        });

        app.get(apiVersionV1 + "/notifications/{id}", ctx -> {
            UUID notificationId = UUID.fromString(ctx.pathParam("id"));
            notificationService.getNotificationById(notificationId)
                    .ifPresentOrElse(
                            notification -> ctx.json(notification),
                            () -> ctx.status(HttpStatus.NOT_FOUND).json("Notification not found")
                    );
        });

        app.delete(apiVersionV1 + "/notifications/{id}", ctx -> {
            UUID notificationId = UUID.fromString(ctx.pathParam("id"));
            boolean isDeleted = notificationService.deleteNotificationById(notificationId);

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("Notification deleted");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("Notification not found");
            }
        });
    }
}


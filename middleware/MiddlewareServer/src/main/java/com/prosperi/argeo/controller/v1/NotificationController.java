package com.prosperi.argeo.controller.v1;

import com.prosperi.argeo.enums.NotificationPriority;
import com.prosperi.argeo.enums.NotificationType;
import com.prosperi.argeo.model.Notification;
import com.prosperi.argeo.service.NotificationService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
            Map<String, Object> requestBody = ctx.bodyAsClass(Map.class);
            Notification notificationToCreate = new Notification();
            notificationToCreate.setSenderId(UUID.fromString((String) requestBody.get("senderId")));
            notificationToCreate.setTitle((String) requestBody.get("title"));
            notificationToCreate.setContent((String) requestBody.get("content"));
            notificationToCreate.setNotificationType(NotificationType.valueOf((String) requestBody.get("notificationType")));
            notificationToCreate.setPriority(NotificationPriority.valueOf((String) requestBody.get("priority")));
            notificationToCreate.setCreationDate(LocalDateTime.now());

            List<String> recipientIds = (List<String>) requestBody.get("recipients");
            List<UUID> recipientUUIDs = recipientIds.stream().map(UUID::fromString).toList();
            notificationService.createNotificationForRecipients(notificationToCreate, recipientUUIDs);

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
        
        app.put(apiVersionV1 + "/notifications/{id}", ctx -> {
            UUID notificationId = UUID.fromString(ctx.pathParam("id"));
            Notification updatedNotification = ctx.bodyAsClass(Notification.class);
        
            // Assicurati che l'ID nell'oggetto inviato corrisponda all'ID nell'URL
            if (!notificationId.equals(updatedNotification.getId())) {
                ctx.status(HttpStatus.BAD_REQUEST).json("ID mismatch");
                return;
            }
        
            notificationService.markAsRead(updatedNotification);
            ctx.status(HttpStatus.OK).json("Notification marked as read");
        });
    }
}


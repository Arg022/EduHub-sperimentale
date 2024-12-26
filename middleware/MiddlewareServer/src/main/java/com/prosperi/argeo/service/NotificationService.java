package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.NotificationDAO;
import com.prosperi.argeo.model.Notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NotificationService {
    private final NotificationDAO notificationDAO = new NotificationDAO();

    public Notification createNotification(Notification notification) {
        return notificationDAO.addNotification(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationDAO.getAllNotifications();
    }

    public Optional<Notification> getNotificationById(UUID id) {
        return notificationDAO.findNotificationById(id);
    }

    public boolean deleteNotificationById(UUID id) {
        return notificationDAO.deleteById(id);
    }
}

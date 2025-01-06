package com.prosperi.argeo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prosperi.argeo.dao.NotificationDAO;
import com.prosperi.argeo.model.Notification;

public class NotificationService {
    private final NotificationDAO notificationDAO = new NotificationDAO();

    public void createNotificationForRecipients(Notification notification, List<UUID> recipientIds) {
        for (UUID recipientId : recipientIds) {
            Notification newNotification = new Notification();
            newNotification.setSenderId(notification.getSenderId());
            newNotification.setRecipientId(recipientId);
            newNotification.setTitle(notification.getTitle());
            newNotification.setContent(notification.getContent());
            newNotification.setNotificationType(notification.getNotificationType());
            newNotification.setPriority(notification.getPriority());
            newNotification.setCreationDate(notification.getCreationDate());
            notificationDAO.addNotification(newNotification);
        }
    }
    
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

    public boolean markAsRead(Notification updatedNotification) {
        return notificationDAO.markAsRead(updatedNotification.getId());
    }
}

package com.prosperi.argeo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prosperi.argeo.enums.NotificationPriority;
import com.prosperi.argeo.enums.NotificationType;
import com.prosperi.argeo.model.Notification;
import com.prosperi.argeo.util.database.DatabaseConnection;

public class NotificationDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Notification addNotification(Notification notification) {
        String insertSQL = "INSERT INTO public.notification (id, sender_id, recipient_id, title, content, notification_type, priority, creation_date) " +
                           "VALUES (?, ?, ?, ?, ?, ?::notification_type, ?::notification_priority, ?)";
    
        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            UUID id = UUID.randomUUID();
            ps.setObject(1, id);
            ps.setObject(2, notification.getSenderId());
            ps.setObject(3, notification.getRecipientId());
            ps.setString(4, notification.getTitle());
            ps.setString(5, notification.getContent());
            ps.setString(6, notification.getNotificationType().name().toLowerCase());
            ps.setString(7, notification.getPriority().name().toLowerCase());
            ps.setTimestamp(8, Timestamp.valueOf(notification.getCreationDate()));
    
            ps.executeUpdate();
            notification.setId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Errore nell'aggiunta della notifica", e);
        }
        return notification;
    }

    public List<Notification> getAllNotifications() {
        String selectSQL = "SELECT * FROM public.notification";
        List<Notification> notifications = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Notification notification = new Notification();
                notification.setId(rs.getObject("id", java.util.UUID.class));
                notification.setSenderId(rs.getObject("sender_id", java.util.UUID.class));
                notification.setRecipientId(rs.getObject("recipient_id", java.util.UUID.class));
                notification.setTitle(rs.getString("title"));
                notification.setContent(rs.getString("content"));
                notification.setNotificationType(NotificationType.valueOf(rs.getString("notification_type").toUpperCase()));
                notification.setPriority(NotificationPriority.valueOf(rs.getString("priority").toUpperCase()));
                notification.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                notification.setReadDate(rs.getTimestamp("read_date") != null ? rs.getTimestamp("read_date").toLocalDateTime() : null);
                notifications.add(notification);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all notifications", e);
        }
        return notifications;
    }

    public Optional<Notification> findNotificationById(UUID id) {
        String selectSQL = "SELECT * FROM public.notification WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Notification notification = new Notification();
                    notification.setId(rs.getObject("id", java.util.UUID.class));
                    notification.setSenderId(rs.getObject("sender_id", java.util.UUID.class));
                    notification.setRecipientId(rs.getObject("recipient_id", java.util.UUID.class));
                    notification.setTitle(rs.getString("title"));
                    notification.setContent(rs.getString("content"));
                    notification.setNotificationType(NotificationType.valueOf(rs.getString("notification_type").toUpperCase()));
                    notification.setPriority(NotificationPriority.valueOf(rs.getString("priority").toUpperCase()));
                    notification.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                    notification.setReadDate(rs.getTimestamp("read_date") != null ? rs.getTimestamp("read_date").toLocalDateTime() : null);
                    return Optional.of(notification);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching notification by ID", e);
        }
        return Optional.empty();
    }

    public boolean deleteById(UUID id) {
        String deleteSQL = "DELETE FROM public.notification WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting notification by ID", e);
        }
    }

    public boolean markAsRead(UUID notificationId) {
        String updateSQL = "UPDATE public.notification SET read_date = CURRENT_TIMESTAMP WHERE id = ?";
    
        try (PreparedStatement ps = connection.prepareStatement(updateSQL)) {
            ps.setObject(1, notificationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Errore nel marcare la notifica come letta", e);
        }
    }
}
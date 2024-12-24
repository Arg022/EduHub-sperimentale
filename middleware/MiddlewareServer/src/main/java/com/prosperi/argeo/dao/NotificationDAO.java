package com.prosperi.argeo.dao;

import com.prosperi.argeo.enums.NotificationPriority;
import com.prosperi.argeo.enums.NotificationType;
import com.prosperi.argeo.model.Notification;
import com.prosperi.argeo.util.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NotificationDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Notification addNotification(Notification notification) {
        String insertSQL = "INSERT INTO public.\"notification\" (sender_id, recipient_id, title, content, notification_type, priority, creation_date, read_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setObject(1, notification.getSenderId());
            ps.setObject(2, notification.getRecipientId());
            ps.setString(3, notification.getTitle());
            ps.setString(4, notification.getContent());
            ps.setString(5, notification.getNotificationType().name());
            ps.setString(6, notification.getPriority().name());
            ps.setTimestamp(7, Timestamp.valueOf(notification.getCreationDate()));
            ps.setTimestamp(8, notification.getReadDate() != null ? Timestamp.valueOf(notification.getReadDate()) : null);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding notification", e);
        }
        return notification;
    }

    public List<Notification> getAllNotifications() {
        String selectSQL = "SELECT * FROM public.\"notification\"";
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
                notification.setNotificationType(NotificationType.valueOf(rs.getString("notification_type")));
                notification.setPriority(NotificationPriority.valueOf(rs.getString("priority")));
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
        String selectSQL = "SELECT * FROM public.\"notification\" WHERE id = ?";
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
                    notification.setNotificationType(NotificationType.valueOf(rs.getString("notification_type")));
                    notification.setPriority(NotificationPriority.valueOf(rs.getString("priority")));
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
        String deleteSQL = "DELETE FROM public.\"notification\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting notification by ID", e);
        }
    }
}

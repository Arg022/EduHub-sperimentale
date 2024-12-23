package com.prosperi.argeo.model;

import com.prosperi.argeo.enums.NotificationPriority;
import com.prosperi.argeo.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Notification {
    private UUID id;
    private UUID senderId;
    private UUID recipientId;
    private String title;
    private String content;
    private NotificationType notificationType;
    private NotificationPriority priority;
    private LocalDateTime creationDate;
    private LocalDateTime readDate;
}

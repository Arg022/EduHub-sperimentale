package com.prosperi.argeo.model;

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
public class Quiz {
    private UUID id;
    private UUID courseId;
    private UUID creatorId;
    private String title;
    private String description;
    private Integer durationMinutes;
    private Integer maxAttempts;
    private LocalDateTime creationDate;
    private LocalDateTime publicationDate;
}

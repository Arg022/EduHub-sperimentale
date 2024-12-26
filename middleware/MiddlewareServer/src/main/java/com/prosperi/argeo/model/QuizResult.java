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
public class QuizResult {
    private UUID id;
    private UUID quizId;
    private UUID userId;
    private Float totalScore;
    private LocalDateTime startTime;
    private LocalDateTime completionTime;
    private Integer timeSpent;
}

package com.prosperi.argeo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnswer {
    private UUID id;
    private UUID quizResultId;
    private UUID questionId;
    private UUID answerId;
    private Float scoreObtained;
}

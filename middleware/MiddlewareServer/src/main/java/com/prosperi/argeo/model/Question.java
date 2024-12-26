package com.prosperi.argeo.model;

import com.prosperi.argeo.enums.QuestionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private UUID id;
    private UUID quizId;
    private String text;
    private Float score;
    private QuestionType questionType;
}

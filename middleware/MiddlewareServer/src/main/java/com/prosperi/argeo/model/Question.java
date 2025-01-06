package com.prosperi.argeo.model;

import com.prosperi.argeo.enums.QuestionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
    private List<Answer> answers;
    
    public List<Answer> getAnswers() {
        return answers;
    }
    

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}



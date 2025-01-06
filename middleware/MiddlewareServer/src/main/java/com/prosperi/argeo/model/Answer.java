package com.prosperi.argeo.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    private UUID id;
    private UUID questionId;
    private String text;
    private float score;
    
    @JsonProperty("isCorrect")
    private boolean isCorrect;

}

package com.prosperi.argeo.model;

import java.time.LocalDateTime;
import java.util.List;
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
    private List<Question> questions;

    @JsonProperty("creator_id")
    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }
}

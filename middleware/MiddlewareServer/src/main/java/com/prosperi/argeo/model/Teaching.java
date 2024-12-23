package com.prosperi.argeo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Teaching {
    private UUID id;
    private UUID userId;
    private UUID subjectId;
    private UUID courseId;
    private LocalDate startDate;
    private LocalDate endDate;
}

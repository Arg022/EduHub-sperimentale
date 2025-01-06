package com.prosperi.argeo.model;

import com.prosperi.argeo.enums.EnrollmentStatus;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    private UUID id;
    private UUID userId;
    private UUID courseId;
    private LocalDate enrollmentDate;
    private EnrollmentStatus status;
}

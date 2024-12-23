package com.prosperi.argeo.model;


import com.prosperi.argeo.enums.AttendanceMode;

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
public class Attendance {
    private UUID id;
    private UUID lessonId;
    private UUID userId;
    private boolean present;
    private AttendanceMode mode;
    private String notes;
    private LocalDateTime recordTime;
}

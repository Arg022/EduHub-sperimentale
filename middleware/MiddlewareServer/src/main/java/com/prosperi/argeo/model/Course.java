package com.prosperi.argeo.model;

import com.prosperi.argeo.enums.CourseLevel;
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
public class Course {
    private UUID id;
    private String name;
    private String description;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private String syllabus;
    private Integer maxStudents;
    private CourseLevel level;
}

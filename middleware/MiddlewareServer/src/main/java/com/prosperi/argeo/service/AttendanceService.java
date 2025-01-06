package com.prosperi.argeo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.prosperi.argeo.dao.AttendanceDAO;
import com.prosperi.argeo.enums.AttendanceMode;
import com.prosperi.argeo.model.Attendance;

public class AttendanceService {
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();

    public Attendance createAttendance(Attendance attendance) {
        return attendanceDAO.addAttendance(attendance);
    }

    public List<Attendance> getAllAttendances() {
        return attendanceDAO.getAllAttendance();
    }

    public Optional<Attendance> getAttendanceById(UUID id) {
        return attendanceDAO.findAttendanceById(id);
    }

    public boolean deleteAttendanceById(UUID id) {
        return attendanceDAO.deleteById(id);
    }

    public List<Attendance> getAttendancesByLessonId(UUID lessonId) {
        return attendanceDAO.findAttendancesByLessonId(lessonId);
    }
    
    public void recordAttendance(UUID lessonId, Map<String, Boolean> attendanceMap) {
    attendanceMap.forEach((studentIdStr, isPresent) -> {
        UUID studentId = UUID.fromString(studentIdStr);
        Attendance attendance = Attendance.builder()
                .id(UUID.randomUUID())
                .lessonId(lessonId)
                .userId(studentId)
                .present(isPresent)
                .mode(AttendanceMode.IN_PERSON) 
                .recordTime(LocalDateTime.now())
                .build();
        attendanceDAO.addAttendance(attendance);
    });
}
}
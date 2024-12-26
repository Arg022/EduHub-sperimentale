package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.AttendanceDAO;
import com.prosperi.argeo.model.Attendance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
}
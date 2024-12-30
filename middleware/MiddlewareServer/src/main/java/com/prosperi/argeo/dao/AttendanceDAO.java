package com.prosperi.argeo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prosperi.argeo.enums.AttendanceMode;
import com.prosperi.argeo.model.Attendance;
import com.prosperi.argeo.util.database.DatabaseConnection;

public class AttendanceDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Attendance addAttendance(Attendance attendance) {
        String insertSQL = "INSERT INTO public.\"attendance\" (id, lesson_id, user_id, present, mode, notes, record_time) VALUES (?, ?, ?, ?, ?::attendance_mode, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            UUID id = UUID.randomUUID();
            ps.setObject(1, id);
            ps.setObject(2, attendance.getLessonId());
            ps.setObject(3, attendance.getUserId());
            ps.setBoolean(4, attendance.isPresent());
            ps.setString(5, attendance.getMode().name().toLowerCase());
            ps.setString(6, attendance.getNotes());
            ps.setTimestamp(7, Timestamp.valueOf(attendance.getRecordTime()));

            ps.executeUpdate();
            attendance.setId(id); // Imposta l'ID generato nella presenza
        } catch (SQLException e) {
            throw new RuntimeException("Error adding attendance", e);
        }
        return attendance;
    }

    public List<Attendance> getAllAttendance() {
        String selectSQL = "SELECT * FROM public.\"attendance\"";
        List<Attendance> attendances = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Attendance attendance = new Attendance();
                attendance.setId(rs.getObject("id", java.util.UUID.class));
                attendance.setLessonId(rs.getObject("lesson_id", java.util.UUID.class));
                attendance.setUserId(rs.getObject("user_id", java.util.UUID.class));
                attendance.setPresent(rs.getBoolean("present"));
                attendance.setMode(AttendanceMode.valueOf(rs.getString("mode")));
                attendance.setNotes(rs.getString("notes"));
                attendance.setRecordTime(rs.getTimestamp("record_time").toLocalDateTime());
                attendances.add(attendance);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all attendance", e);
        }
        return attendances;
    }

    public Optional<Attendance> findAttendanceById(UUID id) {
        String selectSQL = "SELECT * FROM public.\"attendance\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Attendance attendance = new Attendance();
                    attendance.setId(rs.getObject("id", java.util.UUID.class));
                    attendance.setLessonId(rs.getObject("lesson_id", java.util.UUID.class));
                    attendance.setUserId(rs.getObject("user_id", java.util.UUID.class));
                    attendance.setPresent(rs.getBoolean("present"));
                    attendance.setMode(AttendanceMode.valueOf(rs.getString("mode")));
                    attendance.setNotes(rs.getString("notes"));
                    attendance.setRecordTime(rs.getTimestamp("record_time").toLocalDateTime());
                    return Optional.of(attendance);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching attendance by ID", e);
        }
        return Optional.empty();
    }

    public boolean deleteById(UUID id) {
        String deleteSQL = "DELETE FROM public.\"attendance\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting attendance by ID", e);
        }
    }
}
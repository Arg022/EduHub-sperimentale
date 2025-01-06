package com.prosperi.argeo.dao;

import com.prosperi.argeo.enums.AttendanceMode;
import com.prosperi.argeo.model.Attendance;
import com.prosperi.argeo.util.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AttendanceDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Attendance addAttendance(Attendance attendance) {
        String insertSQL = "INSERT INTO public.\"attendance\" (id, lesson_id, user_id, present, mode, record_time) " +
                           "VALUES (?, ?, ?, ?, ?::attendance_mode, ?) " +
                           "ON CONFLICT (lesson_id, user_id) DO UPDATE " +
                           "SET present = EXCLUDED.present, mode = EXCLUDED.mode, record_time = EXCLUDED.record_time";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            UUID id = UUID.randomUUID();
            ps.setObject(1, id);
            ps.setObject(2, attendance.getLessonId());
            ps.setObject(3, attendance.getUserId());
            ps.setBoolean(4, attendance.isPresent());
            ps.setString(5, attendance.getMode().name().toLowerCase());
            ps.setTimestamp(6, Timestamp.valueOf(attendance.getRecordTime()));

            ps.executeUpdate();
            attendance.setId(id);
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
                attendance.setId(rs.getObject("id", UUID.class));
                attendance.setLessonId(rs.getObject("lesson_id", UUID.class));
                attendance.setUserId(rs.getObject("user_id", UUID.class));
                attendance.setPresent(rs.getBoolean("present"));
                attendance.setMode(AttendanceMode.valueOf(rs.getString("mode").toUpperCase()));
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
                    attendance.setId(rs.getObject("id", UUID.class));
                    attendance.setLessonId(rs.getObject("lesson_id", UUID.class));
                    attendance.setUserId(rs.getObject("user_id", UUID.class));
                    attendance.setPresent(rs.getBoolean("present"));
                    attendance.setMode(AttendanceMode.valueOf(rs.getString("mode").toUpperCase()));
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

    public List<Attendance> findAttendancesByLessonId(UUID lessonId) {
        String selectSQL = "SELECT * FROM public.\"attendance\" WHERE lesson_id = ?";
        List<Attendance> attendances = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, lessonId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Attendance attendance = new Attendance();
                    attendance.setId(rs.getObject("id", UUID.class));
                    attendance.setLessonId(rs.getObject("lesson_id", UUID.class));
                    attendance.setUserId(rs.getObject("user_id", UUID.class));
                    attendance.setPresent(rs.getBoolean("present"));
                    attendance.setMode(AttendanceMode.valueOf(rs.getString("mode").toUpperCase()));
                    attendance.setRecordTime(rs.getTimestamp("record_time").toLocalDateTime());
                    attendances.add(attendance);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching attendances by lesson ID", e);
        }
        return attendances;
    }
}
package com.prosperi.argeo.dao;

import com.prosperi.argeo.enums.EnrollmentStatus;
import com.prosperi.argeo.model.Enrollment;
import com.prosperi.argeo.util.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EnrollmentDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Enrollment addEnrollment(Enrollment enrollment) {
        String insertSQL = "INSERT INTO public.\"enrollment\" (user_id, course_id, enrollment_date, status) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setObject(1, enrollment.getUserId());
            ps.setObject(2, enrollment.getCourseId());
            ps.setDate(3, Date.valueOf(enrollment.getEnrollmentDate()));
            ps.setString(4, enrollment.getStatus().name());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding enrollment", e);
        }
        return enrollment;
    }

    public List<Enrollment> getAllEnrollments() {
        String selectSQL = "SELECT * FROM public.\"enrollment\"";
        List<Enrollment> enrollments = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setId(rs.getObject("id", java.util.UUID.class));
                enrollment.setUserId(rs.getObject("user_id", java.util.UUID.class));
                enrollment.setCourseId(rs.getObject("course_id", java.util.UUID.class));
                enrollment.setEnrollmentDate(rs.getDate("enrollment_date").toLocalDate());
                enrollment.setStatus(EnrollmentStatus.valueOf(rs.getString("status")));
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all enrollments", e);
        }
        return enrollments;
    }

    public Optional<Enrollment> findEnrollmentById(UUID id) {
        String selectSQL = "SELECT * FROM public.\"enrollment\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setId(rs.getObject("id", java.util.UUID.class));
                    enrollment.setUserId(rs.getObject("user_id", java.util.UUID.class));
                    enrollment.setCourseId(rs.getObject("course_id", java.util.UUID.class));
                    enrollment.setEnrollmentDate(rs.getDate("enrollment_date").toLocalDate());
                    enrollment.setStatus(EnrollmentStatus.valueOf(rs.getString("status")));
                    return Optional.of(enrollment);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching enrollment by ID", e);
        }
        return Optional.empty();
    }
    public boolean deleteById(UUID id) {
        String deleteSQL = "DELETE FROM public.\"enrollment\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting enrollment by ID", e);
        }
    }
}

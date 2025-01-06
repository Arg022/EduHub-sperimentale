package com.prosperi.argeo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prosperi.argeo.enums.EnrollmentStatus;
import com.prosperi.argeo.model.Enrollment;
import com.prosperi.argeo.util.database.DatabaseConnection;

public class EnrollmentDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Enrollment addEnrollment(Enrollment enrollment) {
        String insertSQL = "INSERT INTO public.\"enrollment\" (id, user_id, course_id, enrollment_date, status) VALUES (?, ?, ?, ?, ?::enrollment_status)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            UUID id = UUID.randomUUID();
            ps.setObject(1, id);
            ps.setObject(2, enrollment.getUserId());
            ps.setObject(3, enrollment.getCourseId());
            ps.setDate(4, enrollment.getEnrollmentDate() != null ? Date.valueOf(enrollment.getEnrollmentDate()) : new Date(System.currentTimeMillis()));
            ps.setObject(5, enrollment.getStatus() != null ? enrollment.getStatus().name().toLowerCase() : "pending");

            ps.executeUpdate();
            enrollment.setId(id);
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
                enrollment.setStatus(EnrollmentStatus.valueOf(rs.getString("status").toUpperCase())); // Converti in maiuscolo
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
                    enrollment.setStatus(EnrollmentStatus.valueOf(rs.getString("status").toUpperCase())); // Converti in maiuscolo
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
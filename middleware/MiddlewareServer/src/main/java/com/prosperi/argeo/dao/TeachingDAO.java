package com.prosperi.argeo.dao;

import com.prosperi.argeo.model.Teaching;
import com.prosperi.argeo.util.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TeachingDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Teaching addTeaching(Teaching teaching) {
        String insertSQL = "INSERT INTO public.\"teaching\" (user_id, subject_id, course_id, start_date, end_date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setObject(1, teaching.getUserId());
            ps.setObject(2, teaching.getSubjectId());
            ps.setObject(3, teaching.getCourseId());
            ps.setDate(4, Date.valueOf(teaching.getStartDate()));
            ps.setDate(5, Date.valueOf(teaching.getEndDate()));

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding teaching", e);
        }
        return teaching;
    }

    public List<Teaching> getAllTeachings() {
        String selectSQL = "SELECT * FROM public.\"teaching\"";
        List<Teaching> teachings = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Teaching teaching = new Teaching();
                teaching.setId(rs.getObject("id", java.util.UUID.class));
                teaching.setUserId(rs.getObject("user_id", java.util.UUID.class));
                teaching.setSubjectId(rs.getObject("subject_id", java.util.UUID.class));
                teaching.setCourseId(rs.getObject("course_id", java.util.UUID.class));
                teaching.setStartDate(rs.getDate("start_date").toLocalDate());
                teaching.setEndDate(rs.getDate("end_date").toLocalDate());
                teachings.add(teaching);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all teachings", e);
        }
        return teachings;
    }

    public Optional<Teaching> findTeachingById(UUID id) {
        String selectSQL = "SELECT * FROM public.\"teaching\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Teaching teaching = new Teaching();
                    teaching.setId(rs.getObject("id", java.util.UUID.class));
                    teaching.setUserId(rs.getObject("user_id", java.util.UUID.class));
                    teaching.setSubjectId(rs.getObject("subject_id", java.util.UUID.class));
                    teaching.setCourseId(rs.getObject("course_id", java.util.UUID.class));
                    teaching.setStartDate(rs.getDate("start_date").toLocalDate());
                    teaching.setEndDate(rs.getDate("end_date").toLocalDate());
                    return Optional.of(teaching);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching teaching by ID", e);
        }
        return Optional.empty();
    }

    public boolean deleteById(UUID id) {
        String deleteSQL = "DELETE FROM public.\"teaching\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting teaching by ID", e);
        }
    }
}

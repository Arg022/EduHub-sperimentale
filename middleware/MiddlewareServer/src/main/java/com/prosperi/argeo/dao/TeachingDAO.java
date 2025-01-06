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

import com.prosperi.argeo.model.Teaching;
import com.prosperi.argeo.util.database.DatabaseConnection;

public class TeachingDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Teaching addTeaching(Teaching teaching) {
        String insertSQL = "INSERT INTO public.\"teaching\" (id, user_id, subject_id, course_id, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            UUID id = UUID.randomUUID();
            ps.setObject(1, id);
            ps.setObject(2, teaching.getUserId());
            ps.setObject(3, teaching.getSubjectId());
            ps.setObject(4, teaching.getCourseId());
            ps.setDate(5, Date.valueOf(teaching.getStartDate()));
            ps.setDate(6, Date.valueOf(teaching.getEndDate()));

            ps.executeUpdate();
            teaching.setId(id); // Imposta l'ID generato nell'insegnamento
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

package com.prosperi.argeo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prosperi.argeo.model.Subject;
import com.prosperi.argeo.util.database.DatabaseConnection;

public class SubjectDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Subject addSubject(Subject subject) {
        String insertSQL = "INSERT INTO public.\"subject\" (id, name, description) VALUES (?, ?, ?)";
    
        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            UUID id = UUID.randomUUID();
            ps.setObject(1, id);
            ps.setString(2, subject.getName());
            ps.setString(3, subject.getDescription());
            ps.executeUpdate();
            subject.setId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error adding subject", e);
        }
        return subject;
    }

    public List<Subject> getAllSubjects() {
        String selectSQL = "SELECT * FROM public.\"subject\"";
        List<Subject> subjects = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Subject subject = new Subject();
                subject.setId(rs.getObject("id", UUID.class));
                subject.setName(rs.getString("name"));
                subject.setDescription(rs.getString("description"));
                subjects.add(subject);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all subjects", e);
        }
        return subjects;
    }

    public List<Subject> getSubjectsByStudentId(UUID studentId) {
        String selectSQL = "SELECT DISTINCT s.* FROM public.\"subject\" s " +
                           "JOIN public.\"teaching\" t ON s.id = t.subject_id " +
                           "JOIN public.\"course\" c ON t.course_id = c.id " +
                           "JOIN public.\"enrollment\" e ON c.id = e.course_id " +
                           "WHERE e.user_id = ?";
        List<Subject> subjects = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setId(rs.getObject("id", UUID.class));
                    subject.setName(rs.getString("name"));
                    subject.setDescription(rs.getString("description"));
                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching subjects by student ID", e);
        }

        return subjects;
    }

    public List<Subject> getSubjectsByTeacherId(UUID teacherId) {
        String selectSQL = "SELECT s.* FROM public.\"subject\" s " +
                           "JOIN public.\"teaching\" t ON s.id = t.subject_id " +
                           "WHERE t.user_id = ?";
        List<Subject> subjects = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setId(rs.getObject("id", UUID.class));
                    subject.setName(rs.getString("name"));
                    subject.setDescription(rs.getString("description"));
                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching subjects by teacher ID", e);
        }
        return subjects;
    }

    public Optional<Subject> findSubjectById(UUID id) {
        String selectSQL = "SELECT * FROM public.\"subject\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Subject subject = new Subject();
                    subject.setId(rs.getObject("id", UUID.class));
                    subject.setName(rs.getString("name"));
                    subject.setDescription(rs.getString("description"));
                    return Optional.of(subject);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching subject by ID", e);
        }
        return Optional.empty();
    }

    public boolean deleteById(UUID id) {
        String deleteSQL = "DELETE FROM public.\"subject\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting subject by ID", e);
        }
    }
}
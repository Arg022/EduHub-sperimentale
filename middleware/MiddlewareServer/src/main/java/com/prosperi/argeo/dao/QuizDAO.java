package com.prosperi.argeo.dao;

import com.prosperi.argeo.model.Quiz;
import com.prosperi.argeo.util.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuizDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Quiz addQuiz(Quiz quiz) {
        String insertSQL = "INSERT INTO public.\"quiz\" (course_id, creator_id, title, description, duration_minutes, max_attempts, creation_date, publication_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setObject(1, quiz.getCourseId());
            ps.setObject(2, quiz.getCreatorId());
            ps.setString(3, quiz.getTitle());
            ps.setString(4, quiz.getDescription());
            ps.setInt(5, quiz.getDurationMinutes());
            ps.setInt(6, quiz.getMaxAttempts());
            ps.setTimestamp(7, Timestamp.valueOf(quiz.getCreationDate()));
            ps.setTimestamp(8, Timestamp.valueOf(quiz.getPublicationDate()));

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding quiz", e);
        }
        return quiz;
    }

    public List<Quiz> getAllQuizzes() {
        String selectSQL = "SELECT * FROM public.\"quiz\"";
        List<Quiz> quizzes = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setId(rs.getObject("id", java.util.UUID.class));
                quiz.setCourseId(rs.getObject("course_id", java.util.UUID.class));
                quiz.setCreatorId(rs.getObject("creator_id", java.util.UUID.class));
                quiz.setTitle(rs.getString("title"));
                quiz.setDescription(rs.getString("description"));
                quiz.setDurationMinutes(rs.getInt("duration_minutes"));
                quiz.setMaxAttempts(rs.getInt("max_attempts"));
                quiz.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                quiz.setPublicationDate(rs.getTimestamp("publication_date").toLocalDateTime());
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all quizzes", e);
        }
        return quizzes;
    }

    public Optional<Quiz> findQuizById(UUID id) {
        String selectSQL = "SELECT * FROM public.\"quiz\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Quiz quiz = new Quiz();
                    quiz.setId(rs.getObject("id", java.util.UUID.class));
                    quiz.setCourseId(rs.getObject("course_id", java.util.UUID.class));
                    quiz.setCreatorId(rs.getObject("creator_id", java.util.UUID.class));
                    quiz.setTitle(rs.getString("title"));
                    quiz.setDescription(rs.getString("description"));
                    quiz.setDurationMinutes(rs.getInt("duration_minutes"));
                    quiz.setMaxAttempts(rs.getInt("max_attempts"));
                    quiz.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                    quiz.setPublicationDate(rs.getTimestamp("publication_date").toLocalDateTime());
                    return Optional.of(quiz);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching quiz by ID", e);
        }
        return Optional.empty();
    }

    public boolean deleteById(UUID id) {
        String deleteSQL = "DELETE FROM public.\"quiz\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting quiz by ID", e);
        }
    }
}

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

import com.prosperi.argeo.model.Quiz;
import com.prosperi.argeo.util.database.DatabaseConnection;

public class QuizDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Quiz addQuiz(Quiz quiz) {
        String insertSQL = "INSERT INTO public.\"quiz\" (id, course_id, creator_id, title, description, duration_minutes, max_attempts, creation_date, publication_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            UUID id = UUID.randomUUID();
            ps.setObject(1, id);
            ps.setObject(2, quiz.getCourseId());
            ps.setObject(3, quiz.getCreatorId());
            ps.setString(4, quiz.getTitle());
            ps.setString(5, quiz.getDescription());
            ps.setInt(6, quiz.getDurationMinutes());
            ps.setInt(7, quiz.getMaxAttempts());
            ps.setTimestamp(8, Timestamp.valueOf(quiz.getCreationDate()));
            ps.setTimestamp(9, Timestamp.valueOf(quiz.getPublicationDate()));

            ps.executeUpdate();
            quiz.setId(id); // Imposta l'ID generato nel quiz
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

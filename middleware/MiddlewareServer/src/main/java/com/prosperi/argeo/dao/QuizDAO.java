package com.prosperi.argeo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

        LocalDateTime creationDate = quiz.getCreationDate();
        if (creationDate == null) {
            creationDate = LocalDateTime.now(); 
        }
        ps.setTimestamp(8, Timestamp.valueOf(creationDate));

        LocalDateTime publicationDate = quiz.getPublicationDate();
        if (publicationDate == null) {
            publicationDate = LocalDateTime.now(); 
        }
        ps.setTimestamp(9, Timestamp.valueOf(publicationDate));

        ps.executeUpdate();
        quiz.setId(id);
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

    public List<Quiz> getQuizzesByStudentId(UUID studentId) {
        String selectSQL = "SELECT q.* FROM public.\"quiz\" q " +
                           "JOIN public.\"course\" c ON q.course_id = c.id " +
                           "JOIN public.\"enrollment\" e ON c.id = e.course_id " +
                           "WHERE e.user_id = ?";
        List<Quiz> quizzes = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Quiz quiz = new Quiz();
                    quiz.setId(rs.getObject("id", UUID.class));
                    quiz.setCourseId(rs.getObject("course_id", UUID.class));
                    quiz.setCreatorId(rs.getObject("creator_id", UUID.class));
                    quiz.setTitle(rs.getString("title"));
                    quiz.setDescription(rs.getString("description"));
                    quiz.setDurationMinutes(rs.getInt("duration_minutes"));
                    quiz.setMaxAttempts(rs.getInt("max_attempts"));
                    quiz.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                    quiz.setPublicationDate(rs.getTimestamp("publication_date").toLocalDateTime());
                    quizzes.add(quiz);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching quizzes by student ID", e);
        }
        return quizzes;
    }

    public List<Quiz> getQuizzesByTeacherId(UUID teacherId) {
        String selectSQL = "SELECT q.* FROM public.\"quiz\" q " +
                           "WHERE q.creator_id = ?";
        List<Quiz> quizzes = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Quiz quiz = new Quiz();
                    quiz.setId(rs.getObject("id", UUID.class));
                    quiz.setCourseId(rs.getObject("course_id", UUID.class));
                    quiz.setCreatorId(rs.getObject("creator_id", UUID.class));
                    quiz.setTitle(rs.getString("title"));
                    quiz.setDescription(rs.getString("description"));
                    quiz.setDurationMinutes(rs.getInt("duration_minutes"));
                    quiz.setMaxAttempts(rs.getInt("max_attempts"));
                    quiz.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                    quiz.setPublicationDate(rs.getTimestamp("publication_date").toLocalDateTime());
                    quizzes.add(quiz);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching quizzes by teacher ID", e);
        }
        return quizzes;
    }
}

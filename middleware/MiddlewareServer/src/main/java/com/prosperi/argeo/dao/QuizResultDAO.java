package com.prosperi.argeo.dao;

import com.prosperi.argeo.model.QuizResult;
import com.prosperi.argeo.util.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuizResultDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public QuizResult addQuizResult(QuizResult quizResult) {
        String insertSQL = "INSERT INTO public.\"quiz_result\" (quiz_id, user_id, total_score, start_time, completion_time, time_spent) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setObject(1, quizResult.getQuizId());
            ps.setObject(2, quizResult.getUserId());
            ps.setFloat(3, quizResult.getTotalScore());
            ps.setTimestamp(4, Timestamp.valueOf(quizResult.getStartTime()));
            ps.setTimestamp(5, Timestamp.valueOf(quizResult.getCompletionTime()));
            ps.setInt(6, quizResult.getTimeSpent());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding quiz result", e);
        }
        return quizResult;
    }

    public List<QuizResult> getAllQuizResults() {
        String selectSQL = "SELECT * FROM public.\"quiz_result\"";
        List<QuizResult> results = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                QuizResult result = new QuizResult();
                result.setId(rs.getObject("id", java.util.UUID.class));
                result.setQuizId(rs.getObject("quiz_id", java.util.UUID.class));
                result.setUserId(rs.getObject("user_id", java.util.UUID.class));
                result.setTotalScore(rs.getFloat("total_score"));
                result.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                result.setCompletionTime(rs.getTimestamp("completion_time").toLocalDateTime());
                result.setTimeSpent(rs.getInt("time_spent"));
                results.add(result);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all quiz results", e);
        }
        return results;
    }

    public Optional<QuizResult> findQuizResultById(UUID id) {
        String selectSQL = "SELECT * FROM public.\"quiz_result\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    QuizResult result = new QuizResult();
                    result.setId(rs.getObject("id", java.util.UUID.class));
                    result.setQuizId(rs.getObject("quiz_id", java.util.UUID.class));
                    result.setUserId(rs.getObject("user_id", java.util.UUID.class));
                    result.setTotalScore(rs.getFloat("total_score"));
                    result.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                    result.setCompletionTime(rs.getTimestamp("completion_time").toLocalDateTime());
                    result.setTimeSpent(rs.getInt("time_spent"));
                    return Optional.of(result);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching quiz result by ID", e);
        }
        return Optional.empty();
    }
    public boolean deleteById(UUID id) {
        String deleteSQL = "DELETE FROM public.\"quiz_result\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting quiz result by ID", e);
        }
    }
}

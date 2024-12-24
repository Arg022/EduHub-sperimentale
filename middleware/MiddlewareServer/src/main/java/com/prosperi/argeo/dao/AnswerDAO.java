package com.prosperi.argeo.dao;

import com.prosperi.argeo.model.Answer;
import com.prosperi.argeo.util.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AnswerDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Answer addAnswer(Answer answer) {
        String insertSQL = "INSERT INTO public.\"answer\" (question_id, text, is_correct) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setObject(1, answer.getQuestionId());
            ps.setString(2, answer.getText());
            ps.setBoolean(3, answer.isCorrect());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding answer", e);
        }
        return answer;
    }

    public List<Answer> getAllAnswers() {
        String selectSQL = "SELECT * FROM public.\"answer\"";
        List<Answer> answers = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Answer answer = new Answer();
                answer.setId(rs.getObject("id", java.util.UUID.class));
                answer.setQuestionId(rs.getObject("question_id", java.util.UUID.class));
                answer.setText(rs.getString("text"));
                answer.setCorrect(rs.getBoolean("is_correct"));
                answers.add(answer);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all answers", e);
        }
        return answers;
    }

    public Optional<Answer> findAnswerById(UUID id) {
        String selectSQL = "SELECT * FROM public.\"answer\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Answer answer = new Answer();
                    answer.setId(rs.getObject("id", java.util.UUID.class));
                    answer.setQuestionId(rs.getObject("question_id", java.util.UUID.class));
                    answer.setText(rs.getString("text"));
                    answer.setCorrect(rs.getBoolean("is_correct"));
                    return Optional.of(answer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching answer by ID", e);
        }
        return Optional.empty();
    }

    public boolean deleteById(UUID id) {
        String deleteSQL = "DELETE FROM public.\"answer\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting answer by ID", e);
        }
    }
}

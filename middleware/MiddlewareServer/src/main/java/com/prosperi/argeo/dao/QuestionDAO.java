package com.prosperi.argeo.dao;

import com.prosperi.argeo.enums.QuestionType;
import com.prosperi.argeo.model.Question;
import com.prosperi.argeo.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuestionDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Question addQuestion(Question question) {
        String insertSQL = "INSERT INTO public.\"question\" (quiz_id, text, score, question_type) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setObject(1, question.getQuizId());
            ps.setString(2, question.getText());
            ps.setFloat(3, question.getScore());
            ps.setString(4, question.getQuestionType().name());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding question", e);
        }
        return question;
    }

    public List<Question> getAllQuestions() {
        String selectSQL = "SELECT * FROM public.\"question\"";
        List<Question> questions = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Question question = new Question();
                question.setId(rs.getObject("id", java.util.UUID.class));
                question.setQuizId(rs.getObject("quiz_id", java.util.UUID.class));
                question.setText(rs.getString("text"));
                question.setScore(rs.getFloat("score"));
                question.setQuestionType(QuestionType.valueOf(rs.getString("question_type")));
                questions.add(question);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all questions", e);
        }
        return questions;
    }

    public Optional<Question> findQuestionById(UUID id) {
        String selectSQL = "SELECT * FROM public.\"question\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Question question = new Question();
                    question.setId(rs.getObject("id", java.util.UUID.class));
                    question.setQuizId(rs.getObject("quiz_id", java.util.UUID.class));
                    question.setText(rs.getString("text"));
                    question.setScore(rs.getFloat("score"));
                    question.setQuestionType(QuestionType.valueOf(rs.getString("question_type")));
                    return Optional.of(question);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching question by ID", e);
        }
        return Optional.empty();
    }

    public boolean deleteById(UUID id) {
        String deleteSQL = "DELETE FROM public.\"question\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting question by ID", e);
        }
    }
}

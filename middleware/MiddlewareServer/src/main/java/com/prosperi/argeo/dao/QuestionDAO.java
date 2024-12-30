package com.prosperi.argeo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prosperi.argeo.enums.QuestionType;
import com.prosperi.argeo.model.Question;
import com.prosperi.argeo.util.database.DatabaseConnection;

public class QuestionDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Question addQuestion(Question question) {
        String insertSQL = "INSERT INTO public.\"question\" (id, quiz_id, text, score, question_type) VALUES (?, ?, ?, ?, ?::question_type)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            UUID id = UUID.randomUUID();
            ps.setObject(1, id);
            ps.setObject(2, question.getQuizId());
            ps.setString(3, question.getText());
            ps.setFloat(4, question.getScore());
            ps.setString(5, question.getQuestionType().name().toLowerCase());

            ps.executeUpdate();
            question.setId(id); // Imposta l'ID generato nella domanda
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

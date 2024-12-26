package com.prosperi.argeo.dao;

import com.prosperi.argeo.model.StudentAnswer;
import com.prosperi.argeo.util.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StudentAnswerDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public StudentAnswer addStudentAnswer(StudentAnswer studentAnswer) {
        String insertSQL = "INSERT INTO public.\"student_answer\" (quiz_result_id, question_id, answer_id, score_obtained) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setObject(1, studentAnswer.getQuizResultId());
            ps.setObject(2, studentAnswer.getQuestionId());
            ps.setObject(3, studentAnswer.getAnswerId());
            ps.setFloat(4, studentAnswer.getScoreObtained());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding student answer", e);
        }
        return studentAnswer;
    }

    public List<StudentAnswer> getAllStudentAnswers() {
        String selectSQL = "SELECT * FROM public.\"student_answer\"";
        List<StudentAnswer> studentAnswers = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                StudentAnswer studentAnswer = new StudentAnswer();
                studentAnswer.setId(rs.getObject("id", java.util.UUID.class));
                studentAnswer.setQuizResultId(rs.getObject("quiz_result_id", java.util.UUID.class));
                studentAnswer.setQuestionId(rs.getObject("question_id", java.util.UUID.class));
                studentAnswer.setAnswerId(rs.getObject("answer_id", java.util.UUID.class));
                studentAnswer.setScoreObtained(rs.getFloat("score_obtained"));
                studentAnswers.add(studentAnswer);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all student answers", e);
        }
        return studentAnswers;
    }

    public Optional<StudentAnswer> findStudentAnswerById(UUID id) {
        String selectSQL = "SELECT * FROM public.\"student_answer\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StudentAnswer studentAnswer = new StudentAnswer();
                    studentAnswer.setId(rs.getObject("id", java.util.UUID.class));
                    studentAnswer.setQuizResultId(rs.getObject("quiz_result_id", java.util.UUID.class));
                    studentAnswer.setQuestionId(rs.getObject("question_id", java.util.UUID.class));
                    studentAnswer.setAnswerId(rs.getObject("answer_id", java.util.UUID.class));
                    studentAnswer.setScoreObtained(rs.getFloat("score_obtained"));
                    return Optional.of(studentAnswer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching student answer by ID", e);
        }
        return Optional.empty();
    }

    public boolean deleteById(UUID id) {
        String deleteSQL = "DELETE FROM public.\"student_answer\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting student answer by ID", e);
        }
    }
}

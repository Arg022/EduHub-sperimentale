package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.StudentAnswerDAO;
import com.prosperi.argeo.model.StudentAnswer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StudentAnswerService {
    private final StudentAnswerDAO studentAnswerDAO = new StudentAnswerDAO();


    public StudentAnswer createStudentAnswer(StudentAnswer studentAnswer) {
        return studentAnswerDAO.addStudentAnswer(studentAnswer);
    }

    public List<StudentAnswer> getAllStudentAnswers() {
        return studentAnswerDAO.getAllStudentAnswers();
    }

    public Optional<StudentAnswer> getStudentAnswerById(UUID id) {
        return studentAnswerDAO.findStudentAnswerById(id);
    }

    public boolean deleteStudentAnswerById(UUID id) {
        return studentAnswerDAO.deleteById(id);
    }
}

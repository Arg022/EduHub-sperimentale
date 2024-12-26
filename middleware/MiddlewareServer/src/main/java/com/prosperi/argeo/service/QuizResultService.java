package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.QuizResultDAO;
import com.prosperi.argeo.model.QuizResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuizResultService {
    private final QuizResultDAO quizResultDAO = new QuizResultDAO();

    public QuizResult createQuizResult(QuizResult quizResult) {
        return quizResultDAO.addQuizResult(quizResult);
    }

    public List<QuizResult> getAllQuizResults() {
        return quizResultDAO.getAllQuizResults();
    }

    public Optional<QuizResult> getQuizResultById(UUID id) {
        return quizResultDAO.findQuizResultById(id);
    }

    public boolean deleteQuizResultById(UUID id) {
        return quizResultDAO.deleteById(id);
    }
}

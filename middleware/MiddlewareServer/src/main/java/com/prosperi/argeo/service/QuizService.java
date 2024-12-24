package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.QuizDAO;
import com.prosperi.argeo.model.Quiz;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuizService {
    private final QuizDAO quizDAO;

    public QuizService(QuizDAO quizDAO) {
        this.quizDAO = quizDAO;
    }

    public Quiz createQuiz(Quiz quiz) {
        return quizDAO.addQuiz(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizDAO.getAllQuizzes();
    }

    public Optional<Quiz> getQuizById(UUID id) {
        return quizDAO.findQuizById(id);
    }

    public boolean deleteQuizById(UUID id) {
        return quizDAO.deleteById(id);
    }
}

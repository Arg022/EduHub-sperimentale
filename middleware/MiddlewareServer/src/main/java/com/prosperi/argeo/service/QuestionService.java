package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.QuestionDAO;
import com.prosperi.argeo.model.Question;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuestionService {
    private final QuestionDAO questionDAO = new QuestionDAO();

    public Question createQuestion(Question question) {
        return questionDAO.addQuestion(question);
    }

    public List<Question> getAllQuestions() {
        return questionDAO.getAllQuestions();
    }

    public Optional<Question> getQuestionById(UUID id) {
        return questionDAO.findQuestionById(id);
    }

    public boolean deleteQuestionById(UUID id) {
        return questionDAO.deleteById(id);
    }
}

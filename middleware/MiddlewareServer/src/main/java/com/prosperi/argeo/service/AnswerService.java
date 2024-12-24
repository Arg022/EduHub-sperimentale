package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.AnswerDAO;
import com.prosperi.argeo.model.Answer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AnswerService {
    private final AnswerDAO answerDAO = new AnswerDAO();

    public Answer createAnswer(Answer answer) {
        return answerDAO.addAnswer(answer);
    }

    public List<Answer> getAllAnswers() {
        return answerDAO.getAllAnswers();
    }

    public Optional<Answer> getAnswerById(UUID id) {
        return answerDAO.findAnswerById(id);
    }

    public boolean deleteAnswerById(UUID id) {
        return answerDAO.deleteById(id);
    }
}

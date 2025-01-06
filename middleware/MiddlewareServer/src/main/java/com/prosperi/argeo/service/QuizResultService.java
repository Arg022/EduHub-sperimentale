package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.QuizResultDAO;
import com.prosperi.argeo.dao.AnswerDAO;
import com.prosperi.argeo.dao.QuestionDAO;
import com.prosperi.argeo.dao.StudentAnswerDAO;
import com.prosperi.argeo.model.QuizResult;
import com.prosperi.argeo.model.StudentAnswer;
import com.prosperi.argeo.model.Answer;
import com.prosperi.argeo.model.Question;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QuizResultService {
    private final QuizResultDAO quizResultDAO = new QuizResultDAO();
    private final AnswerDAO answerDAO = new AnswerDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final StudentAnswerDAO studentAnswerDAO = new StudentAnswerDAO();

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

    public QuizResult calculateAndSaveQuizResult(QuizResult quizResult, List<StudentAnswer> studentAnswers) {
        float totalScore = 0;

        for (StudentAnswer studentAnswer : studentAnswers) {
            Optional<Answer> correctAnswerOpt = answerDAO.findAnswerById(studentAnswer.getAnswerId());
            Optional<Question> questionOpt = questionDAO.findQuestionById(studentAnswer.getQuestionId());
            if (correctAnswerOpt.isPresent() && correctAnswerOpt.get().isCorrect() && questionOpt.isPresent()) {
                totalScore += questionOpt.get().getScore();
                studentAnswer.setScoreObtained(questionOpt.get().getScore());
            } else {
                studentAnswer.setScoreObtained(0f);
            }
        }

        quizResult.setTotalScore(totalScore);
        quizResultDAO.addQuizResult(quizResult);
        studentAnswers.forEach(answer -> studentAnswerDAO.addStudentAnswer(answer));

        return quizResult;
    }
}
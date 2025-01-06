package com.prosperi.argeo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prosperi.argeo.dao.AnswerDAO;
import com.prosperi.argeo.dao.QuestionDAO;
import com.prosperi.argeo.dao.QuizDAO;
import com.prosperi.argeo.model.Answer;
import com.prosperi.argeo.model.Question;
import com.prosperi.argeo.model.Quiz;

public class QuizService {
    private final QuizDAO quizDAO = new QuizDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final AnswerDAO answerDAO = new AnswerDAO();

    public Quiz createQuiz(Quiz quiz, List<Question> questions) {
        quizDAO.addQuiz(quiz);
        for (Question question : questions) {
            question.setQuizId(quiz.getId());
            questionDAO.addQuestion(question);
            for (Answer answer : question.getAnswers()) {
                answer.setQuestionId(question.getId());
                answerDAO.addAnswer(answer);
            }
        }
        return quiz;
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
    public List<Quiz> getQuizzesByStudentId(UUID studentId) {
        return quizDAO.getQuizzesByStudentId(studentId);
    }

    public List<Quiz> getQuizzesByTeacherId(UUID teacherId) {
        return quizDAO.getQuizzesByTeacherId(teacherId);
    }
}

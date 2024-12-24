package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.LessonDAO;
import com.prosperi.argeo.model.Lesson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LessonService {
    private final LessonDAO lessonDAO = new LessonDAO();


    public Lesson createLesson(Lesson lesson) {
        return lessonDAO.addLesson(lesson);
    }

    public List<Lesson> getAllLessons() {
        return lessonDAO.getAllLessons();
    }

    public Optional<Lesson> getLessonById(UUID id) {
        return lessonDAO.findLessonById(id);
    }

    public boolean deleteLessonById(UUID id) {
        return lessonDAO.deleteById(id);
    }
}

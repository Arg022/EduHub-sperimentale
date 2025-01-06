package com.prosperi.argeo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prosperi.argeo.dao.LessonDAO;
import com.prosperi.argeo.model.Lesson;

public class LessonService {
    private final LessonDAO lessonDAO = new LessonDAO();

    public Lesson createLesson(Lesson lesson) {
        return lessonDAO.addLesson(lesson);
    }

    public List<Lesson> getAllLessons() {
        return lessonDAO.getAllLessons();
    }

    public List<Lesson> getLessonsByStudentId(UUID studentId) {
        return lessonDAO.getLessonsByStudentId(studentId);
    }

    public List<Lesson> getLessonsByTeacherId(UUID teacherId) {
        return lessonDAO.getLessonsByTeacherId(teacherId);
    }

    public Optional<Lesson> getLessonById(UUID id) {
        return lessonDAO.findLessonById(id);
    }

    public boolean deleteLessonById(UUID id) {
        return lessonDAO.deleteById(id);
    }
}
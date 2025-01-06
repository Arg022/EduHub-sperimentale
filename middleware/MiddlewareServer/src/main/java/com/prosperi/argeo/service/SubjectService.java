package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.SubjectDAO;
import com.prosperi.argeo.model.Subject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SubjectService {
    private final SubjectDAO subjectDAO = new SubjectDAO();

    public Subject createSubject(Subject subject) {
        return subjectDAO.addSubject(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectDAO.getAllSubjects();
    }

    public List<Subject> getSubjectsByStudentId(UUID studentId) {
        return subjectDAO.getSubjectsByStudentId(studentId);
    }

    public List<Subject> getSubjectsByTeacherId(UUID teacherId) {
        return subjectDAO.getSubjectsByTeacherId(teacherId);
    }

    public Optional<Subject> getSubjectById(UUID id) {
        return subjectDAO.findSubjectById(id);
    }

    public boolean deleteSubjectById(UUID id) {
        return subjectDAO.deleteById(id);
    }
}
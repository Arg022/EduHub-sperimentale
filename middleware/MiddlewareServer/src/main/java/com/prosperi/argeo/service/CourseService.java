package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.CourseDAO;
import com.prosperi.argeo.model.Course;
import com.prosperi.argeo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CourseService {
    private final CourseDAO courseDAO = new CourseDAO();
    
    public Course createCourse(Course course) {
        return courseDAO.addCourse(course);
    }

    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    public List<Course> getCoursesByStudentId(UUID studentId) {
        return courseDAO.getCoursesByStudentId(studentId);
    }

    public List<Course> getCoursesByTeacherId(UUID teacherId) {
        return courseDAO.getCoursesByTeacherId(teacherId);
    }

    public Optional<Course> getCourseById(UUID id) {
        return courseDAO.findCourseById(id);
    }

    public boolean deleteCourseById(UUID id) {
        return courseDAO.deleteById(id);
    }

    public List<User> getStudentsByCourseId(UUID courseId) {
        return courseDAO.findStudentsByCourseId(courseId);
    }

    public List<User> getTeachersByCourseId(UUID courseId) {
        return courseDAO.getTeachersByCourseId(courseId);
    }
}
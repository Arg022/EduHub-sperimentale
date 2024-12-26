package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.CourseDAO;
import com.prosperi.argeo.model.Course;

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

    public Optional<Course> getCourseById(UUID id) {
        return courseDAO.findCourseById(id);
    }

    public boolean deleteCourseById(UUID id) {
        return courseDAO.deleteById(id);
    }
}

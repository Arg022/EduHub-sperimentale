package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.EnrollmentDAO;
import com.prosperi.argeo.model.Enrollment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EnrollmentService {
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();


    public Enrollment createEnrollment(Enrollment enrollment) {
        return enrollmentDAO.addEnrollment(enrollment);
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentDAO.getAllEnrollments();
    }

    public Optional<Enrollment> getEnrollmentById(UUID id) {
        return enrollmentDAO.findEnrollmentById(id);
    }

    public boolean deleteEnrollmentById(UUID id) {
        return enrollmentDAO.deleteById(id);
    }
}

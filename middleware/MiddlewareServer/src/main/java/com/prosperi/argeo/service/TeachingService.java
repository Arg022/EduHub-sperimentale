package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.TeachingDAO;
import com.prosperi.argeo.model.Teaching;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TeachingService {
    private final TeachingDAO teachingDAO = new TeachingDAO();


    public Teaching createTeaching(Teaching teaching) {
        return teachingDAO.addTeaching(teaching);
    }

    public List<Teaching> getAllTeachings() {
        return teachingDAO.getAllTeachings();
    }

    public Optional<Teaching> getTeachingById(UUID id) {
        return teachingDAO.findTeachingById(id);
    }

    public boolean deleteTeachingById(UUID id) {
        return teachingDAO.deleteById(id);
    }
}

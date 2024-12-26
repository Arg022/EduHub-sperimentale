package com.prosperi.argeo.service;

import com.prosperi.argeo.dao.UserDAO;
import com.prosperi.argeo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public User createUser(User user) {
        return userDAO.addUser(user);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public Optional<User> getUserById(UUID id) {
        return userDAO.findUserById(id);
    }

    public boolean deleteUserById(UUID id) {
        return userDAO.deleteById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }
}
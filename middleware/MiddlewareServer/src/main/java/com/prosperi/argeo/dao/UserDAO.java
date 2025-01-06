package com.prosperi.argeo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prosperi.argeo.enums.UserRole;
import com.prosperi.argeo.model.User;
import com.prosperi.argeo.util.database.DatabaseConnection;

public class UserDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public User addUser(User user) {
        String insertSQL = "INSERT INTO public.\"user_account\" (id, email, password, first_name, last_name, role, registration_date, phone, address, last_access) " +
                "VALUES (?, ?, ?, ?, ?, ?::user_role, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setObject(1, user.getId()); 
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setString(6, user.getRole().name().toLowerCase()); // Usa il nome dell'enum
            ps.setDate(7, Date.valueOf(user.getRegistrationDate()));
            ps.setString(8, user.getPhone());
            ps.setString(9, user.getAddress());
            ps.setTimestamp(10, Timestamp.valueOf(user.getLastAccess())); // Passa il valore corretto di lastAccess

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding user", e);
        }
        return user;
    }

    public List<User> getAllUsers() {
        String selectSQL = "SELECT * FROM public.\"user_account\"";
        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getObject("id", java.util.UUID.class));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setRole(UserRole.valueOf(rs.getString("role")));
                user.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setLastAccess(rs.getTimestamp("last_access").toLocalDateTime());
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all users", e);
        }
        return users;
    }

    public Optional<User> findUserById(UUID id) {
        String selectSQL = "SELECT * FROM public.\"user_account\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = User.builder()
                            .id(rs.getObject("id", java.util.UUID.class))
                            .email(rs.getString("email"))
                            .password(rs.getString("password"))
                            .firstName(rs.getString("first_name"))
                            .lastName(rs.getString("last_name"))
                            .role(UserRole.valueOf(rs.getString("role").toUpperCase())) // Converti il valore in maiuscolo
                            .registrationDate(rs.getDate("registration_date").toLocalDate())
                            .phone(rs.getString("phone"))
                            .address(rs.getString("address"))
                            .lastAccess(rs.getTimestamp("last_access").toLocalDateTime())
                            .build();
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user by id", e);
        }
        return Optional.empty();
    }

    public boolean deleteById(UUID id) {
        String deleteSQL = "DELETE FROM public.\"user_account\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user by ID", e);
        }
    }

    public Optional<User> findByEmail(String email) {
        String selectSQL = "SELECT * FROM public.\"user_account\" WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getObject("id", java.util.UUID.class));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setRole(UserRole.valueOf(rs.getString("role").toUpperCase()));
                    user.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setLastAccess(rs.getTimestamp("last_access").toLocalDateTime());
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user by email", e);
        }
        return Optional.empty();
    }

    public List<User> getAllTeachers() {
        String selectSQL = "SELECT * FROM public.\"user_account\" WHERE role = 'teacher'";
        List<User> teachers = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getObject("id", java.util.UUID.class));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setRole(UserRole.valueOf(rs.getString("role").toUpperCase()));
                user.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setLastAccess(rs.getTimestamp("last_access").toLocalDateTime());
                teachers.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all teachers", e);
        }
        return teachers;
    }

    public List<User> getAllStudents() {
        String selectSQL = "SELECT * FROM public.\"user_account\" WHERE role = 'student'";
        List<User> students = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getObject("id", java.util.UUID.class));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setRole(UserRole.valueOf(rs.getString("role").toUpperCase()));
                user.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setLastAccess(rs.getTimestamp("last_access").toLocalDateTime());
                students.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all students", e);
        }
        return students;
    }
}

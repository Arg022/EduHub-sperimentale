package com.prosperi.argeo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prosperi.argeo.enums.CourseLevel;
import com.prosperi.argeo.enums.UserRole;
import com.prosperi.argeo.model.Course;
import com.prosperi.argeo.model.User;
import com.prosperi.argeo.util.database.DatabaseConnection;

public class CourseDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Course addCourse(Course course) {
        String insertSQL = "INSERT INTO public.course (id, name, description, category, start_date, end_date, syllabus, max_students, level) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::course_level)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            UUID id = UUID.randomUUID();
            ps.setObject(1, id);
            ps.setString(2, course.getName());
            ps.setString(3, course.getDescription());
            ps.setString(4, course.getCategory());
            ps.setDate(5, Date.valueOf(course.getStartDate()));
            ps.setDate(6, Date.valueOf(course.getEndDate()));
            ps.setString(7, course.getSyllabus());
            ps.setInt(8, course.getMaxStudents());
            ps.setString(9, course.getLevel().name().toLowerCase());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding course", e);
        }
        return course;
    }

    public List<Course> getAllCourses() {
        String selectSQL = "SELECT * FROM public.course";
        List<Course> courses = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getObject("id", UUID.class));
                course.setName(rs.getString("name"));
                course.setDescription(rs.getString("description"));
                course.setCategory(rs.getString("category"));
                course.setStartDate(rs.getDate("start_date").toLocalDate());
                course.setEndDate(rs.getDate("end_date").toLocalDate());
                course.setSyllabus(rs.getString("syllabus"));
                course.setMaxStudents(rs.getInt("max_students"));
                course.setLevel(CourseLevel.valueOf(rs.getString("level").toUpperCase()));
                courses.add(course);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all courses", e);
        }
        return courses;
    }

    public Optional<Course> findCourseById(UUID id) {
        String selectSQL = "SELECT * FROM public.course WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Course course = new Course();
                    course.setId(rs.getObject("id", UUID.class));
                    course.setName(rs.getString("name"));
                    course.setDescription(rs.getString("description"));
                    course.setCategory(rs.getString("category"));
                    course.setStartDate(rs.getDate("start_date").toLocalDate());
                    course.setEndDate(rs.getDate("end_date").toLocalDate());
                    course.setSyllabus(rs.getString("syllabus"));
                    course.setMaxStudents(rs.getInt("max_students"));
                    course.setLevel(CourseLevel.valueOf(rs.getString("level").toUpperCase()));
                    return Optional.of(course);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching course by ID", e);
        }
        return Optional.empty();
    }

    public boolean deleteById(UUID id) {
        String deleteSQL = "DELETE FROM public.course WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting course by ID", e);
        }
    }


    public List<User> findStudentsByCourseId(UUID courseId) {
        String selectSQL = "SELECT u.* FROM user_account u " +
                           "JOIN enrollment e ON u.id = e.user_id " +
                           "WHERE e.course_id = ?";
        List<User> students = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User student = new User();
                    student.setId(rs.getObject("id", UUID.class));
                    student.setFirstName(rs.getString("first_name"));
                    student.setLastName(rs.getString("last_name"));
                    student.setEmail(rs.getString("email"));
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching students by course ID", e);
        }
        return students;
    }

    public List<User> getTeachersByCourseId(UUID courseId) {
        String selectSQL = "SELECT u.* FROM public.\"user_account\" u JOIN public.\"teaching\" t ON u.id = t.user_id WHERE t.course_id = ?";
        List<User> teachers = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching teachers by course ID", e);
        }
        return teachers;
    }

    public List<Course> getCoursesByStudentId(UUID studentId) {
        String selectSQL = "SELECT c.* FROM public.\"course\" c JOIN public.\"enrollment\" e ON c.id = e.course_id WHERE e.user_id = ?";
        List<Course> courses = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course();
                    course.setId(rs.getObject("id", UUID.class));
                    course.setName(rs.getString("name"));
                    course.setDescription(rs.getString("description"));
                    course.setCategory(rs.getString("category"));
                    course.setStartDate(rs.getDate("start_date").toLocalDate());
                    course.setEndDate(rs.getDate("end_date").toLocalDate());
                    course.setLevel(CourseLevel.valueOf(rs.getString("level").toUpperCase()));
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching courses by student ID", e);
        }
        return courses;
    }

    public List<Course> getCoursesByTeacherId(UUID teacherId) {
        String selectSQL = "SELECT c.* FROM public.\"course\" c JOIN public.\"teaching\" t ON c.id = t.course_id WHERE t.user_id = ?";
        List<Course> courses = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course();
                    course.setId(rs.getObject("id", UUID.class));
                    course.setName(rs.getString("name"));
                    course.setDescription(rs.getString("description"));
                    course.setCategory(rs.getString("category"));
                    course.setStartDate(rs.getDate("start_date").toLocalDate());
                    course.setEndDate(rs.getDate("end_date").toLocalDate());
                    course.setLevel(CourseLevel.valueOf(rs.getString("level").toUpperCase()));
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching courses by teacher ID", e);
        }
        return courses;
    }
    
}
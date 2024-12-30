package com.prosperi.argeo.dao;

import com.prosperi.argeo.enums.CourseLevel;
import com.prosperi.argeo.model.Course;
import com.prosperi.argeo.util.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CourseDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Course addCourse(Course course) {
        String insertSQL = "INSERT INTO public.course (id, name, description, category, start_date, end_date, syllabus, max_students, level) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::course_level)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setObject(1, course.getId());
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
                course.setLevel(CourseLevel.valueOf(rs.getString("level")));
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
                    course.setLevel(CourseLevel.valueOf(rs.getString("level")));
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
}
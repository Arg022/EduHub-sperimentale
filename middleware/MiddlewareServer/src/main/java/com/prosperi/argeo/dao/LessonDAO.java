package com.prosperi.argeo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prosperi.argeo.model.Lesson;
import com.prosperi.argeo.util.database.DatabaseConnection;

public class LessonDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Lesson addLesson(Lesson lesson) {
        String insertSQL = "INSERT INTO public.\"lesson\" (id, course_id, subject_id, title, description, date, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            UUID id = UUID.randomUUID();
            ps.setObject(1, id);
            ps.setObject(2, lesson.getCourseId());
            ps.setObject(3, lesson.getSubjectId());
            ps.setString(4, lesson.getTitle());
            ps.setString(5, lesson.getDescription());
            ps.setDate(6, Date.valueOf(lesson.getDate()));
            ps.setTime(7, Time.valueOf(lesson.getStartTime()));
            ps.setTime(8, Time.valueOf(lesson.getEndTime()));
            ps.executeUpdate();
            lesson.setId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error adding lesson", e);
        }
        return lesson;
    }

    public List<Lesson> getAllLessons() {
        String selectSQL = "SELECT * FROM public.\"lesson\"";
        List<Lesson> lessons = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Lesson lesson = new Lesson();
                lesson.setId(rs.getObject("id", java.util.UUID.class));
                lesson.setCourseId(rs.getObject("course_id", java.util.UUID.class));
                lesson.setSubjectId(rs.getObject("subject_id", java.util.UUID.class));
                lesson.setTitle(rs.getString("title"));
                lesson.setDescription(rs.getString("description"));
                lesson.setDate(rs.getDate("date").toLocalDate());
                lesson.setStartTime(rs.getTime("start_time").toLocalTime());
                lesson.setEndTime(rs.getTime("end_time").toLocalTime());
                lessons.add(lesson);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all lessons", e);
        }
        return lessons;
    }

    public Optional<Lesson> findLessonById(UUID id) {
        String selectSQL = "SELECT * FROM public.\"lesson\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Lesson lesson = new Lesson();
                    lesson.setId(rs.getObject("id", java.util.UUID.class));
                    lesson.setCourseId(rs.getObject("course_id", java.util.UUID.class));
                    lesson.setSubjectId(rs.getObject("subject_id", java.util.UUID.class));
                    lesson.setTitle(rs.getString("title"));
                    lesson.setDescription(rs.getString("description"));
                    lesson.setDate(rs.getDate("date").toLocalDate());
                    lesson.setStartTime(rs.getTime("start_time").toLocalTime());
                    lesson.setEndTime(rs.getTime("end_time").toLocalTime());
                    return Optional.of(lesson);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching lesson by ID", e);
        }
        return Optional.empty();
    }
    public boolean deleteById(UUID id) {
        String deleteSQL = "DELETE FROM public.\"lesson\" WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setObject(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting lesson by ID", e);
        }
    }
}

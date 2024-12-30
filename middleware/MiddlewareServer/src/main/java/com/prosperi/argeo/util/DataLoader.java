package com.prosperi.argeo.util;

import com.prosperi.argeo.dao.*;
import com.prosperi.argeo.enums.*;
import com.prosperi.argeo.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class DataLoader {

    public static void loadInitialData() {
        // DAOs
        UserDAO userDAO = new UserDAO();
        CourseDAO courseDAO = new CourseDAO();
        SubjectDAO subjectDAO = new SubjectDAO();
        LessonDAO lessonDAO = new LessonDAO();
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
        TeachingDAO teachingDAO = new TeachingDAO();
        AttendanceDAO attendanceDAO = new AttendanceDAO();
        QuizDAO quizDAO = new QuizDAO();
        QuestionDAO questionDAO = new QuestionDAO();
        AnswerDAO answerDAO = new AnswerDAO();
        QuizResultDAO quizResultDAO = new QuizResultDAO();
        StudentAnswerDAO studentAnswerDAO = new StudentAnswerDAO();
        NotificationDAO notificationDAO = new NotificationDAO();

        // USERS
        User admin = User.builder()
                .id(UUID.randomUUID())
                .email("admin@example.com")
                .password("admin123") // Usa un sistema di hashing sicuro in produzione
                .firstName("Admin")
                .lastName("User")
                .role(UserRole.ADMIN)
                .registrationDate(LocalDate.now())
                .phone("123456789")
                .address("Admin Street 1")
                .lastAccess(LocalDateTime.now())
                .build();
        userDAO.addUser(admin);

        User teacher = User.builder()
                .id(UUID.randomUUID())
                .email("teacher@example.com")
                .password("teacher123")
                .firstName("Teacher")
                .lastName("User")
                .role(UserRole.TEACHER)
                .registrationDate(LocalDate.now())
                .phone("987654321")
                .address("Teacher Lane 2")
                .lastAccess(LocalDateTime.now())
                .build();
        userDAO.addUser(teacher);

        User student = User.builder()
                .id(UUID.randomUUID())
                .email("student@example.com")
                .password("student123")
                .firstName("Student")
                .lastName("User")
                .role(UserRole.STUDENT)
                .registrationDate(LocalDate.now())
                .phone("123123123")
                .address("Student Avenue 3")
                .lastAccess(LocalDateTime.now())
                .build();
        userDAO.addUser(student);

        // COURSES
        Course course = Course.builder()
                .id(UUID.randomUUID())
                .name("Java Programming")
                .description("Learn the fundamentals of Java programming.")
                .category("Programming")
                .startDate(LocalDate.now().plusDays(5))
                .endDate(LocalDate.now().plusMonths(3))
                .syllabus("Basics, OOP, Streams, Spring")
                .maxStudents(30)
                .level(CourseLevel.BEGINNER)
                .build();
        courseDAO.addCourse(course);

        // SUBJECTS
        Subject subject = Subject.builder()
                .id(UUID.randomUUID())
                .name("Object-Oriented Programming")
                .description("Understand the principles of OOP.")
                .build();
        subjectDAO.addSubject(subject);

        // LESSONS
        Lesson lesson = Lesson.builder()
                .id(UUID.randomUUID())
                .courseId(course.getId())
                .subjectId(subject.getId())
                .title("Introduction to OOP")
                .description("Basic concepts of Object-Oriented Programming.")
                .date(LocalDate.now().plusDays(7))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .build();
        lessonDAO.addLesson(lesson);

        // ENROLLMENTS
        Enrollment enrollment = Enrollment.builder()
                .id(UUID.randomUUID())
                .userId(student.getId())
                .courseId(course.getId())
                .enrollmentDate(LocalDate.now())
                .status(EnrollmentStatus.ACTIVE)
                .build();
        enrollmentDAO.addEnrollment(enrollment);

        // TEACHING
        Teaching teaching = Teaching.builder()
                .id(UUID.randomUUID())
                .userId(teacher.getId())
                .subjectId(subject.getId())
                .courseId(course.getId())
                .startDate(LocalDate.now())
                .endDate(course.getEndDate())
                .build();
        teachingDAO.addTeaching(teaching);

        // ATTENDANCE
        Attendance attendance = Attendance.builder()
                .id(UUID.randomUUID())
                .lessonId(lesson.getId())
                .userId(student.getId())
                .present(true)
                .mode(AttendanceMode.IN_PERSON)
                .notes("Student was on time.")
                .recordTime(LocalDateTime.now())
                .build();
        attendanceDAO.addAttendance(attendance);

        // QUIZ
        Quiz quiz = Quiz.builder()
                .id(UUID.randomUUID())
                .courseId(course.getId())
                .creatorId(teacher.getId())
                .title("Basic OOP Quiz")
                .description("Test your knowledge on basic OOP concepts.")
                .durationMinutes(30)
                .maxAttempts(3)
                .creationDate(LocalDateTime.now())
                .publicationDate(LocalDateTime.now().plusDays(1))
                .build();
        quizDAO.addQuiz(quiz);

        // QUESTIONS AND ANSWERS
        Question question = Question.builder()
                .id(UUID.randomUUID())
                .quizId(quiz.getId())
                .text("What does OOP stand for?")
                .score(1.0f)
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .build();
        questionDAO.addQuestion(question);

        Answer answer1 = Answer.builder()
                .id(UUID.randomUUID())
                .questionId(question.getId())
                .text("Object-Oriented Programming")
                .isCorrect(true)
                .build();
        answerDAO.addAnswer(answer1);

        Answer answer2 = Answer.builder()
                .id(UUID.randomUUID())
                .questionId(question.getId())
                .text("Objective-Oriented Programming")
                .isCorrect(false)
                .build();
        answerDAO.addAnswer(answer2);

        // NOTIFICATIONS
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .senderId(admin.getId())
                .recipientId(student.getId())
                .title("Welcome!")
                .content("Welcome to the platform!")
                .notificationType(NotificationType.SYSTEM)
                .priority(NotificationPriority.HIGH)
                .creationDate(LocalDateTime.now())
                .build();
        notificationDAO.addNotification(notification);
    }
}

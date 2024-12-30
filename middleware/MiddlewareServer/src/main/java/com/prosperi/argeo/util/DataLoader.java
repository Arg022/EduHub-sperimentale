package com.prosperi.argeo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.prosperi.argeo.enums.AttendanceMode;
import com.prosperi.argeo.enums.CourseLevel;
import com.prosperi.argeo.enums.EnrollmentStatus;
import com.prosperi.argeo.enums.NotificationPriority;
import com.prosperi.argeo.enums.NotificationType;
import com.prosperi.argeo.enums.QuestionType;
import com.prosperi.argeo.enums.UserRole;
import com.prosperi.argeo.model.Answer;
import com.prosperi.argeo.model.Attendance;
import com.prosperi.argeo.model.Course;
import com.prosperi.argeo.model.Enrollment;
import com.prosperi.argeo.model.Lesson;
import com.prosperi.argeo.model.Notification;
import com.prosperi.argeo.model.Question;
import com.prosperi.argeo.model.Quiz;
import com.prosperi.argeo.model.Subject;
import com.prosperi.argeo.model.Teaching;
import com.prosperi.argeo.model.User;
import com.prosperi.argeo.service.AnswerService;
import com.prosperi.argeo.service.AttendanceService;
import com.prosperi.argeo.service.CourseService;
import com.prosperi.argeo.service.EnrollmentService;
import com.prosperi.argeo.service.LessonService;
import com.prosperi.argeo.service.NotificationService;
import com.prosperi.argeo.service.QuestionService;
import com.prosperi.argeo.service.QuizService;
import com.prosperi.argeo.service.SubjectService;
import com.prosperi.argeo.service.TeachingService;
import com.prosperi.argeo.service.UserService;

public class DataLoader {

    public static void loadInitialData() {
        // SERVICES
        UserService userService = new UserService();
        CourseService courseService = new CourseService();
        SubjectService subjectService = new SubjectService();
        LessonService lessonService = new LessonService();
        EnrollmentService enrollmentService = new EnrollmentService();
        TeachingService teachingService = new TeachingService();
        AttendanceService attendanceService = new AttendanceService();
        QuizService quizService = new QuizService();
        QuestionService questionService = new QuestionService();
        AnswerService answerService = new AnswerService();
        NotificationService notificationService = new NotificationService();

        // USERS
        User user = User.builder()
                .id(UUID.randomUUID()) // Genera un ID univoco
                .email("admin@example.com")
                .password("securepassword")
                .firstName("Admin")
                .lastName("User")
                .role(UserRole.ADMIN)
                .registrationDate(LocalDate.now())
                .phone("123456789")
                .address("123 Admin Street")
                .lastAccess(LocalDateTime.now())
                .build();
        userService.createUser(user);

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
        userService.createUser(teacher);

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
        userService.createUser(student);

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
        courseService.createCourse(course);

        // SUBJECTS
        Subject subject = Subject.builder()
                .id(UUID.randomUUID())
                .name("Object-Oriented Programming")
                .description("Understand the principles of OOP.")
                .build();
        subjectService.createSubject(subject);

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
        lessonService.createLesson(lesson);

        // ENROLLMENTS
        Enrollment enrollment = Enrollment.builder()
                .id(UUID.randomUUID())
                .userId(student.getId())
                .courseId(course.getId())
                .enrollmentDate(LocalDate.now())
                .status(EnrollmentStatus.ACTIVE)
                .build();
        enrollmentService.createEnrollment(enrollment);

        // TEACHING
        Teaching teaching = Teaching.builder()
                .id(UUID.randomUUID())
                .userId(teacher.getId())
                .subjectId(subject.getId())
                .courseId(course.getId())
                .startDate(LocalDate.now())
                .endDate(course.getEndDate())
                .build();
        teachingService.createTeaching(teaching);

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
        attendanceService.createAttendance(attendance);

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
        quizService.createQuiz(quiz);

        // QUESTIONS AND ANSWERS
        Question question = Question.builder()
                .id(UUID.randomUUID())
                .quizId(quiz.getId())
                .text("What does OOP stand for?")
                .score(1.0f)
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .build();
        questionService.createQuestion(question);

        Answer answer1 = Answer.builder()
                .id(UUID.randomUUID())
                .questionId(question.getId())
                .text("Object-Oriented Programming")
                .isCorrect(true)
                .build();
        answerService.createAnswer(answer1);

        Answer answer2 = Answer.builder()
                .id(UUID.randomUUID())
                .questionId(question.getId())
                .text("Objective-Oriented Programming")
                .isCorrect(false)
                .build();
        answerService.createAnswer(answer2);

        // NOTIFICATIONS
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .senderId(user.getId())
                .recipientId(student.getId())
                .title("Welcome!")
                .content("Welcome to the platform!")
                .notificationType(NotificationType.SYSTEM)
                .priority(NotificationPriority.HIGH)
                .creationDate(LocalDateTime.now())
                .readDate(null) // Assicurati di gestire il campo readDate
                .build();
        notificationService.createNotification(notification);
    }
}
package com.prosperi.argeo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import com.prosperi.argeo.enums.AttendanceMode;
import com.prosperi.argeo.enums.CourseLevel;
import com.prosperi.argeo.enums.EnrollmentStatus;
import com.prosperi.argeo.enums.NotificationPriority;
import com.prosperi.argeo.enums.NotificationType;
import com.prosperi.argeo.enums.QuestionType;
import com.prosperi.argeo.enums.UserRole;
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
        // Initialize services
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

        // Create users
        User admin = createUser(userService, "admin@example.com", "securepassword", "Admin", "User", UserRole.ADMIN, "123456789", "123 Admin Street");
        User teacher = createUser(userService, "teacher@example.com", "teacher123", "Teacher", "User", UserRole.TEACHER, "987654321", "Teacher Lane 2");
        User student = createUser(userService, "student@example.com", "student123", "Student", "User", UserRole.STUDENT, "123123123", "Student Avenue 3");

        // Create course
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

        // Create subject
        Subject subject = Subject.builder()
                .id(UUID.randomUUID())
                .name("Object-Oriented Programming")
                .description("Understand the principles of OOP.")
                .build();
        subjectService.createSubject(subject);

        // Create lesson
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

        // Create enrollment
        Enrollment enrollment = Enrollment.builder()
                .id(UUID.randomUUID())
                .userId(student.getId())
                .courseId(course.getId())
                .enrollmentDate(LocalDate.now())
                .status(EnrollmentStatus.ACTIVE)
                .build();
        enrollmentService.createEnrollment(enrollment);

        // Create teaching
        Teaching teaching = Teaching.builder()
                .id(UUID.randomUUID())
                .userId(teacher.getId())
                .subjectId(subject.getId())
                .courseId(course.getId())
                .startDate(LocalDate.now())
                .endDate(course.getEndDate())
                .build();
        teachingService.createTeaching(teaching);

        // Create attendance
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

        // Create quiz
        Quiz quiz = Quiz.builder()
                .id(UUID.randomUUID())
                .title("Sample Quiz")
                .description("This is a sample quiz")
                .durationMinutes(30)
                .maxAttempts(3)
                .build();

        // Create questions
        List<Question> questions = new ArrayList<>();
        questions.add(createQuestion("What is the capital of France?", 1.0f, QuestionType.MULTIPLE_CHOICE));
        questions.add(createQuestion("What is 2 + 2?", 1.0f, QuestionType.OPEN_ENDED));

        // Create quiz with questions
        quizService.createQuiz(quiz, questions);

        // Create notification
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .senderId(admin.getId())
                .recipientId(student.getId())
                .title("Welcome!")
                .content("Welcome to the platform!")
                .notificationType(NotificationType.SYSTEM)
                .priority(NotificationPriority.HIGH)
                .creationDate(LocalDateTime.now())
                .readDate(null)
                .build();
        notificationService.createNotification(notification);
    }

    private static User createUser(UserService userService, String email, String password, String firstName, String lastName, UserRole role, String phone, String address) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password(BCrypt.hashpw(password, BCrypt.gensalt())) 
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .registrationDate(LocalDate.now())
                .phone(phone)
                .address(address)
                .lastAccess(LocalDateTime.now())
                .build();
        userService.createUser(user);
        return user;
    }

    private static Question createQuestion(String text, float score, QuestionType questionType) {
        return Question.builder()
                .id(UUID.randomUUID())
                .text(text)
                .score(score)
                .questionType(questionType)
                .build();
    }
}
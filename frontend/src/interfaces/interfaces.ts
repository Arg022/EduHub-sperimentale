import {
  UserRole,
  CourseLevel,
  EnrollmentStatus,
  AttendanceMode,
  QuestionType,
  NotificationPriority,
  NotificationType,
  MaterialType,
} from "./types";

export interface IUser {
  id: string; // UUID
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  registrationDate: string; // ISO date
  phone?: string;
  address?: string;
  lastAccess?: string; // ISO timestamp
}

export interface ICourse {
  id: string; // UUID
  name: string;
  description?: string;
  category?: string;
  startDate: string; // ISO date
  endDate: string; // ISO date
  syllabus?: string;
  maxStudents?: number;
  level: CourseLevel;
}

export interface IMaterial {
  id: string; // UUID
  courseId: string; // UUID
  creatorId: string; // UUID
  title: string;
  description?: string;
  type: MaterialType;
  data: string; // Base64 encoded string
  size: number;
  uploadDate: string; // ISO timestamp
  published: boolean;
}

export interface ISubject {
  id: string; // UUID
  name: string;
  description?: string;
}

export interface ILesson {
  id: string; // UUID
  courseId: string; // UUID
  subjectId: string; // UUID
  title: string;
  description?: string;
  date: string; // ISO date
  startTime: string; // ISO time
  endTime: string; // ISO time
}

export interface IEnrollment {
  id: string; // UUID
  userId: string; // UUID
  courseId: string; // UUID
  enrollmentDate: string; // ISO date
  status: EnrollmentStatus;
}

export interface ITeaching {
  id: string; // UUID
  userId: string; // UUID
  subjectId: string; // UUID
  courseId: string; // UUID
  startDate: string; // ISO date
  endDate: string; // ISO date
}

export interface IAttendance {
  id: string; // UUID
  lessonId: string; // UUID
  userId: string; // UUID
  present: boolean;
  mode: AttendanceMode;
  notes?: string;
  recordTime: string; // ISO timestamp
}

export interface IQuiz {
  id: string; // UUID
  courseId: string; // UUID
  creatorId: string; // UUID
  title: string;
  description?: string;
  durationMinutes: number;
  maxAttempts: number;
  creationDate: string; // ISO timestamp
  publicationDate?: string; // ISO timestamp
}

export interface IQuestion {
  id: string; // UUID
  quizId: string; // UUID
  text: string;
  score: number;
  questionType: QuestionType;
}

export interface IAnswer {
  id: string; // UUID
  questionId: string; // UUID
  text: string;
  isCorrect: boolean;
}

export interface IQuizResult {
  id: string; // UUID
  quizId: string; // UUID
  userId: string; // UUID
  totalScore: number;
  startTime: string; // ISO timestamp
  completionTime: string; // ISO timestamp
  timeSpent: number; // seconds
}

export interface IStudentAnswer {
  id: string; // UUID
  quizResultId: string; // UUID
  questionId: string; // UUID
  answerId?: string; // UUID
  scoreObtained: number;
}

export interface INotification {
  id: string; // UUID
  senderId: string; // UUID
  recipientId: string; // UUID
  title: string;
  content: string;
  notificationType: NotificationType;
  priority: NotificationPriority;
  creationDate: string; // ISO timestamp
  readDate?: string; // ISO timestamp
}
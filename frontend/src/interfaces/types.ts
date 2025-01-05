export enum UserRole {
  ADMIN = "ADMIN",
  TEACHER = "TEACHER",
  STUDENT = "STUDENT",
}

export enum EnrollmentStatus {
  PENDING = "PENDING",
  ACTIVE = "ACTIVE",
  COMPLETED = "COMPLETED",
  CANCELLED = "CANCELLED",
}

export enum AttendanceMode {
  IN_PERSON = "IN_PERSON",
  ONLINE = "ONLINE",
}

export enum NotificationType {
  SYSTEM = "SYSTEM",
  COURSE = "COURSE",
  QUIZ = "QUIZ",
  MATERIAL = "MATERIAL",
}

export enum NotificationPriority {
  LOW = "LOW",
  MEDIUM = "MEDIUM",
  HIGH = "HIGH",
}

export enum QuestionType {
  MULTIPLE_CHOICE = "MULTIPLE_CHOICE",
  TRUE_FALSE = "TRUE_FALSE",
  OPEN_ENDED = "OPEN_ENDED",
}

export enum CourseLevel {
  BEGINNER = "BEGINNER",
  INTERMEDIATE = "INTERMEDIATE",
  ADVANCED = "ADVANCED",
}

export enum MaterialType {
  PDF = "PDF",
  VIDEO = "VIDEO",
  LINK = "LINK",
  OTHER = "OTHER",
}
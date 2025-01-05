import {
  IAnswer,
  ILesson,
  INotification,
  IQuestion,
  IQuiz,
  IQuizResult,
  IStudentAnswer,
  ISubject,
} from "@/interfaces/interfaces";
import axios from "axios";

const BASE_URL = "http://localhost:8082/api/v1";

const MATERIALS_URL = "http://localhost:8083/api/v1";

const checkResponse = async (response: Response) => {
  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Failed to fetch: ${errorText}`);
  }
  return response.json();
};

export const fetchCourses = async (role?: string, userId?: string) => {
  let url = `${BASE_URL}/courses`;
  if (role === "STUDENT") {
    url = `${BASE_URL}/courses?studentId=${userId}`;
  } else if (role === "TEACHER") {
    url = `${BASE_URL}/courses?teacherId=${userId}`;
  }
  const response = await fetch(url);
  return checkResponse(response);
};

export const fetchCourseDetails = async (courseId: string) => {
  const response = await fetch(`${BASE_URL}/courses/${courseId}`);
  return checkResponse(response);
};

export const fetchCourseStudents = async (courseId: string) => {
  const response = await fetch(`${BASE_URL}/courses/${courseId}/students`);
  return checkResponse(response);
};

export const createCourse = async (courseData: any) => {
  const response = await fetch(`${BASE_URL}/courses`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(courseData),
  });
  return checkResponse(response);
};

export const createQuiz = async (quizData: any) => {
  const response = await fetch(`${BASE_URL}/quizzes`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(quizData),
  });
  return checkResponse(response);
};

export const createQuestion = async (questionData: any) => {
  const response = await fetch(`${BASE_URL}/questions`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(questionData),
  });
  return checkResponse(response);
};

export const fetchQuizzes = async (role?: string, userId?: string) => {
  let url = `${BASE_URL}/quizzes`;
  if (role === "STUDENT") {
    url = `${BASE_URL}/quizzes?studentId=${userId}`;
  } else if (role === "TEACHER") {
    url = `${BASE_URL}/quizzes?teacherId=${userId}`;
  }
  const response = await fetch(url);
  return checkResponse(response);
};

export const fetchNotifications = async () => {
  const response = await fetch(`${BASE_URL}/notifications`);
  return checkResponse(response);
};

export const createNotification = async (
  notificationData: Partial<INotification>
) => {
  const response = await fetch(`${BASE_URL}/notifications`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(notificationData),
  });
  return checkResponse(response);
};

export const fetchStudents = async () => {
  const response = await fetch(`${BASE_URL}/students`);
  return checkResponse(response);
};

export const fetchTeachers = async () => {
  const response = await fetch(`${BASE_URL}/teachers`);
  return checkResponse(response);
};

export const fetchUsers = async () => {
  const response = await fetch(`${BASE_URL}/users`);
  return checkResponse(response);
};

export const fetchTeachings = async () => {
  const response = await fetch(`${BASE_URL}/teachings`);
  return checkResponse(response);
};

export const fetchCourseTeachers = async (courseId: string) => {
  const response = await fetch(`${BASE_URL}/courses/${courseId}/teachers`);
  return checkResponse(response);
};

export const fetchSubjects = async (userId: string, role: string) => {
  let url = `${BASE_URL}/subjects`;
  if (role === "STUDENT") {
    url = `${BASE_URL}/subjects?userId=${userId}&role=STUDENT`;
  } else if (role === "TEACHER") {
    url = `${BASE_URL}/subjects?userId=${userId}&role=TEACHER`;
  } else if (role === "ADMIN") {
    url = `${BASE_URL}/subjects?userId=${userId}&role=ADMIN`;
  }
  const response = await fetch(url);
  return checkResponse(response);
};
export const createSubject = async (subjectData: Partial<ISubject>) => {
  const response = await axios.post(`${BASE_URL}/subjects`, subjectData);
  return response.data;
};

export const addEnrollment = async (enrollment: {
  userId: string;
  courseId: string;
}) => {
  const response = await fetch(`${BASE_URL}/enrollments`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(enrollment),
  });
  return checkResponse(response);
};

export const addTeaching = async (teaching: {
  userId: string;
  courseId: string;
  subjectId: string;
  startDate: string;
  endDate: string;
}) => {
  const response = await fetch(`${BASE_URL}/teachings`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(teaching),
  });
  return checkResponse(response);
};

export const removeEnrollment = async (userId: string, courseId: string) => {
  const response = await axios.delete(`${BASE_URL}/enrollments`, {
    data: { userId, courseId },
  });
  return response.data;
};

export const removeTeaching = async (userId: string, courseId: string) => {
  const response = await axios.delete(`${BASE_URL}/teachings`, {
    data: { userId, courseId },
  });
  return response.data;
};

export const fetchStudentAnswers = async () => {
  const response = await fetch(`${BASE_URL}/student-answers`);
  return checkResponse(response);
};

export const fetchQuizResults = async () => {
  const response = await fetch(`${BASE_URL}/quiz-results`);
  return checkResponse(response);
};

export const fetchQuestions = async () => {
  const response = await fetch(`${BASE_URL}/questions`);
  return checkResponse(response);
};

export const fetchQuiz = async (quizId: string) => {
  const response = await axios.get(`${BASE_URL}/quizzes/${quizId}`);
  return response.data;
};

export const submitQuizAttempt = async (
  quizResult: IQuizResult,
  studentAnswers: IStudentAnswer[]
): Promise<IQuizResult> => {
  const response = await axios.post(`${BASE_URL}/quiz-attempts`, {
    quizResult,
    studentAnswers,
  });
  return response.data;
};

export const fetchLessons = async (userId: string, role: string) => {
  let url = `${BASE_URL}/lessons`;
  if (role === "STUDENT") {
    url = `${BASE_URL}/lessons?userId=${userId}&role=STUDENT`;
  } else if (role === "TEACHER") {
    url = `${BASE_URL}/lessons?userId=${userId}&role=TEACHER`;
  } else if (role === "ADMIN") {
    url = `${BASE_URL}/lessons?userId=${userId}&role=ADMIN`;
  }
  const response = await fetch(url);
  return checkResponse(response);
};

export const createLesson = async (lesson: Partial<ILesson>) => {
  const response = await fetch(`${BASE_URL}/lessons`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(lesson),
  });
  return checkResponse(response);
};

export const fetchAttendanceByLesson = async (lessonId: string) => {
  const response = await fetch(`${BASE_URL}/attendance/lesson/${lessonId}`);
  return checkResponse(response);
};

export const fetchEnrollments = async () => {
  const response = await fetch(`${BASE_URL}/enrollments`);
  return checkResponse(response);
};

export const fetchAttendances = async () => {
  const response = await fetch(`${BASE_URL}/attendances`);
  return checkResponse(response);
};

export const fetchLessonDetails = async (lessonId: string) => {
  const response = await fetch(`${BASE_URL}/lessons/${lessonId}`);
  return checkResponse(response);
};

export const recordAttendance = async (
  lessonId: string,
  attendance: { [studentId: string]: boolean }
) => {
  const response = await fetch(`${BASE_URL}/attendance/${lessonId}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(attendance),
  });
  return checkResponse(response);
};

export const fetchAnswers = async () => {
  const response = await fetch(`${BASE_URL}/answers`);
  return checkResponse(response);
};

export const fetchMaterials = async (userId: string, role: string) => {
  let url = `${MATERIALS_URL}/materials`;
  if (role === "STUDENT") {
    url = `${MATERIALS_URL}/materials?userId=${userId}`;
  } else if (role === "TEACHER") {
    url = `${MATERIALS_URL}/materials?userId=${userId}`;
  } else if (role === "ADMIN") {
    url = `${MATERIALS_URL}/materials?userId=${userId}`;
  }
  const response = await fetch(url);
  return checkResponse(response);
};

export const uploadMaterial = async (formData: FormData) => {
  const response = await fetch(`${MATERIALS_URL}/materials/upload`, {
    method: "POST",
    body: formData,
  });
  return checkResponse(response);
};

export const downloadMaterial = async (materialId: string): Promise<Blob> => {
  const response = await fetch(
    `${MATERIALS_URL}/materials/download/${materialId}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    }
  );
  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Failed to fetch: ${errorText}`);
  }
  return response.blob();
};

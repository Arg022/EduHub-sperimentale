import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {
  fetchCourseDetails,
  fetchCourseStudents,
  fetchCourseTeachers,
  addEnrollment,
  addTeaching,
} from "@/services/apiService";
import {
  ICourse,
  IEnrollment,
  IUser,
  ITeaching,
} from "@/interfaces/interfaces";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import EntityList from "@/components/EntityList";
import AddEntityModal from "@/components/AddEntityModal";

type ISimpleUser = Omit<IUser, "password" | "role" | "registrationDate">;

const CourseDetails = () => {
  const { courseId } = useParams<{ courseId: string }>();
  const [course, setCourse] = useState<ICourse | null>(null);
  const [students, setStudents] = useState<(IUser & IEnrollment)[]>([]);
  const [teachers, setTeachers] = useState<(IUser & ITeaching)[]>([]);
  const [showStudents, setShowStudents] = useState(false);
  const [showTeachers, setShowTeachers] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [entityType, setEntityType] = useState<"student" | "teacher">(
    "student"
  );

  useEffect(() => {
    const loadCourseDetails = async () => {
      try {
        if (courseId) {
          const courseData = await fetchCourseDetails(courseId);
          setCourse(courseData);
        }
      } catch (error) {
        console.error("Failed to load course details", error);
      }
    };

    loadCourseDetails();
  }, [courseId]);

  const loadStudents = async () => {
    try {
      if (courseId) {
        const studentsData = await fetchCourseStudents(courseId);
        setStudents(studentsData);
        setShowStudents(true);
      }
    } catch (error) {
      console.error("Failed to load students", error);
    }
  };

  const loadTeachers = async () => {
    try {
      if (courseId) {
        const teachersData = await fetchCourseTeachers(courseId);
        setTeachers(teachersData);
        setShowTeachers(true);
      }
    } catch (error) {
      console.error("Failed to load teachers", error);
    }
  };

  const handleAddEntity = async (entity: ISimpleUser, subjectId?: string) => {
    try {
      if (entityType === "student") {
        await addEnrollment({ userId: entity.id, courseId: courseId! });
        loadStudents();
      } else {
        await addTeaching({
          userId: entity.id,
          courseId: courseId!,
          subjectId: subjectId!,
          startDate: new Date().toISOString().split("T")[0],
          endDate: new Date(
            new Date().setFullYear(new Date().getFullYear() + 1)
          )
            .toISOString()
            .split("T")[0],
        });
        loadTeachers();
      }
    } catch (error) {
      console.error("Failed to add entity", error);
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">{course?.name}</h1>

      <Card className="mb-6">
        <CardHeader>
          <CardTitle>Dettagli del Corso</CardTitle>
          <CardDescription>
            {course?.category || "Senza categoria"}
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <p>
                <strong>Inizio:</strong> {course?.startDate}
              </p>
              <p>
                <strong>Fine:</strong> {course?.endDate}
              </p>
              <p>
                <strong>Livello:</strong> <Badge>{course?.level}</Badge>
              </p>
              <p>
                <strong>Massimo studenti:</strong> {course?.maxStudents}
              </p>
            </div>
            <div>
              <p>
                <strong>Descrizione:</strong> {course?.description}
              </p>
              <p>
                <strong>Syllabus:</strong>
              </p>
              <pre>{course?.syllabus}</pre>
            </div>
          </div>
        </CardContent>
      </Card>

      <div className="flex space-x-4">
        <Button onClick={loadStudents}>Mostra Lista Studenti</Button>
        <Button onClick={loadTeachers}>Mostra Lista Docenti</Button>
      </div>

      {showStudents && (
        <EntityList
          title="Lista degli Studenti"
          entities={students}
          onAdd={() => {
            setEntityType("student");
            setIsModalOpen(true);
          }}
        />
      )}

      {showTeachers && (
        <EntityList
          title="Lista dei Docenti"
          entities={teachers}
          onAdd={() => {
            setEntityType("teacher");
            setIsModalOpen(true);
          }}
        />
      )}

      <AddEntityModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onAdd={handleAddEntity}
        entityType={entityType}
      />
    </div>
  );
};

export default CourseDetails;

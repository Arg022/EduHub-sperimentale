import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DataTable from "@/components/ui/DataTable";
import ActionButton from "@/components/ui/ActionButton";
import { useToast } from "@/hooks/use-toast";
import { fetchLessonDetails, fetchStudents, recordAttendance } from "@/services/apiService";
import { ILesson, IUser } from "@/interfaces/interfaces";

export default function AttendanceRecord() {
  const { lessonId } = useParams<{ lessonId: string }>();
  const [lesson, setLesson] = useState<ILesson | null>(null);
  const [students, setStudents] = useState<IUser[]>([]);
  const [attendance, setAttendance] = useState<{ [studentId: string]: boolean }>({});
  const { toast } = useToast();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const lessonData = await fetchLessonDetails(lessonId!);
        const studentsData = await fetchStudents();
        setLesson(lessonData);
        setStudents(studentsData);
      } catch (error) {
        console.error("Error fetching data", error);
      }
    };

    fetchData();
  }, [lessonId]);

  const handleAttendanceChange = (studentId: string, isPresent: boolean) => {
    setAttendance((prev) => ({ ...prev, [studentId]: isPresent }));
  };

  const handleSaveAttendance = async () => {
    try {
      await recordAttendance(lessonId!, attendance);
      toast({
        title: "Success",
        description: "Attendance has been recorded successfully.",
        status: "success",
      });
    } catch (error) {
      console.error("Error recording attendance", error);
      toast({
        title: "Error",
        description: "There was an error recording the attendance.",
        status: "error",
      });
    }
  };

  if (!lesson) {
    return <div>Loading...</div>;
  }

  const columns = [
    { header: "Student Name", accessor: "name" },
    { header: "Present", accessor: "present" },
  ];

  const data = students.map((student) => ({
    name: `${student.firstName} ${student.lastName}`,
    present: (
      <input
        type="checkbox"
        checked={attendance[student.id] || false}
        onChange={(e) => handleAttendanceChange(student.id, e.target.checked)}
      />
    ),
  }));

  return (
    <Card className="w-[800px] mx-auto my-8">
      <CardHeader>
        <CardTitle>Attendance for {lesson.title}</CardTitle>
      </CardHeader>
      <CardContent>
        <DataTable columns={columns} data={data} />
        <ActionButton label="Save Attendance" onClick={handleSaveAttendance} className="mt-4" />
      </CardContent>
    </Card>
  );
}
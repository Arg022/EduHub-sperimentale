import React, { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { fetchLessons, fetchAttendanceByLesson, fetchStudents } from "@/services/apiService";
import { ILesson, IAttendance, IUser } from "@/interfaces/interfaces";
import { useAuth } from "@/contexts/AuthContext";

export default function AttendanceReport() {
  const { user } = useAuth();
  const [lessons, setLessons] = useState<ILesson[]>([]);
  const [selectedLesson, setSelectedLesson] = useState<ILesson | null>(null);
  const [attendance, setAttendance] = useState<IAttendance[]>([]);
  const [students, setStudents] = useState<IUser[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const lessonsData = await fetchLessons(user!.id, user!.role);
        setLessons(lessonsData);
      } catch (error) {
        console.error("Error fetching lessons", error);
      }
    };

    fetchData();
  }, [user]);

  const handleViewAttendance = async (lesson: ILesson) => {
    try {
      const attendanceData = await fetchAttendanceByLesson(lesson.id);
      const studentsData = await fetchStudents();
      setSelectedLesson(lesson);
      setAttendance(attendanceData);
      setStudents(studentsData);
    } catch (error) {
      console.error("Error fetching attendance data", error);
    }
  };

  const getStudentName = (studentId: string) => {
    const student = students.find((s) => s.id === studentId);
    return student ? `${student.firstName} ${student.lastName}` : "Unknown";
  };

  return (
    <Card className="w-[800px] mx-auto my-8">
      <CardHeader>
        <CardTitle>Attendance Report</CardTitle>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Lesson Title</TableHead>
              <TableHead>Date</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {lessons.map((lesson) => (
              <TableRow key={lesson.id}>
                <TableCell>{lesson.title}</TableCell>
                <TableCell>{lesson.date}</TableCell>
                <TableCell>
                  <Dialog>
                    <DialogTrigger asChild>
                      <Button onClick={() => handleViewAttendance(lesson)}>
                        Visualizza Presenze
                      </Button>
                    </DialogTrigger>
                    <DialogContent>
                      <DialogHeader>
                        <DialogTitle>
                          Attendance for {selectedLesson?.title}
                        </DialogTitle>
                      </DialogHeader>
                      <Table>
                        <TableHeader>
                          <TableRow>
                            <TableHead>Student Name</TableHead>
                            <TableHead>Present</TableHead>
                          </TableRow>
                        </TableHeader>
                        <TableBody>
                          {attendance.map((record) => (
                            <TableRow key={record.id}>
                              <TableCell>{getStudentName(record.userId)}</TableCell>
                              <TableCell>
                                {record.present ? "Yes" : "No"}
                              </TableCell>
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </DialogContent>
                  </Dialog>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
}
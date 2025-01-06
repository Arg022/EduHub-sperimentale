import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
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
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useToast } from "@/hooks/use-toast";
import { useAuth } from "@/contexts/AuthContext";
import { ILesson, ICourse, ISubject } from "@/interfaces/interfaces";
import {
  fetchLessons,
  fetchCourses,
  fetchSubjects,
  createLesson,
} from "@/services/apiService";
import { format, parseISO } from "date-fns";

export default function LessonList() {
  const { user } = useAuth();
  const [lessons, setLessons] = useState<ILesson[]>([]);
  const [courses, setCourses] = useState<ICourse[]>([]);
  const [subjects, setSubjects] = useState<ISubject[]>([]);
  const [newLesson, setNewLesson] = useState<Partial<ILesson>>({
    courseId: "",
    subjectId: "",
    title: "",
    description: "",
    date: "",
    startTime: "",
    endTime: "",
  });
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [showTodayLessons, setShowTodayLessons] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();

  useEffect(() => {
    if (!user) return;

    const fetchData = async () => {
      try {
        const lessonsData = await fetchLessons(user.id, user.role);
        const coursesData = await fetchCourses(user.role, user.id);
        const subjectsData = await fetchSubjects(user.id, user.role);
        setLessons(lessonsData);
        setCourses(coursesData);
        setSubjects(subjectsData);
      } catch (error) {
        console.error("Error fetching data", error);
      }
    };

    fetchData();
  }, [user]);

  const handleCreateLesson = async () => {
    try {
      const createdLesson = await createLesson(newLesson);
      setLessons([...lessons, createdLesson]);
      setNewLesson({
        courseId: "",
        subjectId: "",
        title: "",
        description: "",
        date: "",
        startTime: "",
        endTime: "",
      });
      setIsDialogOpen(false);
      toast({
        title: "Lesson Created",
        description: "The new lesson has been added successfully.",
      });
    } catch (error) {
      console.error("Error creating lesson", error);
      toast({
        title: "Error",
        description: "There was an error creating the lesson.",
        variant: "destructive",
      });
    }
  };

  const handleAttendance = (lessonId: string) => {
    navigate(`/attendance/${lessonId}`);
  };

  const filterTodayLessons = () => {
    setShowTodayLessons(!showTodayLessons);
  };

  const today = new Date().toISOString().split("T")[0];
  const filteredLessons = showTodayLessons
    ? lessons.filter((lesson) => lesson.date === today)
    : lessons;

  if (!user) {
    return <div>Loading...</div>;
  }

  return (
    <Card className="w-[800px] mx-auto my-8">
      <CardHeader>
        <CardTitle>Lesson List</CardTitle>
        {(user.role === "ADMIN" || user.role === "TEACHER") && (
          <Button onClick={filterTodayLessons} className="mt-4">
            {showTodayLessons ? "Show All Lessons" : "Show Today's Lessons"}
          </Button>
        )}
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Title</TableHead>
              <TableHead>Date</TableHead>
              <TableHead>Start Time</TableHead>
              <TableHead>End Time</TableHead>
              {(user.role === "ADMIN" || user.role === "TEACHER") && (
                <TableHead>Actions</TableHead>
              )}
            </TableRow>
          </TableHeader>
          <TableBody>
            {Array.isArray(filteredLessons) && filteredLessons.length > 0 ? (
              filteredLessons.map((lesson) => (
                <TableRow key={lesson.id}>
                  <TableCell>{lesson.title}</TableCell>
                  <TableCell>
                    {format(new Date(lesson.date), "dd/MM/yyyy")}
                  </TableCell>
                  <TableCell>
                    {format(
                      parseISO(`1970-01-01T${lesson.startTime}`),
                      "HH:mm"
                    )}
                  </TableCell>
                  <TableCell>
                    {format(parseISO(`1970-01-01T${lesson.endTime}`), "HH:mm")}
                  </TableCell>
                  {(user.role === "ADMIN" || user.role === "TEACHER") && (
                    <TableCell>
                      <Button onClick={() => handleAttendance(lesson.id)}>
                        Attendance
                      </Button>
                    </TableCell>
                  )}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={5}>No lessons available</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>

        {user.role === "ADMIN" && (
          <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
            <DialogTrigger asChild>
              <Button className="mt-4">Create New Lesson</Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>Create New Lesson</DialogTitle>
              </DialogHeader>
              <div className="grid gap-4 py-4">
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="courseId" className="text-right">
                    Course
                  </Label>
                  <Select
                    onValueChange={(value) =>
                      setNewLesson({ ...newLesson, courseId: value })
                    }
                    value={newLesson.courseId}
                  >
                    <SelectTrigger className="col-span-3">
                      <SelectValue placeholder="Select a course" />
                    </SelectTrigger>
                    <SelectContent>
                      {courses.map((course) => (
                        <SelectItem key={course.id} value={course.id}>
                          {course.name}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="subjectId" className="text-right">
                    Subject
                  </Label>
                  <Select
                    onValueChange={(value) =>
                      setNewLesson({ ...newLesson, subjectId: value })
                    }
                    value={newLesson.subjectId}
                  >
                    <SelectTrigger className="col-span-3">
                      <SelectValue placeholder="Select a subject" />
                    </SelectTrigger>
                    <SelectContent>
                      {subjects.map((subject) => (
                        <SelectItem key={subject.id} value={subject.id}>
                          {subject.name}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="title" className="text-right">
                    Title
                  </Label>
                  <Input
                    id="title"
                    value={newLesson.title}
                    onChange={(e) =>
                      setNewLesson({ ...newLesson, title: e.target.value })
                    }
                    className="col-span-3"
                  />
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="description" className="text-right">
                    Description
                  </Label>
                  <Input
                    id="description"
                    value={newLesson.description}
                    onChange={(e) =>
                      setNewLesson({
                        ...newLesson,
                        description: e.target.value,
                      })
                    }
                    className="col-span-3"
                  />
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="date" className="text-right">
                    Date
                  </Label>
                  <Input
                    id="date"
                    type="date"
                    value={newLesson.date}
                    onChange={(e) =>
                      setNewLesson({ ...newLesson, date: e.target.value })
                    }
                    className="col-span-3"
                  />
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="startTime" className="text-right">
                    Start Time
                  </Label>
                  <Input
                    id="startTime"
                    type="time"
                    value={newLesson.startTime}
                    onChange={(e) =>
                      setNewLesson({ ...newLesson, startTime: e.target.value })
                    }
                    className="col-span-3"
                  />
                </div>
                <div className="grid grid-cols-4 items-center gap-4">
                  <Label htmlFor="endTime" className="text-right">
                    End Time
                  </Label>
                  <Input
                    id="endTime"
                    type="time"
                    value={newLesson.endTime}
                    onChange={(e) =>
                      setNewLesson({ ...newLesson, endTime: e.target.value })
                    }
                    className="col-span-3"
                  />
                </div>
              </div>
              <Button onClick={handleCreateLesson}>Create Lesson</Button>
            </DialogContent>
          </Dialog>
        )}
      </CardContent>
    </Card>
  );
}

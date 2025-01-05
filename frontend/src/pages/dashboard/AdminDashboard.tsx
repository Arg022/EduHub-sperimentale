import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Book, Bell, ChevronRight } from "lucide-react";
import { Link } from "react-router-dom";
import { fetchCourses, fetchQuizzes, fetchNotifications } from "@/services/apiService";
import { ICourse, IQuiz, INotification } from "@/interfaces/interfaces";

export function AdminDashboard() {
  const [courses, setCourses] = useState<ICourse[]>([]);
  const [quizzes, setQuizzes] = useState<IQuiz[]>([]);
  const [notifications, setNotifications] = useState<INotification[]>([]);

  useEffect(() => {
    const loadData = async () => {
      try {
        const [coursesData, quizzesData, notificationsData] = await Promise.all([
          fetchCourses(),
          fetchQuizzes(),
          fetchNotifications(),
        ]);
        setCourses(coursesData);
        setQuizzes(quizzesData);
        setNotifications(notificationsData);
      } catch (error) {
        console.error("Failed to load data", error);
      }
    };

    loadData();
  }, []);

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">Admin Dashboard</h1>
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">All Courses</CardTitle>
            <Book className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <ScrollArea className="h-72">
              <ul className="space-y-2">
                {courses.map((course) => (
                  <li key={course.id}>
                    <Link
                      to={`/courses/${course.id}`}
                      className="flex items-center justify-between hover:bg-accent p-2 rounded-md"
                    >
                      <span>{course.name}</span>
                      <ChevronRight className="h-4 w-4" />
                    </Link>
                  </li>
                ))}
              </ul>
            </ScrollArea>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">All Quizzes</CardTitle>
            <Book className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <ScrollArea className="h-72">
              <ul className="space-y-2">
                {quizzes.map((quiz) => (
                  <li key={quiz.id}>
                    <Link
                      to={`/quiz/${quiz.id}`}
                      className="flex items-center justify-between hover:bg-accent p-2 rounded-md"
                    >
                      <span>{quiz.title}</span>
                      <ChevronRight className="h-4 w-4" />
                    </Link>
                  </li>
                ))}
              </ul>
            </ScrollArea>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Recent Notifications
            </CardTitle>
            <Bell className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <ScrollArea className="h-72">
              <ul className="space-y-2">
                {notifications.map((notification) => (
                  <li key={notification.id} className="text-sm">
                    {notification.content}
                  </li>
                ))}
              </ul>
            </ScrollArea>
            <div className="mt-4">
              <Button asChild variant="outline">
                <Link to="/notifications">View All Notifications</Link>
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
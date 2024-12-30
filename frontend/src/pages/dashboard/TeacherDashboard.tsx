import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { ScrollArea } from "@/components/ui/scroll-area";
import {
  Book,
  Calendar,
  ChevronRight,
  GraduationCap,
  LineChart,
  Bell,
  Users,
} from "lucide-react";
import { Link } from "react-router-dom";

export function TeacherDashboard() {
  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">Teacher Dashboard</h1>
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">My Courses</CardTitle>
            <Book className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <ScrollArea className="h-72">
              <ul className="space-y-2">
                <li>
                  <Link
                    to="/courses/1/edit"
                    className="flex items-center justify-between hover:bg-accent p-2 rounded-md"
                  >
                    <span>Mathematics 101</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                <li>
                  <Link
                    to="/courses/2/edit"
                    className="flex items-center justify-between hover:bg-accent p-2 rounded-md"
                  >
                    <span>Advanced Algebra</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                {/* Add more courses as needed */}
              </ul>
            </ScrollArea>
            <div className="mt-4">
              <Button asChild>
                <Link to="/courses">Manage All Courses</Link>
              </Button>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Upcoming Quizzes
            </CardTitle>
            <GraduationCap className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <ScrollArea className="h-72">
              <ul className="space-y-2">
                <li>
                  <Link
                    to="/quiz/1/edit"
                    className="flex items-center justify-between hover:bg-accent p-2 rounded-md"
                  >
                    <span>Math Quiz - Chapter 3</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                <li>
                  <Link
                    to="/quiz/2/edit"
                    className="flex items-center justify-between hover:bg-accent p-2 rounded-md"
                  >
                    <span>Algebra Quiz - Polynomials</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                {/* Add more quizzes as needed */}
              </ul>
            </ScrollArea>
            <div className="mt-4">
              <Button asChild>
                <Link to="/quiz">Manage All Quizzes</Link>
              </Button>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Attendance Overview
            </CardTitle>
            <Calendar className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="space-y-2">
              <div>
                <div className="text-sm font-medium">Mathematics 101</div>
                <div className="text-2xl font-bold">92%</div>
              </div>
              <div>
                <div className="text-sm font-medium">Advanced Algebra</div>
                <div className="text-2xl font-bold">88%</div>
              </div>
            </div>
            <div className="mt-4">
              <Button asChild>
                <Link to="/courses/1/attendance/report">View Full Report</Link>
              </Button>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Course Statistics
            </CardTitle>
            <LineChart className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <ScrollArea className="h-72">
              <ul className="space-y-2">
                <li>
                  <Link
                    to="/statistics/course/1"
                    className="flex items-center justify-between hover:bg-accent p-2 rounded-md"
                  >
                    <span>Mathematics 101</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                <li>
                  <Link
                    to="/statistics/course/2"
                    className="flex items-center justify-between hover:bg-accent p-2 rounded-md"
                  >
                    <span>Advanced Algebra</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                {/* Add more courses as needed */}
              </ul>
            </ScrollArea>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Student Performance
            </CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <ScrollArea className="h-72">
              <ul className="space-y-2">
                <li>
                  <Link
                    to="/statistics/student/1"
                    className="flex items-center justify-between hover:bg-accent p-2 rounded-md"
                  >
                    <span>John Doe</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                <li>
                  <Link
                    to="/statistics/student/2"
                    className="flex items-center justify-between hover:bg-accent p-2 rounded-md"
                  >
                    <span>Jane Smith</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                {/* Add more students as needed */}
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
                <li className="text-sm">
                  New student enrolled in Mathematics 101
                </li>
                <li className="text-sm">
                  Reminder: Grade submission due for Advanced Algebra
                </li>
                <li className="text-sm">
                  System update: New quiz feature available
                </li>
                {/* Add more notifications as needed */}
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

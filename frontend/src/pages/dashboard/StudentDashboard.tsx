import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { ScrollArea } from "@/components/ui/scroll-area"
import { Book, Calendar, ChevronRight, GraduationCap, LineChart, Bell } from 'lucide-react'
import { Link } from "react-router-dom"

export function StudentDashboard() {
  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">Student Dashboard</h1>
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
                  <Link to="/courses/1" className="flex items-center justify-between hover:bg-accent p-2 rounded-md">
                    <span>Mathematics 101</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                <li>
                  <Link to="/courses/2" className="flex items-center justify-between hover:bg-accent p-2 rounded-md">
                    <span>Introduction to Physics</span>
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
            <CardTitle className="text-sm font-medium">Upcoming Quizzes</CardTitle>
            <GraduationCap className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <ScrollArea className="h-72">
              <ul className="space-y-2">
                <li>
                  <Link to="/quiz/1" className="flex items-center justify-between hover:bg-accent p-2 rounded-md">
                    <span>Math Quiz - Chapter 3</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                <li>
                  <Link to="/quiz/2" className="flex items-center justify-between hover:bg-accent p-2 rounded-md">
                    <span>Physics Quiz - Mechanics</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                {/* Add more quizzes as needed */}
              </ul>
            </ScrollArea>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Attendance Overview</CardTitle>
            <Calendar className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">85%</div>
            <p className="text-xs text-muted-foreground">Overall attendance rate</p>
            <div className="mt-4">
              <Button asChild>
                <Link to="/courses/1/attendance/report">View Details</Link>
              </Button>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">My Statistics</CardTitle>
            <LineChart className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="space-y-2">
              <div>
                <div className="text-sm font-medium">Average Grade</div>
                <div className="text-2xl font-bold">B+</div>
              </div>
              <div>
                <div className="text-sm font-medium">Completed Courses</div>
                <div className="text-2xl font-bold">4</div>
              </div>
            </div>
            <div className="mt-4">
              <Button asChild>
                <Link to="/statistics/student/1">View Full Statistics</Link>
              </Button>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Recent Notifications</CardTitle>
            <Bell className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <ScrollArea className="h-72">
              <ul className="space-y-2">
                <li className="text-sm">New course material available for Mathematics 101</li>
                <li className="text-sm">Reminder: Physics Quiz due in 2 days</li>
                <li className="text-sm">Your attendance report for last month is ready</li>
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
  )
}


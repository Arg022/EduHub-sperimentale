import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { ScrollArea } from "@/components/ui/scroll-area"
import { Book, Calendar, ChevronRight, GraduationCap, LineChart, Bell, Users, Settings } from 'lucide-react'
import { Link } from "react-router-dom"

export function AdminDashboard() {
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
                <li>
                  <Link to="/courses/1" className="flex items-center justify-between hover:bg-accent p-2 rounded-md">
                    <span>Mathematics 101</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                <li>
                  <Link to="/courses/2" className="flex items-center justify-between hover:bg-accent p-2 rounded-md">
                    <span>Advanced Physics</span>
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
            <CardTitle className="text-sm font-medium">User Management</CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <ScrollArea className="h-72">
              <ul className="space-y-2">
                <li>
                  <Link to="/users/students" className="flex items-center justify-between hover:bg-accent p-2 rounded-md">
                    <span>Students</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                <li>
                  <Link to="/users/teachers" className="flex items-center justify-between hover:bg-accent p-2 rounded-md">
                    <span>Teachers</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
                <li>
                  <Link to="/users/admins" className="flex items-center justify-between hover:bg-accent p-2 rounded-md">
                    <span>Admins</span>
                    <ChevronRight className="h-4 w-4" />
                  </Link>
                </li>
              </ul>
            </ScrollArea>
            <div className="mt-4">
              <Button asChild>
                <Link to="/users">Manage All Users</Link>
              </Button>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">System Statistics</CardTitle>
            <LineChart className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="space-y-2">
              <div>
                <div className="text-sm font-medium">Total Users</div>
                <div className="text-2xl font-bold">1,234</div>
              </div>
              <div>
                <div className="text-sm font-medium">Active Courses</div>
                <div className="text-2xl font-bold">42</div>
              </div>
              <div>
                <div className="text-sm font-medium">Quizzes Completed</div>
                <div className="text-2xl font-bold">7,890</div>
              </div>
            </div>
            <div className="mt-4">
              <Button asChild>
                <Link to="/statistics">View Detailed Statistics</Link>
              </Button>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">System Configuration</CardTitle>
            <Settings className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <ul className="space-y-2">
              <li>
                <Link to="/config/general" className="flex items-center justify-between hover:bg-accent p-2 rounded-md">
                  <span>General Settings</span>
                  <ChevronRight className="h-4 w-4" />
                </Link>
              </li>
              <li>
                <Link to="/config/email" className="flex items-center justify-between hover:bg-accent p-2 rounded-md">
                  <span>Email Configuration</span>
                  <ChevronRight className="h-4 w-4" />
                </Link>
              </li>
              <li>
                <Link to="/config/security" className="flex items-center justify-between hover:bg-accent p-2 rounded-md">
                  <span>Security Settings</span>
                  <ChevronRight className="h-4 w-4" />
                </Link>
              </li>
            </ul>
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
                <li className="text-sm">New course created: Advanced Machine Learning</li>
                <li className="text-sm">User reported issue with quiz submission</li>
                <li className="text-sm">System update completed successfully</li>
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
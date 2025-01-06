import { useState, useEffect } from "react";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { fetchCourses } from "@/services/apiService";
import { ICourse } from "@/interfaces/interfaces";
import { Link } from "react-router-dom";
import { useAuth } from "@/contexts/AuthContext";
import { format } from "date-fns";

export default function CoursesList() {
  const { user } = useAuth();
  const [courses, setCourses] = useState<ICourse[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [categoryFilter, setCategoryFilter] = useState("");

  useEffect(() => {
    const loadCourses = async () => {
      if (!user) {
        console.error("User is not authenticated");
        return;
      }

      try {
        const coursesData = await fetchCourses(user.role, user.id);
        setCourses(coursesData);
      } catch (error) {
        console.error("Failed to load courses", error);
      }
    };

    loadCourses();
  }, [user]);

  if (!user) {
    return <p>Loading...</p>;
  }

  const filteredCourses = courses.filter(
    (course) =>
      course.name.toLowerCase().includes(searchTerm.toLowerCase()) &&
      (categoryFilter === "" || course.category === categoryFilter)
  );

  const categories = Array.from(
    new Set(courses.map((course) => course.category || "Senza categoria"))
  );

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">Corsi Disponibili</h1>

      <div className="flex gap-4 mb-6">
        <Input
          placeholder="Cerca corsi..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="max-w-sm"
        />
        <Select value={categoryFilter} onValueChange={setCategoryFilter}>
          <SelectTrigger className="w-[180px]">
            <SelectValue placeholder="Categoria" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">Tutte le categorie</SelectItem>
            {categories.map((category) => (
              <SelectItem key={category} value={category}>
                {category}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredCourses.map((course) => (
          <Card key={course.id}>
            <CardHeader>
              <CardTitle>{course.name}</CardTitle>
              <CardDescription>
                {course.category || "Senza categoria"}
              </CardDescription>
            </CardHeader>
            <CardContent>
              <p>
                <strong>Descrizione:</strong> {course.description}
              </p>
              <p>
                <strong>Data di inizio:</strong>{" "}
                {format(new Date(course.startDate), "dd/MM/yyyy")}
              </p>
              <p>
                <strong>Data di fine:</strong>{" "}
                {format(new Date(course.endDate), "dd/MM/yyyy")}
              </p>
              <Badge
                variant={course.level === "BEGINNER" ? "default" : "secondary"}
                className="mt-2"
              >
                {course.level}
              </Badge>
            </CardContent>
            <CardFooter>
              <Button asChild>
                <Link to={`/courses/${course.id}`}>Dettagli Corso</Link>
              </Button>
            </CardFooter>
          </Card>
        ))}
      </div>

      {filteredCourses.length === 0 && (
        <p className="text-center text-gray-500 mt-6">Nessun corso trovato.</p>
      )}
    </div>
  );
}

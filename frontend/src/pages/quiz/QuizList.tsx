import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "@/contexts/AuthContext";
import { fetchQuizzes, fetchCourses } from "@/services/apiService";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Badge } from "@/components/ui/badge";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { useToast } from "@/hooks/use-toast";
import { IQuiz } from "@/interfaces/interfaces";

export default function QuizList() {
  const [quizzes, setQuizzes] = useState<IQuiz[]>([]);
  const [filteredQuizzes, setFilteredQuizzes] = useState<IQuiz[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedCourse, setSelectedCourse] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [courses, setCourses] = useState([]); 

  const { user } = useAuth()!;
  const navigate = useNavigate();
  const { toast } = useToast();

  useEffect(() => {
    const loadQuizzes = async () => {
      if (!user) {
        setError("Utente non autenticato.");
        return;
      }

      setLoading(true);
      setError("");

      try {
        const quizzes = await fetchQuizzes(user.role, user.id);
        setQuizzes(quizzes);
        setFilteredQuizzes(quizzes);
        const courseData = await fetchCourses(user.role, user.id); setCourses(courseData);
      } catch (err) {
        setError("Errore durante il caricamento dei quiz.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    loadQuizzes();
  }, [user]);

  const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    const term = event.target.value.toLowerCase();
    setSearchTerm(term);
    filterQuizzes(term, selectedCourse);
  };

  const handleCourseFilter = (courseId: string) => {
    setSelectedCourse(courseId);
    filterQuizzes(searchTerm, courseId === "all" ? "" : courseId);
  };

  const filterQuizzes = (term: string, courseId: string) => {
    let filtered = quizzes.filter(
      (quiz) =>
        quiz.title.toLowerCase().includes(term) ||
        (quiz.description && quiz.description.toLowerCase().includes(term))
    );

    if (courseId && courseId !== "all") {
      filtered = filtered.filter((quiz) => quiz.courseId === courseId);
    }

    setFilteredQuizzes(filtered);
  };

  const handleStartQuiz = (quizId: string) => {
    toast({
      title: "Quiz Iniziato",
      description: `Hai iniziato il quiz con ID: ${quizId}`,
    });
    navigate(`/quiz/${quizId}/attempt`);
  };

  const handleEditQuiz = (quizId: string) => {
    navigate(`/quiz/${quizId}/edit`);
  };

  if (loading) return <div>Caricamento...</div>;
  if (error) return <div>{error}</div>;

  return (
    <Card className="w-full max-w-4xl mx-auto my-8">
      <CardHeader>
        <CardTitle>Elenco Quiz</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="flex justify-between mb-4">
          <Input
            placeholder="Cerca quiz..."
            value={searchTerm}
            onChange={handleSearch}
            className="max-w-sm"
          />
          <Select onValueChange={handleCourseFilter}>
            <SelectTrigger className="w-[180px]">
              <SelectValue placeholder="Filtra per corso" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">Tutti i corsi</SelectItem>
              {courses.map((course) => (
                <SelectItem key={course.id} value={course.id}>
                  {course.name}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Titolo</TableHead>
              <TableHead>Durata</TableHead>
              <TableHead>Tentativi</TableHead>
              <TableHead>Stato</TableHead>
              <TableHead>Azioni</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filteredQuizzes.map((quiz) => (
              <TableRow key={quiz.id}>
                <TableCell className="font-medium">{quiz.title}</TableCell>
                <TableCell>{quiz.durationMinutes} minuti</TableCell>
                <TableCell>{quiz.maxAttempts}</TableCell>
                <TableCell>
                  <Badge
                    variant={quiz.publicationDate ? "default" : "secondary"}
                  >
                    {quiz.publicationDate ? "Pubblicato" : "Bozza"}
                  </Badge>
                </TableCell>
                <TableCell>
                  <Dialog>
                    <DialogTrigger asChild>
                      <Button variant="outline" size="sm">
                        Dettagli
                      </Button>
                    </DialogTrigger>
                    <DialogContent>
                      <DialogHeader>
                        <DialogTitle>{quiz.title}</DialogTitle>
                        <DialogDescription>
                          {quiz.description}
                        </DialogDescription>
                      </DialogHeader>
                      <div className="grid gap-4 py-4">
                        <div>Durata: {quiz.durationMinutes} minuti</div>
                        <div>Tentativi massimi: {quiz.maxAttempts}</div>
                        <div>
                          Data di creazione:{" "}
                          {new Date(quiz.creationDate).toLocaleString()}
                        </div>
                        {quiz.publicationDate && (
                          <div>
                            Data di pubblicazione:{" "}
                            {new Date(quiz.publicationDate).toLocaleString()}
                          </div>
                        )}
                      </div>
                    </DialogContent>
                  </Dialog>
                  {user.role === "STUDENT" && quiz.publicationDate && (
                    <Button
                      variant="default"
                      size="sm"
                      className="ml-2"
                      onClick={() => handleStartQuiz(quiz.id)}
                    >
                      Inizia Quiz
                    </Button>
                  )}
                  {(user.role === "TEACHER" || user.role === "ADMIN") && (
                    <Button
                      variant="secondary"
                      size="sm"
                      className="ml-2"
                      onClick={() => handleEditQuiz(quiz.id)}
                    >
                      Modifica
                    </Button>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
}

import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "@/contexts/AuthContext";
import { fetchQuizzes, fetchCourses } from "@/services/apiService";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DataTable from "@/components/ui/DataTable";
import ActionButton from "@/components/ui/ActionButton";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
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
        const courseData = await fetchCourses(user.role, user.id);
        setCourses(courseData);
      } catch (error) {
        setError("Errore nel caricamento dei quiz.");
      } finally {
        setLoading(false);
      }
    };

    loadQuizzes();
  }, [user]);

  const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
    const filtered = quizzes.filter((quiz) =>
      quiz.title.toLowerCase().includes(event.target.value.toLowerCase())
    );
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

  const columns = [
    { header: "Titolo", accessor: "title" },
    { header: "Durata", accessor: "durationMinutes" },
    { header: "Tentativi", accessor: "maxAttempts" },
    { header: "Stato", accessor: "status" },
    { header: "Azioni", accessor: "actions" },
  ];

  const data = filteredQuizzes.map((quiz) => ({
    title: quiz.title,
    durationMinutes: `${quiz.durationMinutes} minuti`,
    maxAttempts: quiz.maxAttempts,
    status: quiz.publicationDate ? "Pubblicato" : "Bozza",
    actions: (
      <>
        {user.role === "STUDENT" && quiz.publicationDate && (
          <ActionButton
            label="Inizia Quiz"
            onClick={() => handleStartQuiz(quiz.id)}
          />
        )}
        {(user.role === "TEACHER" || user.role === "ADMIN") && (
          <ActionButton
            label="Modifica"
            onClick={() => handleEditQuiz(quiz.id)}
            variant="outline"
          />
        )}
      </>
    ),
  }));

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
          <Select value={selectedCourse} onValueChange={setSelectedCourse}>
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
        <DataTable columns={columns} data={data} />
      </CardContent>
    </Card>
  );
}

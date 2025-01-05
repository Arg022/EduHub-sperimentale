import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useAuth } from "@/contexts/AuthContext";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardFooter,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Label } from "@/components/ui/label";
import { Checkbox } from "@/components/ui/checkbox";
import { Input } from "@/components/ui/input";
import { useToast } from "@/hooks/use-toast";
import {
  IQuiz,
  IQuestion,
  IAnswer,
  IQuizResult,
  IStudentAnswer,
} from "@/interfaces/interfaces";
import { fetchQuiz, submitQuizAttempt } from "@/services/apiService";

type QuestionWithAnswers = IQuestion & { answers: IAnswer[] };

export default function QuizAttempt() {
  const { quizId } = useParams<{ quizId: string }>();
  const navigate = useNavigate();
  const { user } = useAuth();
  const { toast } = useToast();

  const [quiz, setQuiz] = useState<IQuiz | null>(null);
  const [questions, setQuestions] = useState<QuestionWithAnswers[]>([]);
  const [answers, setAnswers] = useState<IAnswer[]>([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [studentAnswers, setStudentAnswers] = useState<{
    [questionId: string]: string | string[];
  }>({});
  const [timeLeft, setTimeLeft] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadQuiz = async () => {
      try {
        const data = await fetchQuiz(quizId!);
        console.log("Fetched quiz data:", data);
        setQuiz(data);
        setQuestions(
          data.questions.map((q: Partial<QuestionWithAnswers>) => ({
            ...q,
            answers: q.answers || [],
          }))
        );
        setAnswers(
          data.questions.flatMap(
            (q: Partial<QuestionWithAnswers>) => q.answers || []
          )
        );
        setTimeLeft(data.durationMinutes * 60);
        setLoading(false);
      } catch (err) {
        console.error("Failed to load quiz:", err);
        setError("Failed to load quiz. Please try again.");
        setLoading(false);
      }
    };

    loadQuiz();
  }, [quizId]);

  useEffect(() => {
    if (timeLeft === null || timeLeft <= 0) return;

    const timer = setInterval(() => {
      setTimeLeft((prevTime) => (prevTime !== null ? prevTime - 1 : null));
    }, 1000);

    return () => clearInterval(timer);
  }, [timeLeft]);

  const handleAnswerChange = (
    questionId: string,
    answer: string | string[]
  ) => {
    setStudentAnswers((prev) => ({ ...prev, [questionId]: answer }));
  };

  const handleSubmit = async () => {
    if (!quiz || !user) return;

    const endTime = new Date().toISOString();
    const startTime = new Date(
      Date.now() - (quiz.durationMinutes * 60 * 1000 - (timeLeft || 0) * 1000)
    ).toISOString();

    const quizResult: IQuizResult = {
      id: "",
      quizId: quiz.id,
      userId: user.id,
      totalScore: 0,
      startTime,
      completionTime: endTime,
      timeSpent: quiz.durationMinutes * 60 - (timeLeft || 0),
    };

    const studentAnswersArray: IStudentAnswer[] = Object.entries(
      studentAnswers
    ).map(([questionId, answer]) => ({
      id: "",
      quizResultId: "",
      questionId,
      answerId: Array.isArray(answer) ? answer[0] : answer,
      scoreObtained: 0,
    }));

    try {
      console.log("Submitting quiz attempt:", {
        quizResult,
        studentAnswersArray,
      });
      const result = await submitQuizAttempt(quizResult, studentAnswersArray);
      toast({
        title: "Quiz Submitted",
        description: `Your answers have been submitted successfully. Your score is ${result.totalScore}.`,
      });
      navigate("/dashboard/student");
    } catch (err) {
      console.error("Failed to submit quiz attempt:", err);
      toast({
        title: "Submission Failed",
        description: "Failed to submit your answers. Please try again.",
        variant: "destructive",
      });
    }
  };

  if (loading) return <div>Loading quiz...</div>;
  if (error) return <div>{error}</div>;
  if (!quiz || questions.length === 0)
    return <div>No quiz data available.</div>;

  const currentQuestion = questions[currentQuestionIndex];
  const questionAnswers = answers.filter(
    (a) => a.questionId === currentQuestion.id
  );

  return (
    <Card className="w-full max-w-4xl mx-auto my-8">
      <CardHeader>
        <CardTitle>{quiz.title}</CardTitle>
        <div>
          Time Left: {Math.floor(timeLeft! / 60)}:
          {(timeLeft! % 60).toString().padStart(2, "0")}
        </div>
      </CardHeader>
      <CardContent>
        <h2 className="text-xl font-bold mb-4">
          Question {currentQuestionIndex + 1} of {questions.length}
        </h2>
        <p className="mb-4">{currentQuestion.text}</p>
        {currentQuestion.questionType === "MULTIPLE_CHOICE" && (
          <RadioGroup
            onValueChange={(value) =>
              handleAnswerChange(currentQuestion.id!, value)
            }
            value={studentAnswers[currentQuestion.id!] as string}
          >
            {questionAnswers.map((answer) => (
              <div key={answer.id} className="flex items-center space-x-2">
                <RadioGroupItem value={answer.id} id={answer.id} />
                <Label htmlFor={answer.id}>{answer.text}</Label>
              </div>
            ))}
          </RadioGroup>
        )}
        {currentQuestion.questionType === "MULTIPLE_ANSWER" && (
          <div className="space-y-2">
            {questionAnswers.map((answer) => (
              <div key={answer.id} className="flex items-center space-x-2">
                <Checkbox
                  id={answer.id}
                  checked={(
                    (studentAnswers[currentQuestion.id!] as string[]) || []
                  ).includes(answer.id)}
                  onCheckedChange={(checked) => {
                    const currentAnswers =
                      (studentAnswers[currentQuestion.id!] as string[]) || [];
                    if (checked) {
                      handleAnswerChange(currentQuestion.id!, [
                        ...currentAnswers,
                        answer.id,
                      ]);
                    } else {
                      handleAnswerChange(
                        currentQuestion.id!,
                        currentAnswers.filter((id) => id !== answer.id)
                      );
                    }
                  }}
                />
                <Label htmlFor={answer.id}>{answer.text}</Label>
              </div>
            ))}
          </div>
        )}
        {currentQuestion.questionType === "SHORT_ANSWER" && (
          <Input
            value={(studentAnswers[currentQuestion.id!] as string) || ""}
            onChange={(e) =>
              handleAnswerChange(currentQuestion.id!, e.target.value)
            }
            placeholder="Type your answer here"
          />
        )}
      </CardContent>
      <CardFooter className="flex justify-between">
        <Button
          onClick={() =>
            setCurrentQuestionIndex((prev) => Math.max(0, prev - 1))
          }
          disabled={currentQuestionIndex === 0}
        >
          Previous
        </Button>
        {currentQuestionIndex < questions.length - 1 ? (
          <Button
            onClick={() =>
              setCurrentQuestionIndex((prev) =>
                Math.min(questions.length - 1, prev + 1)
              )
            }
          >
            Next
          </Button>
        ) : (
          <Button onClick={handleSubmit}>Submit Quiz</Button>
        )}
      </CardFooter>
    </Card>
  );
}

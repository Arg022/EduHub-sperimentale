import { useEffect, useState } from "react";
import { useForm, useFieldArray } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardFooter,
  CardDescription,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { useToast } from "@/hooks/use-toast";
import { Toaster } from "@/components/ui/toaster";
import { Checkbox } from "@/components/ui/checkbox";
import { Label } from "@/components/ui/label";
import { Trash2 } from "lucide-react";
import {
  createQuiz,
  fetchCourses,
  createQuestion,
} from "@/services/apiService";
import { useNavigate } from "react-router-dom";
import { ICourse } from "@/interfaces/interfaces"; // Importa l'interfaccia ICourse
import { QuestionType } from "@/interfaces/types"; // Importa l'enum QuestionType
import { useAuth } from "@/contexts/AuthContext"; // Importa il contesto di autenticazione

const questionTypeEnum = z.nativeEnum(QuestionType);

const quizSchema = z.object({
  title: z.string().min(1, "Title is required"),
  description: z.string().optional(),
  durationMinutes: z
    .number()
    .int()
    .positive("Duration must be a positive number"),
  maxAttempts: z
    .number()
    .int()
    .positive("Max attempts must be a positive number"),
  courseId: z.string().uuid({ message: "Course is required" }),
  questions: z
    .array(
      z.object({
        text: z.string().min(1, "Question text is required"),
        score: z.number().positive("Score must be a positive number"),
        questionType: questionTypeEnum,
        answers: z
          .array(
            z.object({
              text: z.string().min(1, "Answer text is required"),
              isCorrect: z.boolean(),
            })
          )
          .min(1, "At least one answer is required"),
      })
    )
    .min(1, "At least one question is required"),
});

type QuizFormValues = z.infer<typeof quizSchema>;

export function QuizCreate() {
  const navigate = useNavigate();
  const { toast } = useToast();
  const { user } = useAuth(); // Ottieni l'utente autenticato
  const [courses, setCourses] = useState<ICourse[]>([]); // Definisci il tipo di courses come ICourse[]
  const form = useForm<QuizFormValues>({
    resolver: zodResolver(quizSchema),
    defaultValues: {
      title: "",
      description: "",
      durationMinutes: 30,
      maxAttempts: 1,
      courseId: "",
      questions: [
        {
          text: "",
          score: 1,
          questionType: QuestionType.MULTIPLE_CHOICE,
          answers: [{ text: "", isCorrect: false }],
        },
      ],
    },
  });

  const {
    fields: questionFields,
    append: appendQuestion,
    remove: removeQuestion,
  } = useFieldArray({
    control: form.control,
    name: "questions",
  });

  useEffect(() => {
    const loadCourses = async () => {
      try {
        const coursesData = await fetchCourses();
        setCourses(coursesData);
      } catch (error) {
        toast({
          title: "Error loading courses",
          description: (error as any).message,
        });
      }
    };

    loadCourses();
  }, [toast]);

  const onSubmit = async (data: QuizFormValues) => {
    try {
      // Aggiungi creator_id al payload
      const payload = { ...data, creator_id: user?.id };

      // Create the quiz
      const quizResponse = await createQuiz(payload);
      const quizId = quizResponse.id;

      // Create each question
      for (const question of data.questions) {
        await createQuestion({ ...question, quizId });
      }

      toast({
        title: "Quiz created successfully",
        description: (
          <pre className="mt-2 w-[340px] rounded-md bg-slate-950 p-4">
            <code className="text-white">
              {JSON.stringify(quizResponse, null, 2)}
            </code>
          </pre>
        ),
      });
      navigate("/quizzes");
    } catch (error) {
      toast({
        title: "Error creating quiz",
        description: (error as any).message,
      });
    }
  };

  return (
    <Card className="w-[800px] mx-auto my-8">
      <CardHeader>
        <CardTitle>Create New Quiz</CardTitle>
        <CardDescription>
          Set up your quiz questions and answers.
        </CardDescription>
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
            <FormField
              control={form.control}
              name="title"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Quiz Title</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter quiz title" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="description"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Description</FormLabel>
                  <FormControl>
                    <Textarea placeholder="Enter quiz description" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <div className="flex space-x-4">
              <FormField
                control={form.control}
                name="durationMinutes"
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>Duration (minutes)</FormLabel>
                    <FormControl>
                      <Input
                        type="number"
                        {...field}
                        onChange={(e) =>
                          field.onChange(parseInt(e.target.value))
                        }
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="maxAttempts"
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>Max Attempts</FormLabel>
                    <FormControl>
                      <Input
                        type="number"
                        {...field}
                        onChange={(e) =>
                          field.onChange(parseInt(e.target.value))
                        }
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
            <FormField
              control={form.control}
              name="courseId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Course</FormLabel>
                  <Select
                    onValueChange={field.onChange}
                    defaultValue={field.value}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select course" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {courses.map((course) => (
                        <SelectItem key={course.id} value={course.id}>
                          {course.name}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />
            {questionFields.map((field, questionIndex) => (
              <Card key={field.id}>
                <CardHeader>
                  <CardTitle>Question {questionIndex + 1}</CardTitle>
                </CardHeader>
                <CardContent>
                  <FormField
                    control={form.control}
                    name={`questions.${questionIndex}.text`}
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Question Text</FormLabel>
                        <FormControl>
                          <Input {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name={`questions.${questionIndex}.score`}
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Score</FormLabel>
                        <FormControl>
                          <Input
                            type="number"
                            {...field}
                            onChange={(e) =>
                              field.onChange(parseFloat(e.target.value))
                            }
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name={`questions.${questionIndex}.questionType`}
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Question Type</FormLabel>
                        <Select
                          onValueChange={field.onChange}
                          defaultValue={field.value}
                        >
                          <FormControl>
                            <SelectTrigger>
                              <SelectValue placeholder="Select question type" />
                            </SelectTrigger>
                          </FormControl>
                          <SelectContent>
                            <SelectItem value={QuestionType.MULTIPLE_CHOICE}>
                              Multiple Choice
                            </SelectItem>
                            <SelectItem value={QuestionType.TRUE_FALSE}>
                              True/False
                            </SelectItem>
                            <SelectItem value={QuestionType.OPEN_ENDED}>
                              Open Ended
                            </SelectItem>
                          </SelectContent>
                        </Select>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <div className="mt-4">
                    <FormLabel>Answers</FormLabel>
                    {form.watch(`questions.${questionIndex}.questionType`) !==
                      QuestionType.OPEN_ENDED && (
                      <div className="space-y-2">
                        {form
                          .watch(`questions.${questionIndex}.answers`)
                          ?.map((answer, answerIndex) => (
                            <div
                              key={answerIndex}
                              className="flex items-center space-x-2"
                            >
                              <FormField
                                control={form.control}
                                name={`questions.${questionIndex}.answers.${answerIndex}.text`}
                                render={({ field }) => (
                                  <FormItem className="flex-grow">
                                    <FormControl>
                                      <Input
                                        {...field}
                                        placeholder="Answer text"
                                      />
                                    </FormControl>
                                    <FormMessage />
                                  </FormItem>
                                )}
                              />
                              <FormField
                                control={form.control}
                                name={`questions.${questionIndex}.answers.${answerIndex}.isCorrect`}
                                render={({ field }) => (
                                  <FormItem>
                                    <FormControl>
                                      <div className="flex items-center space-x-2">
                                        <Checkbox
                                          checked={field.value}
                                          onCheckedChange={field.onChange}
                                          id={`correct-${questionIndex}-${answerIndex}`}
                                        />
                                        <Label
                                          htmlFor={`correct-${questionIndex}-${answerIndex}`}
                                        >
                                          Correct
                                        </Label>
                                      </div>
                                    </FormControl>
                                  </FormItem>
                                )}
                              />
                              <Button
                                type="button"
                                variant="outline"
                                size="icon"
                                onClick={() => {
                                  const answers = form.getValues(
                                    `questions.${questionIndex}.answers`
                                  );
                                  form.setValue(
                                    `questions.${questionIndex}.answers`,
                                    answers.filter((_, i) => i !== answerIndex)
                                  );
                                }}
                              >
                                <Trash2 className="h-4 w-4" />
                              </Button>
                            </div>
                          ))}
                      </div>
                    )}
                    {form.watch(`questions.${questionIndex}.questionType`) !==
                      QuestionType.OPEN_ENDED && (
                      <Button
                        type="button"
                        variant="outline"
                        size="sm"
                        className="mt-2"
                        onClick={() => {
                          const answers =
                            form.getValues(
                              `questions.${questionIndex}.answers`
                            ) || [];
                          form.setValue(`questions.${questionIndex}.answers`, [
                            ...answers,
                            { text: "", isCorrect: false },
                          ]);
                        }}
                      >
                        Add Answer
                      </Button>
                    )}
                  </div>
                </CardContent>
                <CardFooter>
                  <Button
                    type="button"
                    variant="destructive"
                    onClick={() => removeQuestion(questionIndex)}
                  >
                    Remove Question
                  </Button>
                </CardFooter>
              </Card>
            ))}
            <Button
              type="button"
              onClick={() =>
                appendQuestion({
                  text: "",
                  score: 1,
                  questionType: QuestionType.MULTIPLE_CHOICE,
                  answers: [
                    { text: "", isCorrect: false },
                    { text: "", isCorrect: false },
                  ],
                })
              }
            >
              Add Question
            </Button>
            <Button type="submit">Create Quiz</Button>
          </form>
        </Form>
      </CardContent>
      <Toaster />
    </Card>
  );
}

export default QuizCreate;

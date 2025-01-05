import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { useToast } from "@/hooks/use-toast";
import { useAuth } from "@/contexts/AuthContext";
import {
  fetchCourses,
  fetchCourseStudents,
  createNotification,
} from "@/services/apiService";
import { useNavigate } from "react-router-dom";
import { ICourse, IUser, INotification } from "@/interfaces/interfaces";
import {
  UserRole,
  NotificationType,
  NotificationPriority,
} from "@/interfaces/types";

interface IStudent extends IUser {
  courseId: string;
}

const formSchema = z.object({
  title: z.string().min(1, "Il titolo è obbligatorio"),
  content: z.string().min(1, "Il contenuto è obbligatorio"),
  notificationType: z.nativeEnum(NotificationType),
  priority: z.nativeEnum(NotificationPriority),
  courseId: z.string(),
  recipients: z.array(z.string()).min(1, "Seleziona almeno uno studente"),
});

type FormValues = z.infer<typeof formSchema>;

interface NotificationRequest extends Partial<INotification> {
  recipients: string[];
}

export default function CreateNotification() {
  const { user } = useAuth();
  const [courses, setCourses] = useState<ICourse[]>([]);
  const [students, setStudents] = useState<IStudent[]>([]);
  const [selectedCourse, setSelectedCourse] = useState<string | null>(null);
  const [filteredStudents, setFilteredStudents] = useState<IStudent[]>([]);
  const { toast } = useToast();
  const navigate = useNavigate();

  const form = useForm<FormValues>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      title: "",
      content: "",
      notificationType: NotificationType.SYSTEM,
      priority: NotificationPriority.MEDIUM,
      courseId: "",
      recipients: [],
    },
  });

  useEffect(() => {
    if (!user) {
      navigate("/");
      return;
    }

    if (user.role === UserRole.STUDENT) {
      navigate("/");
    } else {
      fetchCourses(user.role, user.id).then((fetchedCourses) => {
        console.log("Fetched courses:", fetchedCourses);
        setCourses(fetchedCourses);
      });
    }
  }, [user, navigate]);

  useEffect(() => {
    if (selectedCourse && selectedCourse !== "all") {
      fetchCourseStudents(selectedCourse).then((fetchedStudents) => {
        console.log(
          "Fetched students for course",
          selectedCourse,
          ":",
          fetchedStudents
        );
        console.log("Setting students state with:", fetchedStudents);
        setStudents(fetchedStudents);
      });
    } else {
      console.log("Fetching all students");
      fetchAllStudents().then((allStudents) => {
        console.log("Fetched all students:", allStudents);
        setStudents(allStudents);
      });
    }
  }, [selectedCourse]);

  useEffect(() => {
    console.log("Current students:", students);
    console.log("Selected course:", selectedCourse);

    if (students.length > 0) {
      const filtered =
        selectedCourse === "all" || !selectedCourse
          ? students
          : students.filter((student) => student.courseId === selectedCourse);
      console.log("Filtered students:", filtered);
      setFilteredStudents(filtered);
    } else {
      setFilteredStudents([]);
    }
  }, [selectedCourse, students]);

  const fetchAllStudents = async (): Promise<IStudent[]> => {
    // Replace this with your actual API call to fetch all students
    const allStudents = await Promise.all(
      courses.map((course) => fetchCourseStudents(course.id))
    );
    return allStudents.flat();
  };

  const onSubmit = async (data: FormValues) => {
    const notificationRequest: NotificationRequest = {
      ...data,
      senderId: user?.id,
      recipients: data.recipients,
    };

    console.log("Submitting notification request:", notificationRequest);

    try {
      await createNotification(notificationRequest);
      toast({
        title: "Notifica inviata",
        description:
          "La notifica è stata inviata con successo agli studenti selezionati.",
      });
      form.reset();
      navigate("/notifications");
    } catch (error) {
      console.error("Error sending notification:", error);
      toast({
        title: "Errore",
        description:
          "Si è verificato un errore durante l'invio della notifica.",
        variant: "destructive",
      });
    }
  };

  const handleSelectAllStudents = () => {
    const allStudentIds = filteredStudents.map((student) => student.id);
    form.setValue("recipients", allStudentIds);
    console.log("Selected all students:", allStudentIds);
  };

  const handleDeselectAllStudents = () => {
    form.setValue("recipients", []);
    console.log("Deselected all students");
  };

  return (
    <Card className="w-[800px] mx-auto my-8">
      <CardHeader>
        <CardTitle>Crea Nuova Notifica</CardTitle>
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
            <FormField
              control={form.control}
              name="title"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Titolo</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Inserisci il titolo della notifica"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="content"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Contenuto</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Inserisci il contenuto della notifica"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="notificationType"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Tipo di Notifica</FormLabel>
                  <Select
                    onValueChange={field.onChange}
                    defaultValue={field.value}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Seleziona il tipo di notifica" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      <SelectItem value={NotificationType.SYSTEM}>
                        Sistema
                      </SelectItem>
                      <SelectItem value={NotificationType.COURSE}>
                        Corso
                      </SelectItem>
                      <SelectItem value={NotificationType.QUIZ}>
                        Quiz
                      </SelectItem>
                      <SelectItem value={NotificationType.MATERIAL}>
                        Materiale
                      </SelectItem>
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="priority"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Priorità</FormLabel>
                  <Select
                    onValueChange={field.onChange}
                    defaultValue={field.value}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Seleziona la priorità" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      <SelectItem value={NotificationPriority.LOW}>
                        Bassa
                      </SelectItem>
                      <SelectItem value={NotificationPriority.MEDIUM}>
                        Media
                      </SelectItem>
                      <SelectItem value={NotificationPriority.HIGH}>
                        Alta
                      </SelectItem>
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="courseId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Corso</FormLabel>
                  <Select
                    onValueChange={(value) => {
                      field.onChange(value);
                      setSelectedCourse(value === "all" ? null : value);
                    }}
                    defaultValue={field.value}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Seleziona un corso" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      <SelectItem value="all">
                        Nessun corso (tutti gli studenti)
                      </SelectItem>
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
            <FormField
              control={form.control}
              name="recipients"
              render={() => (
                <FormItem>
                  <div className="mb-4">
                    <FormLabel className="text-base">Destinatari</FormLabel>
                    <FormDescription>
                      Seleziona gli studenti a cui inviare la notifica
                    </FormDescription>
                  </div>
                  <div className="flex justify-between mb-2">
                    <Button
                      type="button"
                      variant="outline"
                      onClick={handleSelectAllStudents}
                    >
                      Seleziona tutti
                    </Button>
                    <Button
                      type="button"
                      variant="outline"
                      onClick={handleDeselectAllStudents}
                    >
                      Deseleziona tutti
                    </Button>
                  </div>
                  {filteredStudents.length > 0 ? (
                    filteredStudents.map((student) => (
                      <FormField
                        key={student.id}
                        control={form.control}
                        name="recipients"
                        render={({ field }) => (
                          <FormItem
                            key={student.id}
                            className="flex flex-row items-start space-x-3 space-y-0"
                          >
                            <FormControl>
                              <Checkbox
                                checked={field.value?.includes(student.id)}
                                onCheckedChange={(checked) => {
                                  return checked
                                    ? field.onChange([
                                        ...field.value,
                                        student.id,
                                      ])
                                    : field.onChange(
                                        field.value?.filter(
                                          (value) => value !== student.id
                                        )
                                      );
                                }}
                              />
                            </FormControl>
                            <FormLabel className="font-normal">
                              {student.firstName} {student.lastName}
                            </FormLabel>
                          </FormItem>
                        )}
                      />
                    ))
                  ) : (
                    <p>Nessuno studente disponibile per questo corso.</p>
                  )}
                  <FormMessage />
                </FormItem>
              )}
            />
            <Button type="submit">Invia Notifica</Button>
          </form>
        </Form>
      </CardContent>
    </Card>
  );
}

import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";
import { zodResolver } from "@hookform/resolvers/zod";
import axios from "axios";
import { CheckCircle, Eye, EyeOff, XCircle } from "lucide-react";
import { useState } from "react";
import { useForm, Controller, SubmitHandler } from "react-hook-form";
import { Link } from "react-router-dom";
import { z } from "zod";

const STORAGE_URL = import.meta.env.VITE_STORAGE_URL;

const registerFormSchema = z.object({
  email: z
    .string()
    .email("Invalid email address")
    .regex(
      /^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$/,
      "Invalid email format"
    ),
  password: z.string().min(8, "Password must be at least 8 characters"),
  first_name: z.string().min(1, "First name is required"),
  last_name: z.string().min(1, "Last name is required"),
  role: z.enum(["student", "teacher", "admin"]),
  phone: z.string().optional(),
  address: z.string().optional(),
});

type RegisterFormValues = z.infer<typeof registerFormSchema>;

const initialValues: RegisterFormValues = {
  email: "",
  password: "",
  first_name: "",
  last_name: "",
  role: "student",
  phone: "",
  address: "",
};

const Register = () => {
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(false);
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);

  const {
    register,
    control,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFormValues>({
    defaultValues: initialValues,
    resolver: zodResolver(registerFormSchema),
  });

  const submitHandler: SubmitHandler<RegisterFormValues> = (data) => {
    setError(false);
    setSuccess(false);
    axios
      .post(`${STORAGE_URL}/auth/register`, data)
      .then((res) => {
        console.log(res.data);
        setSuccess(true);
      })
      .catch((err) => {
        setError(true);
        console.log(err);
      });
  };

  return (
    <div className="container max-w-md mx-auto p-6 space-y-5">
      <div className="mb-4">
        <h1 className="font-bold text-2xl">Register</h1>
        <p>Create a new account</p>
      </div>
      <form className="space-y-4" onSubmit={handleSubmit(submitHandler)}>
        <div>
          <Input
            {...register("first_name")}
            placeholder="First name"
            className={errors.first_name ? "border-destructive" : ""}
          />
          {errors.first_name && (
            <span className="text-destructive text-sm">
              {errors.first_name.message}
            </span>
          )}
        </div>
        <div>
          <Input
            {...register("last_name")}
            placeholder="Last name"
            className={errors.last_name ? "border-destructive" : ""}
          />
          {errors.last_name && (
            <span className="text-destructive text-sm">
              {errors.last_name.message}
            </span>
          )}
        </div>
        <div>
          <Input
            {...register("email")}
            placeholder="Email"
            type="email"
            className={errors.email ? "border-destructive" : ""}
          />
          {errors.email && (
            <span className="text-destructive text-sm">
              {errors.email.message}
            </span>
          )}
        </div>
        <div className="relative">
          <Input
            {...register("password")}
            placeholder="Password"
            type={isPasswordVisible ? "text" : "password"}
            className={`pr-10 ${errors.password ? "border-destructive" : ""}`}
          />
          <Button
            type="button"
            className="absolute right-1 top-1/2 -translate-y-1/2 p-0 size-8 cursor-pointer"
            variant="ghost"
            onClick={() => setIsPasswordVisible(!isPasswordVisible)}
          >
            <span className="pointer-events-none">
              {isPasswordVisible ? (
                <EyeOff className="h-4 w-4" />
              ) : (
                <Eye className="h-4 w-4" />
              )}
            </span>
          </Button>
          {errors.password && (
            <span className="text-destructive text-sm">
              {errors.password.message}
            </span>
          )}
        </div>
        <div>
          <Controller
            name="role"
            control={control}
            render={({ field }) => (
              <Select onValueChange={field.onChange} defaultValue={field.value}>
                <SelectTrigger
                  className={errors.role ? "border-destructive" : ""}
                >
                  <SelectValue placeholder="Select role" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="student">Student</SelectItem>
                  <SelectItem value="teacher">Teacher</SelectItem>
                  <SelectItem value="admin">Admin</SelectItem>
                </SelectContent>
              </Select>
            )}
          />
          {errors.role && (
            <span className="text-destructive text-sm">
              {errors.role.message}
            </span>
          )}
        </div>
        <div>
          <Input
            {...register("phone")}
            placeholder="Phone (optional)"
            type="tel"
          />
        </div>
        <div>
          <Textarea {...register("address")} placeholder="Address (optional)" />
        </div>
        <Button type="submit" className="w-full" disabled={isSubmitting}>
          Register
        </Button>
      </form>
      {success && (
        <Alert>
          <CheckCircle className="h-4 w-4" />
          <AlertTitle>Registration Successful!</AlertTitle>
          <AlertDescription>
            You have successfully registered! Please login{" "}
            <Link to="/auth/login" className="underline font-bold">
              here
            </Link>
          </AlertDescription>
        </Alert>
      )}
      {error && (
        <Alert variant="destructive">
          <XCircle className="h-4 w-4" />
          <AlertTitle>Error during registration</AlertTitle>
          <AlertDescription>
            There was an error during the registration
          </AlertDescription>
        </Alert>
      )}
    </div>
  );
};

export default Register;

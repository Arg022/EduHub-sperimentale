import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import { zodResolver } from "@hookform/resolvers/zod";
import { Input } from "@/components/ui/input";
import axios from "axios";
import { CheckCircle, Eye, EyeOff, XCircle } from "lucide-react";
import { useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { Link } from "react-router-dom";
import { z } from "zod";

const STORAGE_URL = import.meta.env.VITE_STORAGE_URL;

const registerFormSchema = z
  .object({
    name: z.string().min(1, "Name is required"),
    email: z.string().email("Invalid email address"),
    password: z.string().min(8, "Password must be at least 8 characters"),
    confirmPassword: z
      .string()
      .min(8, "Confirm password must be at least 8 characters"),
  })
  .superRefine(({ confirmPassword, password }, ctx) => {
    if (confirmPassword !== password) {
      ctx.addIssue({
        code: "custom",
        message: "The passwords did not match",
        path: ["confirmPassword"],
      });
    }
  });

type RegisterFormValues = z.infer<typeof registerFormSchema>;

const initialValues: RegisterFormValues = {
  name: "",
  email: "",
  password: "",
  confirmPassword: "",
};

const Register = () => {
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(false);
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const [isConfirmPasswordVisible, setIsConfirmPasswordVisible] =
    useState(false);

  const {
    register,
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
    <div className="container max-w-md mx-auto p-6 space-y-5 ">
      <div className="mb-4">
        <h1 className="font-bold text-2xl">Register</h1>
        <p>Register a new user</p>
      </div>
      <form className="space-y-4" onSubmit={handleSubmit(submitHandler)}>
        <div>
          <Input
            {...register("name")}
            placeholder="Full name"
            type="text"
            className={cn(errors.name && "border-destructive")}
          />
          {errors.name && (
            <span className="text-destructive text-sm">
              {errors.name.message}
            </span>
          )}
        </div>
        <div>
          <Input
            {...register("email")}
            placeholder="Email"
            type="email"
            className={cn(errors.email && "border-destructive")}
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
            className={cn("pr-10", errors.password && "border-destructive")}
            type={isPasswordVisible ? "text" : "password"}
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
        <div className="relative">
          <Input
            {...register("confirmPassword")}
            placeholder="Confirm Password"
            className={cn(
              "pr-10",
              errors.confirmPassword && "border-destructive"
            )}
            type={isConfirmPasswordVisible ? "text" : "password"}
          />
          <Button
            type="button"
            className="absolute right-1 top-1/2 -translate-y-1/2 p-0 size-8 cursor-pointer"
            variant="ghost"
            onClick={() =>
              setIsConfirmPasswordVisible(!isConfirmPasswordVisible)
            }
          >
            <span className="pointer-events-none">
              {isConfirmPasswordVisible ? (
                <EyeOff className="h-4 w-4" />
              ) : (
                <Eye className="h-4 w-4" />
              )}
            </span>
          </Button>
          {errors.confirmPassword && (
            <span className="text-destructive text-sm">
              {errors.confirmPassword.message}
            </span>
          )}
        </div>
        <Button type="submit" className="w-full" disabled={isSubmitting}>
          Register
        </Button>
      </form>
      {success && (
        <Alert className="flex flex-col md:flex-row items-center space-y-2 md:space-y-0 md:space-x-2">
          <CheckCircle className="h-4 w-4" />
          <div>
            <AlertTitle>Registration Successful!</AlertTitle>
            <AlertDescription>
              You have successfully registered! Please login{" "}
              <Link to="/auth/login" className="underline font-bold">
                here
              </Link>
            </AlertDescription>
          </div>
        </Alert>
      )}
      {error && (
        <Alert
          variant="destructive"
          className="flex flex-col md:flex-row items-center space-y-2 md:space-y-0 md:space-x-2"
        >
          <XCircle className="h-4 w-4" />
          <div>
            <AlertTitle>Error during registration</AlertTitle>
            <AlertDescription>
              There was an error during the registration
            </AlertDescription>
          </div>
        </Alert>
      )}
    </div>
  );
};

export default Register;

import { Input } from "./ui/input";
import {
    useEffect,
    //ChangeEvent,
    //useEffect,
    useState,
} from "react";
import { Button } from "./ui/button";
import { EyeOff, Eye } from "lucide-react";
import { SubmitHandler, useForm } from "react-hook-form";
import { cn } from "@/lib/utils";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Navigate, useNavigate } from "react-router-dom";
import IUser from "@/interfaces/User";

interface IUserForm {
    name: {
        firstName: string;
        lastName: string;
    };
    email: string;
    password: string;
}

const userSchema = z.object({
    name: z.object({
        firstName: z.string().min(3, "Minimo 3 caratteri"),
        lastName: z.string().min(3, "Minimo 3 caratteri"),
    }),
    email: z.string().email(),
    password: z.string().min(8),
});

type UserFormValues = z.infer<typeof userSchema>;

const initialValues = {
    name: {
        firstName: "",
        lastName: "",
    },
    email: "",
    password: "",
};

const UserForm = ({ title }: { title: string }) => {
    useEffect(() => {
        document.title = title;
    }, [title]);

    /* controlled form state traditional way */
    // const [formValues, setFormValues] = useState<IUserForm>(initialValues);

    // const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    //     const { name, value } = e.target;

    //     setFormValues((prevFormValues) => ({
    //         ...prevFormValues,
    //         [name]: value,
    //     }));
    // };

    const [isPasswordVisible, setIsPasswordVisible] = useState(false);

    /* react hook form implementation */
    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm<UserFormValues>({
        defaultValues: initialValues,
        resolver: zodResolver(userSchema),
    });

    const navigate = useNavigate();

    const submitHandler: SubmitHandler<UserFormValues> = (data) => {
        // data contiene valori che hanno passato la validazione
        navigate("/");
    };

    //const inputRef = useRef<HTMLInputElement>(null);
    // useEffect(() => {
    //     console.log(inputRef.current);
    //     setTimeout(() => {
    //         inputRef.current?.focus();
    //     }, 2000);
    // }, [inputRef]);

    return (
        <>
            <h1>{title}</h1>
            <form className="space-y-4" onSubmit={handleSubmit(submitHandler)}>
                <Input
                    {...register("name.firstName")}
                    placeholder="First name"
                    type="text"
                    className={cn(errors.name?.firstName && "outline outline-destructive")}
                    // name="name"
                    // value={formValues.name}
                    // onChange={handleInputChange}
                />
                {errors.name?.firstName && (
                    <span className="text-destructive">
                        {errors.name.firstName.message as string}
                    </span>
                )}
                <Input
                    {...register("name.lastName")}
                    placeholder="Last name"
                    type="text"
                    className={cn(errors.name?.lastName && "outline outline-destructive")}
                    // name="name"
                    // value={formValues.name}
                    // onChange={handleInputChange}
                />
                {errors.name?.lastName && (
                    <span className="text-destructive">
                        {errors.name.lastName.message as string}
                    </span>
                )}
                <Input
                    {...register("email", { required: true })}
                    placeholder="Email"
                    type="email"
                    // name="email"
                    // value={formValues.email}
                    // onChange={handleInputChange}
                />
                <div className="relative">
                    <Input
                        {...register("password", { required: true })}
                        placeholder="Password"
                        className="pr-10"
                        name="password"
                        type={isPasswordVisible ? "text" : "password"}
                        // value={formValues.password}
                        // onChange={handleInputChange}
                    />
                    <Button
                        type="button"
                        className="absolute right-1 top-1/2 -translate-y-1/2 p-0 size-8 cursor-pointer"
                        variant="ghost"
                        onClick={() => {
                            setIsPasswordVisible(!isPasswordVisible);
                        }}>
                        <span className="pointer-events-none">
                            {isPasswordVisible ? <EyeOff /> : <Eye />}
                        </span>
                    </Button>
                </div>
                <Button type="submit" disabled={isSubmitting}>
                    Invia
                </Button>
            </form>
        </>
    );
};

export default UserForm;

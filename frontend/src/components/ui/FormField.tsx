import React from "react";
import { useFormContext, Controller } from "react-hook-form";
import { Input } from "@/components/ui/Input";
import { Textarea } from "@/components/ui/Textarea";
import {
  Select,
  SelectItem,
  SelectTrigger,
  SelectValue,
  SelectContent,
} from "@/components/ui/Select";

interface FormFieldProps {
  name: string;
  label: string;
  type?: "text" | "textarea" | "select";
  options?: { value: string; label: string }[];
}

const FormField: React.FC<FormFieldProps> = ({
  name,
  label,
  type = "text",
  options = [],
}) => {
  const { control } = useFormContext();

  return (
    <Controller
      name={name}
      control={control}
      render={({ field, fieldState: { error } }) => (
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700">
            {label}
          </label>
          {type === "text" && <Input {...field} />}
          {type === "textarea" && <Textarea {...field} />}
          {type === "select" && (
            <Select {...field}>
              <SelectTrigger>
                <SelectValue placeholder={`Select ${label}`} />
              </SelectTrigger>
              <SelectContent>
                {options.map((option) => (
                  <SelectItem key={option.value} value={option.value}>
                    {option.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          )}
          {error && (
            <p className="mt-2 text-sm text-red-600">{error.message}</p>
          )}
        </div>
      )}
    />
  );
};

export default FormField;

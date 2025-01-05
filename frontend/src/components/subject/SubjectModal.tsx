import React from "react";
import { useForm } from "react-hook-form";
import { createSubject } from "@/services/apiService";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";

interface SubjectModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubjectCreated: () => void;
}

const SubjectModal: React.FC<SubjectModalProps> = ({ isOpen, onClose, onSubjectCreated }) => {
  const { register, handleSubmit, formState: { errors } } = useForm();

  const onSubmit = async (data: any) => {
    try {
      await createSubject(data);
      onSubjectCreated();
      onClose();
    } catch (error) {
      console.error("Failed to create subject", error);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded shadow-lg w-1/3">
        <h2 className="text-2xl mb-4">Crea Materia</h2>
        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="name">
              Nome
            </label>
            <Input
              id="name"
              type="text"
              {...register("name", { required: "Nome è obbligatorio" })}
            />
            {errors.name && typeof errors.name.message === "string" && (
              <p className="text-red-500 text-xs italic">{errors.name.message}</p>
            )}
          </div>
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="description">
              Descrizione
            </label>
            <Textarea
              id="description"
              {...register("description")}
            />
          </div>
          <div className="flex justify-end">
            <Button type="button" onClick={onClose} className="mr-2">Annulla</Button>
            <Button type="submit">Crea</Button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default SubjectModal;
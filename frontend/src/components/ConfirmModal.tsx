import React from "react";
import { Button } from "@/components/ui/button";

interface ConfirmModalProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  message: string;
}

const ConfirmModal: React.FC<ConfirmModalProps> = ({
  isOpen,
  onClose,
  onConfirm,
  message,
}) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50 bg-black bg-opacity-50">
      <div className="bg-white p-6 rounded shadow-lg w-1/3">
        <h2 className="text-2xl mb-4">Conferma</h2>
        <p className="mb-4">{message}</p>
        <div className="flex justify-end">
          <Button type="button" onClick={onClose} className="mr-2">
            Annulla
          </Button>
          <Button type="button" onClick={onConfirm}>
            Conferma
          </Button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmModal;

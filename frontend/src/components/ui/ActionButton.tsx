import React from "react";
import { Button } from "@/components/ui/button";

interface ActionButtonProps {
  label: string;
  onClick: () => void;
  variant?: "default" | "outline" | "destructive";
}

const ActionButton: React.FC<ActionButtonProps> = ({
  label,
  onClick,
  variant = "default",
}) => {
  return (
    <Button variant={variant} onClick={onClick}>
      {label}
    </Button>
  );
};

export default ActionButton;

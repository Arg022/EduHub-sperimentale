import { useState, useEffect } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { fetchStudents, fetchTeachers, fetchSubjects } from "@/services/apiService";
import { IUser, ISubject } from "@/interfaces/interfaces";

type ISimpleUser = Omit<IUser, 'password' | 'role' | 'registrationDate'>;

interface AddEntityModalProps {
  isOpen: boolean;
  onClose: () => void;
  onAdd: (entity: ISimpleUser, subjectId?: string) => Promise<void>;
  entityType: "student" | "teacher";
}

export const AddEntityModal: React.FC<AddEntityModalProps> = ({
  isOpen,
  onClose,
  onAdd,
  entityType,
}) => {
  const [users, setUsers] = useState<ISimpleUser[]>([]);
  const [subjects, setSubjects] = useState<ISubject[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedUser, setSelectedUser] = useState<ISimpleUser | null>(null);
  const [selectedSubject, setSelectedSubject] = useState<string | null>(null);

  useEffect(() => {
    const loadUsers = async () => {
      try {
        let usersData: ISimpleUser[] = [];
        if (entityType === "student") {
          usersData = await fetchStudents();
        } else {
          usersData = await fetchTeachers();
          const subjectsData = await fetchSubjects();
          setSubjects(subjectsData);
        }
        setUsers(usersData);
      } catch (error) {
        console.error("Failed to load users", error);
      }
    };

    if (isOpen) {
      loadUsers();
    }
  }, [isOpen, entityType]);

  const filteredUsers = users.filter(
    (user) =>
      user.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      user.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      user.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (selectedUser) {
      onAdd(selectedUser, selectedSubject ?? undefined);
      setSelectedUser(null);
      setSelectedSubject(null);
      setSearchTerm("");
      onClose();
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            Add New {entityType === "student" ? "Student" : "Teacher"}
          </DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit}>
          <div className="grid gap-4 py-4">
            <Input
              placeholder="Cerca..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="mb-4"
            />
            <div className="max-h-60 overflow-y-auto">
              {filteredUsers.map((user) => (
                <div
                  key={user.id}
                  className={`p-2 cursor-pointer ${
                    selectedUser?.id === user.id ? "bg-gray-200" : ""
                  }`}
                  onClick={() => setSelectedUser(user)}
                >
                  <p>
                    {user.firstName} {user.lastName}
                  </p>
                  <p className="text-sm text-gray-500">{user.email}</p>
                </div>
              ))}
            </div>
            {entityType === "teacher" && (
              <Select
                onValueChange={(value) => setSelectedSubject(value)}
                defaultValue={selectedSubject || ""}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Select Subject" />
                </SelectTrigger>
                <SelectContent>
                  {subjects.map((subject) => (
                    <SelectItem key={subject.id} value={subject.id}>
                      {subject.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            )}
          </div>
          <DialogFooter>
            <Button type="submit" disabled={!selectedUser || (entityType === "teacher" && !selectedSubject)}>
              Add {entityType === "student" ? "Student" : "Teacher"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default AddEntityModal;
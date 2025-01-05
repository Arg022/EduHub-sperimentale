import React, { useState, useEffect } from "react";
import { useAuth } from "@/contexts/AuthContext";
import { fetchSubjects } from "@/services/apiService";
import { ISubject } from "@/interfaces/interfaces";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router-dom";
import SubjectModal from "@/components/subject/SubjectModal";

const SubjectList: React.FC = () => {
  const { user } = useAuth();
  const [subjects, setSubjects] = useState<ISubject[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const loadSubjects = async () => {
      if (!user) {
        console.error("User is not authenticated");
        return;
      }

      try {
        const subjectsData = await fetchSubjects(user.id, user.role);
        setSubjects(subjectsData);
      } catch (error) {
        console.error("Failed to load subjects", error);
      }
    };

    loadSubjects();
  }, [user]);

  if (!user) {
    return <p>Loading...</p>;
  }

  const handleCreateSubject = () => {
    setIsModalOpen(true);
  };

  const handleModalClose = () => {
    setIsModalOpen(false);
  };

  const handleSubjectCreated = async () => {
    if (user) {
      const subjectsData = await fetchSubjects(user.id, user.role);
      setSubjects(subjectsData);
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">Materie</h1>
      {user.role === "ADMIN" && (
        <Button className="mb-4" onClick={handleCreateSubject}>
          Crea Materia
        </Button>
      )}
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        {subjects.map((subject) => (
          <Card key={subject.id}>
            <CardHeader>
              <CardTitle>{subject.name}</CardTitle>
            </CardHeader>
            <CardContent>
              <p>{subject.description}</p>
            </CardContent>
          </Card>
        ))}
      </div>
      {subjects.length === 0 && (
        <p className="text-center text-gray-500 mt-6">Nessuna materia trovata.</p>
      )}
      <SubjectModal
        isOpen={isModalOpen}
        onClose={handleModalClose}
        onSubjectCreated={handleSubjectCreated}
      />
    </div>
  );
};

export default SubjectList;
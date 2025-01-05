import React, { useState, useEffect } from "react";
import { useAuth } from "@/contexts/AuthContext";
import { uploadMaterial, fetchCourses } from "@/services/apiService";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useToast } from "@/hooks/use-toast";
import { ICourse } from "@/interfaces/interfaces";
import { useNavigate } from "react-router-dom"; // Import useNavigate

const MaterialUpload = () => {
  const { user } = useAuth();
  const [file, setFile] = useState<File | null>(null);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [courseId, setCourseId] = useState("");
  const [courses, setCourses] = useState<ICourse[]>([]);
  const { toast } = useToast();
  const navigate = useNavigate(); // Initialize useNavigate

  useEffect(() => {
    const fetchCoursesData = async () => {
      try {
        const coursesData = await fetchCourses(user?.role, user?.id);
        setCourses(coursesData);
      } catch (error) {
        console.error("Error fetching courses", error);
      }
    };

    fetchCoursesData();
  }, [user]);

  const handleInputFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const {
      target: { files },
    } = e;
    if (files?.length) {
      setFile(files[0]);
    }
  };

  const handleFileUpload = async () => {
    if (file && courseId) {
      const formData = new FormData();
      formData.append("file", file);
      formData.append("title", title);
      formData.append("description", description);
      formData.append("courseId", courseId);
      formData.append("creatorId", user?.id || "");

      try {
        await uploadMaterial(formData);
        toast({
          title: "Material Uploaded",
          description: "The material has been uploaded successfully.",
        });
        setFile(null);
        setTitle("");
        setDescription("");
        setCourseId("");
        navigate("/materials");
      } catch (error) {
        console.error("Error uploading material", error);
        toast({
          title: "Error",
          description: "There was an error uploading the material.",
          variant: "destructive",
        });
      }
    } else {
      toast({
        title: "Error",
        description: "Please fill in all required fields.",
        variant: "destructive",
      });
    }
  };

  return (
    <div className="container max-w-md mx-auto py-6 space-y-5">
      <div className="mb-4">
        <h1 className="font-bold text-2xl">Upload a file</h1>
        <p>Upload a new file</p>
      </div>
      <div className="grid gap-4 py-4">
        <div className="grid grid-cols-4 items-center gap-4">
          <Label htmlFor="title" className="text-right">
            Title
          </Label>
          <Input
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="col-span-3"
          />
        </div>
        <div className="grid grid-cols-4 items-center gap-4">
          <Label htmlFor="description" className="text-right">
            Description
          </Label>
          <Input
            id="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="col-span-3"
          />
        </div>
        <div className="grid grid-cols-4 items-center gap-4">
          <Label htmlFor="courseId" className="text-right">
            Course
          </Label>
          <Select
            onValueChange={(value) => setCourseId(value)}
            value={courseId}
          >
            <SelectTrigger className="col-span-3">
              <SelectValue placeholder="Select a course" />
            </SelectTrigger>
            <SelectContent>
              {courses.map((course) => (
                <SelectItem key={course.id} value={course.id}>
                  {course.name}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        <label htmlFor="file-input" className="cursor-pointer">
          <Input
            id="file-input"
            type="file"
            onChange={handleInputFileChange}
            multiple
          />
          {file && <p>{file.name}</p>}
        </label>
        <Button onClick={handleFileUpload}>Upload File</Button>
      </div>
    </div>
  );
};

export default MaterialUpload;

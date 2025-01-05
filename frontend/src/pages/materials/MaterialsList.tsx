import React, { useState, useEffect } from "react";
import { useAuth } from "@/contexts/AuthContext";
import { fetchMaterials, downloadMaterial } from "@/services/apiService";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router-dom";
import { IMaterial } from "@/interfaces/interfaces";

const MaterialsList = () => {
  const { user } = useAuth();
  const [materials, setMaterials] = useState<IMaterial[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    if (!user) return;

    const fetchMaterialsData = async () => {
      try {
        const materialsData = await fetchMaterials(user.id, user.role);
        setMaterials(materialsData);
      } catch (error) {
        console.error("Error fetching materials", error);
      }
    };

    fetchMaterialsData();
  }, [user]);

  if (!user) {
    return <div>Loading...</div>;
  }

  const handleDownload = async (materialId: string, title: string) => {
    try {
      const blob = await downloadMaterial(materialId);
      const url = window.URL.createObjectURL(new Blob([blob]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", title);
      document.body.appendChild(link);
      link.click();
      if (link.parentNode) {
        link.parentNode.removeChild(link);
      }
    } catch (error) {
      console.error("Error downloading material", error);
    }
  };

  const handleUpload = () => {
    navigate("/materials/upload");
  };

  return (
    <Card className="w-[800px] mx-auto my-8">
      <CardHeader>
        <CardTitle>Materials List</CardTitle>
      </CardHeader>
      <CardContent>
        {user.role === "ADMIN" || user.role === "TEACHER" ? (
          <Button className="mb-4" onClick={handleUpload}>
            Aggiungi File
          </Button>
        ) : null}
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Title</TableHead>
              <TableHead>Description</TableHead>
              <TableHead>Type</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {materials.map((material) => (
              <TableRow key={material.id}>
                <TableCell>{material.title}</TableCell>
                <TableCell>{material.description}</TableCell>
                <TableCell>{material.type}</TableCell>
                <TableCell>
                  <Button
                    onClick={() => handleDownload(material.id, material.title)}
                  >
                    Download
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
};

export default MaterialsList;

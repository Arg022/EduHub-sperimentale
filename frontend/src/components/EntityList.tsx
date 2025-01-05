import { Input } from "@/components/ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";

interface IEntity {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  enrollmentDate?: string;
  startDate?: string;
  endDate?: string;
}

interface EntityListProps {
  title: string;
  entities: IEntity[];
  onAdd: () => void;
}

const EntityList: React.FC<EntityListProps> = ({ title, entities, onAdd }) => {
  const [searchTerm, setSearchTerm] = useState("");

  const filteredEntities = entities.filter(
    (entity) =>
      entity.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      entity.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      entity.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <Card className="mt-6">
      <CardHeader>
        <CardTitle>{title}</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="flex justify-between items-center mb-4">
          <Input
            placeholder="Cerca..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="max-w-sm"
          />
          <Button onClick={onAdd}>Aggiungi</Button>
        </div>

        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nome</TableHead>
              <TableHead>Email</TableHead>
              <TableHead>Data di Iscrizione/Inizio</TableHead>
              <TableHead>Data di Fine</TableHead>
              <TableHead>Azioni</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filteredEntities.map((entity) => (
              <TableRow key={entity.id}>
                <TableCell>
                  {entity.firstName} {entity.lastName}
                </TableCell>
                <TableCell>{entity.email}</TableCell>
                <TableCell>
                  {entity.enrollmentDate || entity.startDate || "N/A"}
                </TableCell>
                <TableCell>{entity.endDate || "N/A"}</TableCell>
                <TableCell>
                  <Button variant="ghost" size="sm">
                    Modifica
                  </Button>
                  <Button variant="ghost" size="sm" className="text-red-500">
                    Rimuovi
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

export default EntityList;

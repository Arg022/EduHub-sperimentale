import React from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";

interface EntityListProps {
  title: string;
  entities: any[];
  onAdd: () => void;
}

const EntityList: React.FC<EntityListProps> = ({ title, entities, onAdd }) => {
  return (
    <div>
      <h2 className="text-xl font-bold mb-4">{title}</h2>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Name</TableHead>
            <TableHead>Email</TableHead>
            <TableHead>Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {entities.map((entity) => (
            <TableRow key={entity.id}>
              <TableCell>
                {entity.firstName} {entity.lastName}
              </TableCell>
              <TableCell>{entity.email}</TableCell>
              <TableCell>
                <Button variant="outline" size="sm">
                  Edit
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <Button onClick={onAdd} className="mt-4">
        Add New
      </Button>
    </div>
  );
};

export default EntityList;

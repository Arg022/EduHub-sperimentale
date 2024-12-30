import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { useStoreContext } from "@/contexts/StoreContext";
import IUser from "@/interfaces/User";
import { Eye, Pencil, Trash2 } from "lucide-react";
import { Link } from "react-router-dom";

export const UserCard = ({ id, name, username, email }: IUser) => {
    const { handleUserEdit, handleUserDelete } = useStoreContext();

    return (
        <Card className="w-full">
            <CardContent className="flex items-center justify-between p-6">
                <div className="flex items-center space-x-4">
                    <Avatar className="h-20 w-20">
                        <AvatarFallback>
                            {name
                                ?.split(" ")
                                .map((n) => n[0])
                                .join("")
                                .toUpperCase()}
                        </AvatarFallback>
                    </Avatar>
                    <div>
                        <h2 className="text-2xl font-bold">{name}</h2>
                        <p className="text-sm text-muted-foreground">{email}</p>
                        <Badge variant="secondary" className="mt-2">
                            {username}
                        </Badge>
                    </div>
                </div>
                <div className="flex space-x-2">
                    <Button asChild variant="outline" size="icon">
                        <Link to={`users/${id}`}>
                            <Eye className="h-4 w-4" />
                            <span className="sr-only">Show</span>
                        </Link>
                    </Button>
                    <Button
                        variant="outline"
                        size="icon"
                        onClick={() =>
                            handleUserEdit({
                                id,
                                name: "John Doe",
                                email: "john@example.com",
                            })
                        }>
                        <Pencil className="h-4 w-4" />
                        <span className="sr-only">Edit</span>
                    </Button>
                    <Button variant="outline" size="icon" onClick={() => handleUserDelete(id)}>
                        <Trash2 className="h-4 w-4" />
                        <span className="sr-only">Delete</span>
                    </Button>
                </div>
            </CardContent>
        </Card>
    );
};

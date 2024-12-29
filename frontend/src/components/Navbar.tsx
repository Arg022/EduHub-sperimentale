import * as React from "react";
import { Laptop, Moon, Sun, User } from "lucide-react";

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Link } from "react-router-dom";
import { useAuth } from "@/contexts/AuthContext";

export function Navbar() {
    const { user, logout } = useAuth();

    return (
        <nav className="border-b">
            <div className="flex h-16 items-center px-4">
                <div className="flex items-center space-x-4">
                    <Link to="/" className="font-semibold flex items-center gap-4">
                        <User className="h-6 w-6" />
                        User Management
                    </Link>
                    {/* <Link to="/" className="font-semibold">
                        Home
                    </Link>
                    <Link to="/about" className="font-semibold">
                        About
                    </Link>
                    <Link to="/contact" className="font-semibold">
                        Contact
                    </Link> */}
                </div>
                <div className="ml-auto flex items-center space-x-4">
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Avatar>
                                <AvatarImage
                                    src="/placeholder.svg?height=32&width=32"
                                    alt="@johndoe"
                                />
                                <AvatarFallback>
                                    {`${user?.name.charAt(0)}${user?.name.charAt(1)}`.toUpperCase()}
                                </AvatarFallback>
                            </Avatar>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent className="w-56" align="end" forceMount>
                            <DropdownMenuLabel className="font-normal">
                                <div className="flex flex-col space-y-1">
                                    <p className="text-sm font-medium leading-none">{user?.name}</p>
                                    <p className="text-xs leading-none text-muted-foreground">
                                        {user?.email}
                                    </p>
                                </div>
                            </DropdownMenuLabel>
                            <DropdownMenuSeparator />
                            <DropdownMenuItem>
                                <User className="mr-2 h-4 w-4" />
                                <span>Profile</span>
                            </DropdownMenuItem>
                            <DropdownMenuItem>
                                <Laptop className="mr-2 h-4 w-4" />
                                <span>Dashboard</span>
                            </DropdownMenuItem>
                            <DropdownMenuSeparator />
                            <DropdownMenuItem onClick={logout}>Log out</DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                </div>
            </div>
        </nav>
    );
}

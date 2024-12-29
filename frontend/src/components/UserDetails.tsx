import IUser from "@/interfaces/User";
import { ChangeEvent, ChangeEventHandler, useEffect, useState } from "react";
import { useParams, useSearchParams } from "react-router-dom";

const API_URL = import.meta.env.VITE_API_URL;

const UserDetails = () => {
    const { userId } = useParams();
    const [user, setUser] = useState<IUser | null>(null);

    const [searchParams, setSearchParams] = useSearchParams();

    useEffect(() => {
        fetch(`${API_URL}/users/${userId}`)
            .then((res) => res.json())
            .then((userData) => setUser(userData));
    }, []);

    useEffect(() => {
        console.log(searchParams.get("color"));
    }, [searchParams]);

    const onColorInputChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { value } = e.target;
        setSearchParams({ color: value });
    };

    return user ? (
        <>
            <input
                type="color"
                value={searchParams.get("color") ?? "rgb(255,255,255)"}
                onChange={onColorInputChange}
            />
            <div style={{ backgroundColor: searchParams.get("color") ?? "#FFF" }}>
                <h1>User Details</h1>
                <p>Name: {user.name}</p>
                <p>Email: {user.email}</p>
                <p>Age: {user.username}</p>
            </div>
        </>
    ) : (
        <>Loading</>
    );
};

export default UserDetails;

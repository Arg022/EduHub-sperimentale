import { cn } from "@/lib/utils";
import { Plus } from "lucide-react";
import { Fragment } from "react/jsx-runtime";
import { Button } from "@/components/ui/button";
import { UserCard } from "@/components/UserCard";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useStoreContext } from "@/contexts/StoreContext";

const UsersList = () => {
    const [loading, setLoading] = useState(false);

    const { users, handleUserCreate } = useStoreContext();

    // useEffect lancia una funzione per gestire un side effect al cambiare delle sue deps
    // la callback che passo a useEffect viene eseguita almeno una volta al mount del componente
    // deps è opzionale, se lo ometto la funzione viene eseguita ad ogni update del componente
    // se definisco deps come [] (empty array) la callback viene eseguita esclusivamente al mount
    // se definisco una o più dependencies (states o props) al cambiare del suo/loro valore viene eseguita nuovamente la callback
    useEffect(
        () => {
            // istruzione da eseguire
            return () => {}; // eventuale cleanup function
        }, // effect callback
        [] // dependencies
    );

    const renderList = () => {
        return users.length > 0 ? (
            users.map((user) => {
                return (
                    <Fragment key={user.id}>
                        {/* <UserItem
                            {...user}
                            key={user.id}
                            onUserEdit={handleUserEdit}
                            onUserDelete={handleUserDelete}
                        /> */}
                        <UserCard {...user} />
                    </Fragment>
                );
            })
        ) : (
            <p>No users</p>
        );
    };

    return (
        <section className="w-full max-w-3xl mx-auto gap-5 p-5 flex flex-col items-center justify-center dark:bg-green-500">
            <div className="w-full flex justify-between items-center">
                <h1
                    className={cn("text-2xl font-bold", {
                        "text-red-500": loading,
                    })}>
                    Lista utenti
                </h1>
                <Button asChild onClick={() => {}}>
                    <Link to="/add-user">
                        <Plus />
                    </Link>
                </Button>
            </div>
            {loading ? <p>Loading...</p> : renderList()}
        </section>
    );
};

export default UsersList;

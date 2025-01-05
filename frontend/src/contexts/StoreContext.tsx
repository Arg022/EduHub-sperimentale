import IUser from "@/interfaces/User";
import axios from "axios";
import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useReducer,
  useState,
} from "react";

interface IStoreContextState {
  users: IUser[];
  products: any[];
}

interface IStoreContextProps extends IStoreContextState {
  dispatch: React.Dispatch<StoreAction>;
  handleUserCreate: (userData: Omit<IUser, "id">) => void;
  handleUserEdit: (userUpdData: Partial<IUser>) => void;
  handleUserDelete: (id: IUser["id"]) => void;
}

type StoreAction =
  | {
      type: "SET_USERS";
      payload: {
        users: IUser[];
      };
    }
  | {
      type: "ADD_USER";
      payload: {
        user: IUser;
      };
    }
  | {
      type: "UPDATE_USER";
      payload: {
        user: Partial<IUser>;
      };
    }
  | {
      type: "DELETE_USER";
      payload: {
        userId: IUser["id"];
      };
    };

export const StoreContext = createContext<IStoreContextProps | null>(null);

const storeReducer = (
  state: IStoreContextState,
  action: StoreAction
): IStoreContextState => {
  switch (action.type) {
    case "SET_USERS":
      return {
        ...state,
        users: action.payload.users,
      };
    case "ADD_USER":
      return {
        ...state,
        users: [...state.users, action.payload.user],
      };

    case "UPDATE_USER":
      return {
        ...state,
        users: state.users.map((user) => {
          const { user: payloadUser } = action.payload;
          if (user.id === payloadUser.id) {
            // devo tornare l'utente modificato
            return {
              ...user,
              ...payloadUser,
            };
          }
          return user;
        }),
      };

    case "DELETE_USER":
      return {
        ...state,
        users: state.users.filter((user) => user.id !== action.payload.userId),
      };

    default:
      return state;
  }
};

const initialState: IStoreContextState = {
  users: [],
  products: [],
};

export const StoreProvider = ({ children }: { children: ReactNode }) => {
  const [state, dispatch] = useReducer(storeReducer, initialState);

  useEffect(() => {
    axios
      .get("https://jsonplaceholder.typicode.com/users")
      .then(({ data: usersData }) => {
        dispatch({
          type: "SET_USERS",
          payload: {
            users: usersData,
          },
        });
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  function handleUserCreate(userData: Omit<IUser, "id">) {
    axios
      .post("https://jsonplaceholder.typicode.com/users", userData)
      .then(({ data: id }) => {
        dispatch({
          type: "ADD_USER",
          payload: {
            user: {
              id,
              ...userData,
            },
          },
        });
      })
      .catch((err) => {
        console.log(err);
      });
  }

  function handleUserEdit(userUpdData: Partial<IUser>) {
    axios
      .put("https://jsonplaceholder.typicode.com/users/" + userUpdData.id, {
        name: userUpdData.name,
      })
      .then(() => {
        dispatch({
          type: "UPDATE_USER",
          payload: {
            user: userUpdData,
          },
        });
      });
  }

  function handleUserDelete(id: IUser["id"]) {
    axios
      .delete("https://jsonplaceholder.typicode.com/users/" + id)
      .then(() => {
        dispatch({
          type: "DELETE_USER",
          payload: {
            userId: id,
          },
        });
      })
      .catch((err) => console.log(err));
  }

  return (
    <StoreContext.Provider
      value={{
        ...state,
        dispatch,
        handleUserCreate,
        handleUserEdit,
        handleUserDelete,
      }}
    >
      {children}
    </StoreContext.Provider>
  );
};

export const useStoreContext = () => {
  const context = useContext(StoreContext);
  if (!context) {
    throw new Error("useStoreContext must be used inside StoreProvider");
  }
  return context;
};

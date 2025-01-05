import { createContext, useContext, useState, ReactNode } from "react";

interface AppContextProps {
  exampleState: string;
  setExampleState: (value: string) => void;
}

const AppContext = createContext<AppContextProps | undefined>(undefined);

export const AppProvider = ({ children }: { children: ReactNode }) => {
  const [exampleState, setExampleState] = useState<string>("");

  return (
    <AppContext.Provider value={{ exampleState, setExampleState }}>
      {children}
    </AppContext.Provider>
  );
};

export const useAppContext = () => {
  const context = useContext(AppContext);
  if (context === undefined) {
    throw new Error("useAppContext must be used within an AppProvider");
  }
  return context;
};


import { createContext, useContext } from 'react';

const VotingContext = createContext();

export const VotingProvider = ({ children }) => {
  return (
    <VotingContext.Provider value={{}}>
      {children}
    </VotingContext.Provider>
  );
};

export const useVoting = () => useContext(VotingContext);

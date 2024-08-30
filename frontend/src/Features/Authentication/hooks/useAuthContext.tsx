import { useContext } from 'react';


export const useAuthMessage = () => {
    const context = useContext(null);
    if (!context) {
        throw new Error('useAuthMessage must be used within an AuthProvider');
    }
    return context;
};

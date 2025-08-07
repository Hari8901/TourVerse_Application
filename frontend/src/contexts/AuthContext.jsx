import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [token, setToken] = useState(localStorage.getItem('token'));

    useEffect(() => {
        // Simulate checking for existing auth
        setTimeout(() => {
            setLoading(false);
        }, 1000);
    }, []);

    const login = async (credentials) => {
        try {
            // Simulate API call
            setUser({ name: 'John Doe', email: credentials.email });
            setToken('fake-jwt-token');
            localStorage.setItem('token', 'fake-jwt-token');
            return { success: true };
        } catch (error) {
            return { success: false, error: 'Login failed' };
        }
    };

    const register = async (userData) => {
        try {
            // Simulate API call
            return { success: true };
        } catch (error) {
            return { success: false, error: 'Registration failed' };
        }
    };

    const logout = async () => {
        try {
            if (token) {
                // await authAPI.logout(); // Uncomment when API is available
            }
        } catch (error) {
            console.error('Logout error:', error);
        } finally {
            localStorage.removeItem('token');
            setToken(null);
            setUser(null);
        }
    };

    const value = {
        user,
        token,
        login,
        register,
        logout,
        loading,
        isAuthenticated: !!user
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};

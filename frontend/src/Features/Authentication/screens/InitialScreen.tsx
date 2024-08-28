import React, { useState } from 'react';
import { CustomFetchResult, setUser } from '../../../redux';
import { useDispatch } from 'react-redux';
import { useNavigate, } from 'react-router-dom';
import { host } from '../../Common/connectionConfig';

//import { Alert, AlertDescription } from '@/components/ui/alert';

async function originalRequest(endpoint, config) {
    try {
        let customResponse: CustomFetchResult = {
            status: 0,
            statusText: '',
            data: undefined,
        }
        let response = await fetch(host + endpoint, config);
        customResponse.status = response.status;
        customResponse.statusText = response.statusText;
        const text = await response.text();
        if (!text) {
            customResponse.data = null
        } else {
            try {
                customResponse.data = JSON.parse(text);
            } catch (error: any) {
                customResponse.data = null;
            }
        }
        return customResponse
    } catch (error) {
        throw error;
    }
};
export const InitialScreen = () => {
    const [isLogin, setIsLogin] = useState(true);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const dispatch = useDispatch<any>();
    const navigate = useNavigate();
    const handleSubmit = async (e: any) => {
        e.preventDefault();
        setError('');

        if (!email || !password) {
            setError('Please fill in all fields');
            return;
        }

        if (!isLogin && password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        // Here you would typically make an API call to your backend
        console.log(isLogin ? 'Logging in...' : 'Registering...', { email, password });
        if (isLogin) {
            const config = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                }),
            }
            const customResponse: CustomFetchResult = await originalRequest('/api/register/Login', config,);
            if (customResponse.status === 200) {
                console.log("Successfully Signed in!");
                console.log(customResponse.data);

                dispatch(setUser({
                    userId: customResponse.data.userId,
                    signedIn: true,
                    firstname: 'test',
                    lastname: 'test',
                    email: email,
                    access_token: customResponse.data.access_token,
                    refresh_token: customResponse.data.refresh_token,
                    role: customResponse.data.role
                }))
                navigate('/home');
            } else {
                console.log('somethign went wrong: ', customResponse.data, customResponse.status, customResponse.statusText);
            }
        }
        else {

            const config = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    firstname: "test",
                    lastname: "testLast",
                    email: email,
                    password: password
                }),
            }
            const customResponse: CustomFetchResult = await originalRequest('/api/register/Signup', config,);
            if (customResponse.status === 200) {
                console.log("Successfully registered");
                console.log(customResponse.data);

                dispatch(setUser({
                    userId: customResponse.data.userId,
                    signedIn: true,
                    firstname: 'test',
                    lastname: 'test',
                    email: email,
                    access_token: customResponse.data.access_token,
                    refresh_token: customResponse.data.refresh_token,
                    role: customResponse.data.role
                }))
            } else {
                console.log('somethign went wrong: ', customResponse.data, customResponse.status, customResponse.statusText);
            }
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.formContainer}>
                <h2 >{isLogin ? 'Login' : 'Register'}</h2>
                <form onSubmit={handleSubmit} style={styles.form}>
                    <div>
                        <label htmlFor="email" style={styles.label}>
                            Email
                        </label>
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            style={{ ...styles.input }}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="password" style={styles.label}>
                            Password
                        </label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            style={{ ...styles.input }}
                            required
                        />
                    </div>
                    {!isLogin && (
                        <div>
                            <label htmlFor="confirmPassword" style={styles.label}>
                                Confirm Password
                            </label>
                            <input
                                type="password"
                                id="confirmPassword"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                style={{ ...styles.input }}
                                required
                            />
                        </div>
                    )}
                    {error && (
                        <span style={styles.error}>{error}</span>
                    )}
                    <button
                        type="submit"
                        style={styles.button}
                        onMouseOver={(e) => {
                            e.currentTarget.style.backgroundColor = styles.buttonHover.backgroundColor;
                        }}
                        onMouseOut={(e) => {
                            e.currentTarget.style.backgroundColor = styles.button.backgroundColor;
                        }}
                    >
                        {isLogin ? 'Login' : 'Register'}
                    </button>
                </form>
                <div
                    onClick={() => setIsLogin(!isLogin)}

                >
                    {isLogin ? 'Need an account? Register' : 'Already have an account? Login'}
                </div>
            </div>
        </div>
    );
};

const styles = {
    container: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: '100vh',
        backgroundColor: '#f7fafc',
    },
    formContainer: {
        backgroundColor: '#ffffff',
        padding: '2rem',
        borderRadius: '0.5rem',
        boxShadow: '0 0.5rem 1rem rgba(0, 0, 0, 0.1)',
        width: '24rem',
    },
    title: {
        fontSize: '1.5rem',
        fontWeight: 'bold',
        marginBottom: '1.5rem',
        textAlign: 'center',
    },
    form: {
        display: 'grid',
        gap: '1rem',
    },
    label: {
        display: 'block',
        fontSize: '0.875rem',
        fontWeight: '500',
        color: '#4a5568',
    },
    input: {
        marginTop: '0.25rem',
        display: 'block',
        width: '100%',
        borderRadius: '0.375rem',
        border: '1px solid #cbd5e0',
        boxShadow: '0 0.125rem 0.25rem rgba(0, 0, 0, 0.075)',
        padding: '0.5rem',
        fontSize: '1rem',
        outline: 'none',
        transition: 'border-color 0.2s, box-shadow 0.2s',
    },
    inputFocus: {
        borderColor: '#5a67d8',
        boxShadow: '0 0.125rem 0.375rem rgba(90, 103, 216, 0.2)',
    },
    error: {
        color: 'red',
    },
    button: {
        width: '100%',
        padding: '0.5rem 1rem',
        borderRadius: '0.375rem',
        border: 'none',
        boxShadow: '0 0.125rem 0.25rem rgba(0, 0, 0, 0.075)',
        backgroundColor: '#5a67d8',
        color: 'white',
        fontWeight: '500',
        fontSize: '0.875rem',
        cursor: 'pointer',
        outline: 'none',
        transition: 'background-color 0.2s, box-shadow 0.2s',
    },
    buttonHover: {
        backgroundColor: '#434190',
    },
    switchText: {
        marginTop: '1rem',
        textAlign: 'center',
        fontSize: '0.875rem',
        color: '#5a67d8',
        cursor: 'pointer',
    },
}
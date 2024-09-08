import React, { useEffect, useState } from 'react';
import { CustomFetchResult, setUser } from '@/Redux';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, } from 'react-router-dom';
import { host } from '../../connectionConfig';
import "./InitialScreen.css";

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
    const user = useSelector((state: any) => state.user)
    const sellerOrders = useSelector((state: any) => state.sellerOrders)


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

        <div className='test-login-main-frame'>
            <div>
                {JSON.stringify(user)}
            </div>
            <div>
                {JSON.stringify(sellerOrders)}
            </div>
            <div className="login-container">
                <div className="login-form-container">
                    <h2 className="login-title">{isLogin ? 'Login' : 'Register'}</h2>
                    <form className="login-form" onSubmit={handleSubmit}>
                        <div>
                            <label htmlFor="email">Email</label>
                            <input
                                type="email"
                                id="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label htmlFor="password">Password</label>
                            <input
                                type="password"
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>

                        {!isLogin && (
                            <>
                                <div >
                                    <label htmlFor="confirmPassword">Confirm Password</label>
                                    <input
                                        type="password"
                                        id="confirmPassword"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}
                                        required
                                    />

                                </div>
                                <div >
                                    <label htmlFor="confirmPassword">Confirm Password</label>
                                    <input
                                        type="password"
                                        id="confirmPassword"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}
                                        required
                                    />

                                </div>
                            </>

                        )

                        }

                        {
                            error &&
                            <span className="error">{error}</span >
                        }
                        <button type="submit">
                            {isLogin ? 'Login' : 'Register'}
                        </button>
                    </form >
                    <div className="login-switch-text" onClick={() => setIsLogin(!isLogin)}>
                        {isLogin ? 'Need an account? Register' : 'Already have an account? Login'}
                    </div>
                </div >

            </div>
        </div>

    );
};


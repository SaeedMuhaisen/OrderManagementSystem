import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { host } from '../Features/Common/connectionConfig';


export interface UserState {
    userId: number | null,
    signedIn: boolean,
    firstname: string,
    lastname: string,
    email: string,
    access_token: string,
    refresh_token: string,
    role: 'ADMIN' | 'BUYER' | 'SELLER' | ''
}

export const refreshTokens = createAsyncThunk(
    'user/refreshToken',
    async (worklet, { getState, dispatch }) => {
        const { user }: any = getState();
        //console.log(`🟣UserSlice - refreshTokens - started - refresh token: ${user.refresh_token}`)
        let endpoint = '/api/register/refresh';
        let config = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: user.refresh_token,
        }
        let customResponse: CustomFetchResult = await dispatch(originalRequest(endpoint, config) as never);
        if (customResponse.status === 200) {
            let newAccessToken: string = customResponse.data.access_token
            let newRefreshToken: string = customResponse.data.refresh_token
            dispatch(updateTokenAndAccessToken({
                access_token: newAccessToken,
                refresh_token: newRefreshToken
            }));
            // console.log(`🟣UserSlice - refreshTokens - received tokens and dispatched, newAccessToken:${newAccessToken.slice(0, 5)}..., newAccessToken:${newRefreshToken.slice(0, 10)}...`)
        }
        //console.log('🟣UserSlice - refreshTokens - exiting')
        return customResponse;
    }
);


export const originalRequest = async (endpoint: any, config: any) => {
    try {
        let customResponse: CustomFetchResult = {
            status: 0,
            statusText: '',
            data: undefined,
        }
        //console.log(`🟣UserSlice - originalRequest starting - endpoint: ${endpoint}, configs: ${JSON.stringify(config, null, 2)}`);
        let response = await fetch(host + endpoint, config);
        customResponse.status = response.status;
        customResponse.statusText = response.statusText;
        const text = await response.text();
        if (!text) {
            //console.log(`🟣UserSlice - originalRequest - response Received and body is empty!`);
            customResponse.data = null
        } else {
            //console.log(`🟣UserSlice - originalRequest - response Received`);
            try {
                customResponse.data = JSON.parse(text);
                //console.log(`🟣UserSlice - originalRequest - response data: ${JSON.stringify(JSON.parse(text), null, 2)}`);
            } catch (error: any) {
                //console.log(`❗❗🟣UserSlice - originalRequest - Error caught while trying to parse response - error: ${error}`);
                customResponse.data = null;
            }
        }
        //console.log(`🟣UserSlice - originalRequest - finished - returning : ${JSON.stringify(customResponse, null, 2)}`);
        return customResponse
    } catch (error) {
        //console.log(`❗❗🟣UserSlice - originalRequest - Error Caucht while processing originalRequest - RETHROWING : ${error}`);
        throw error;
    }
};

export interface CustomFetchResult {
    status: number | null | 0,
    statusText: string | null | '',
    data: any | null,
}

export const fetchWithRefresh = createAsyncThunk(
    'user/fetchWithRefresh',
    async ({ endpoint, config = {} }: { endpoint: string, config: RequestInit }, { getState, dispatch }) => {

        //console.log(`🟣UserSlice - fetchWithRefresh Starting - endpoint ${endpoint}, config ${JSON.stringify(config, null, 2)} `);

        const { user }: any = getState();
        config.headers = {
            ...config.headers,
            Authorization: `Bearer ${user?.access_token}`
        };
        console.log('authorization::',user.access_token);

        try {
            // console.log(`🟣UserSlice - fetchWithRefresh calling originalRequest Method `);
            let customResponse: CustomFetchResult = await originalRequest(endpoint, config);
            //console.log(`🟣UserSlice - fetchWithRefresh returned`);
            if (customResponse.status === 401) {
                //  console.log(`🟣UserSlice - fetchWithRefresh | REFRESH TOKEN- responseStatus: ${customResponse.status} - Must refresh the tokens`);
                let refreshEndpoint = '/api/register/refresh';
                let refreshConfig = {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: user.refresh_token.toString(),
                }
                // console.log(`🟣UserSlice - fetchWithRefresh | REFRESH TOKEN - calling originalRequest Method to refreshtokens - refreshEndpoint ${refreshEndpoint}, refreshConfig ${JSON.stringify(refreshConfig, null, 2)}`);
                let refreshResponse: CustomFetchResult = await originalRequest(refreshEndpoint, refreshConfig);
                // console.log(`🟣UserSlice - fetchWithRefresh - returned from orignalRequest method`);
                if (refreshResponse.status === 200) {
                    //console.log(`🟣UserSlice - fetchWithRefresh | REFRESH TOKEN - status: ${refreshResponse.status}`);
                    dispatch(updateTokenAndAccessToken({
                        access_token: refreshResponse.data.access_token,
                        refresh_token: refreshResponse.data.refresh_token
                    }))
                    //console.log(`🟣UserSlice - fetchWithRefresh | REFRESH TOKEN - updating refresh and access tokens!`);
                    //console.log(`🟣UserSlice - fetchWithRefresh | REFRESH TOKEN - retrying original request that failed`);
                    config.headers = {
                        ...config.headers,
                        Authorization: `Bearer ${refreshResponse.data.access_token}`
                    };
                    let retryFetchingWithNewToken: CustomFetchResult = await originalRequest(endpoint, config);
                    if (retryFetchingWithNewToken.status !== 200) {
                        // console.log(`❗❗🟣UserSlice - fetchWithRefresh | REFRESH TOKEN - new request failed, status is: ${retryFetchingWithNewToken.status}`);
                    }
                    else {
                        //  console.log(`🟣UserSlice - fetchWithRefresh | REFRESH TOKEN - new request finished!, status is: ${retryFetchingWithNewToken.status}`);
                    }
                    //console.log(`🟣UserSlice - fetchWithRefresh | REFRESH TOKEN - finished everythign returning Response to calling method - response: ${JSON.stringify(customResponse, null, 2)}`);
                    return retryFetchingWithNewToken;
                }
                else {
                    //console.log(`❗❗❗❗🟣UserSlice - fetchWithRefresh | REFRESH TOKEN - failed to refresh token - throwing an error`);
                    //console.log("COULD'NT REFRESH TOKEN, MUST SIGN OUT!")
                    dispatch({ type: 'user/forceSignout' });
                }
            }
            else if (customResponse.status !== 200) {
                //console.log(`🟣UserSlice - fetchWithRefresh status is not okay , status: ${customResponse.status}`);
            }
            else {
                //console.log(`🟣UserSlice - fetchWithRefresh status okay , status: ${customResponse.status}`);

            }
            //console.log(`🟣UserSlice - fetchWithRefresh finished returning : ${JSON.stringify(customResponse, null, 2)}`)

            return customResponse;
        } catch (error) {
            //console.log(`❗❗❗❗🟣UserSlice - fetchWithRefresh caught and rethrowing : ${error}`)
            throw error;
        }
    }
);

export const userSlice = createSlice({
    name: "user",
    initialState: {
        userId: null,
        signedIn: false,
        firstname: "",
        lastname: "",
        email: "",
        access_token: "",
        refresh_token: "",
        role: ''
    },
    reducers: {
        updateUserTokens: (state, action) => {
            state.userId = action.payload.userId;
            state.access_token = action.payload.refreshToken;
            state.refresh_token = action.payload.token;
        },
        updateUserDetails: (state, action) => {
            state.firstname = action.payload.firstname;
            state.lastname = action.payload.lastname;
            state.email = action.payload.email;
        },
        setSignedIn: (state, action) => {
            state.signedIn = action.payload;
        },
        setUser: (state, action: PayloadAction<UserState>) => {
            state.userId = action.payload.userId;
            state.access_token = action.payload.access_token;
            state.refresh_token = action.payload.refresh_token;
            state.signedIn = action.payload.signedIn;
            state.firstname = action.payload.firstname;
            state.lastname = action.payload.lastname;
            state.email = action.payload.email;
            state.role = action.payload.role
        },
        updateTokenAndAccessToken: (state, action) => {
            state.refresh_token = action.payload.refresh_token;
            state.access_token = action.payload.access_token;
        }
    }
})

// Update the user id
export const { updateUserDetails, updateUserTokens, setSignedIn, setUser, updateTokenAndAccessToken } = userSlice.actions;
// Update the access token
export default userSlice.reducer;
// Update the refresh token
// Update the signed in status
// Update the first name
// Update the last name
// Update the email

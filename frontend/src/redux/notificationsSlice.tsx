import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { BuyerOrderDTO, StoreOrderDTO, UpdateOrderItemStatusDTO, UpdateStatusNotification } from '@/Types';
import { CustomFetchResult, fetchWithRefresh } from './userSlice';

export interface NotificationsState {

    BUYER: {
        Orders: {
            notificationAvailable: boolean
        },

    },
    SELLER: {
        Orders: {
            notificationAvailable: boolean
        },
    }
    sellerNotificationHistory: StoreOrderDTO[];
    customerNotificationHistory: UpdateStatusNotification[];
}

const initialState: NotificationsState = {
    BUYER: {
        Orders: {
            notificationAvailable: false
        }
    },
    SELLER: {
        Orders: {
            notificationAvailable: false
        }
    },
    sellerNotificationHistory: [],
    customerNotificationHistory: [],
}

export const fetchAllSellerNotifications = createAsyncThunk(
    'sellerOrders/fetchAllSellerOrders',
    async (worklet, { getState, dispatch }) => {
        const config = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/seller/v1/notifications", config: config })).unwrap()
        if (result.status === 200) {
            

            if (result.data !== null && result.data.length > 0 && result.data[0] !== null) {
                let notificationsArray: StoreOrderDTO[] = result.data;
                

                dispatch(initializeSellerNotificationHistory(notificationsArray));

            } else {

            }

        }
        else {

        }
    }
);

export const fetchAllBuyerNotifications = createAsyncThunk(
    'sellerOrders/fetchAllSellerOrders',
    async (worklet, { getState, dispatch }) => {
        const config = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/buyer/v1/notifications", config: config })).unwrap()
        if (result.status === 200) {
          

            if (result.data !== null && result.data.length > 0 && result.data[0] !== null) {
                let notificationsArray: UpdateStatusNotification[] = result.data;
                dispatch(initializeCustomerNotificationHistory(notificationsArray));

            } else {

            }

        }
        else {

        }
    }
);

export const notificationsSlice = createSlice({
    name: "notifications",
    initialState,
    reducers: {
        insertNotification: (state, action: PayloadAction<{ userType: 'BUYER' | 'SELLER', screen: 'Orders' }>) => {
            state[action.payload.userType][action.payload.screen].notificationAvailable = true
        },

        removeNotification: (state, action: PayloadAction<{ userType: 'BUYER' | 'SELLER', screen: 'Orders' }>) => {
            state[action.payload.userType][action.payload.screen].notificationAvailable = false
        },

        initializeSellerNotificationHistory: (state, action: PayloadAction<StoreOrderDTO[]>) => {
            state.sellerNotificationHistory = action.payload
        },

        insertIntoSellerNotificationHistory: (state, action: PayloadAction<StoreOrderDTO>) => {
            state.sellerNotificationHistory.push(action.payload)
        },
        initializeCustomerNotificationHistory: (state, action: PayloadAction<UpdateStatusNotification[]>) => {
            state.customerNotificationHistory = action.payload
        },

        insertIntoCustomerNotificationHistory: (state, action: PayloadAction<UpdateStatusNotification>) => {
            state.customerNotificationHistory.push(action.payload)
        }

    }
})


export const { insertNotification, removeNotification, initializeSellerNotificationHistory, insertIntoSellerNotificationHistory, initializeCustomerNotificationHistory, insertIntoCustomerNotificationHistory } = notificationsSlice.actions;
export default notificationsSlice.reducer;


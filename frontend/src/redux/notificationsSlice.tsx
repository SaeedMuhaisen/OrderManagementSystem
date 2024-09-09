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
                for(let i = 0; i < notificationsArray.length; i++) {
                    dispatch(insertIntoSellerNotificationHistory(notificationsArray[i]));
                }
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
                for(let i = 0; i < notificationsArray.length; i++) {
                    dispatch(insertIntoCustomerNotificationHistory(notificationsArray[i]));
                }
               

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



        insertIntoSellerNotificationHistory: (state, action: PayloadAction<StoreOrderDTO>) => {
            if(state.sellerNotificationHistory.filter((item) => item.orderId === action.payload.orderId ).length > 0) return
            state.sellerNotificationHistory.push(action.payload)
        },


        insertIntoCustomerNotificationHistory: (state, action: PayloadAction<UpdateStatusNotification>) => {
            if(state.customerNotificationHistory.filter((item) => item.orderId === action.payload.orderId && item.productId === action.payload.productId && item.newStatus === action.payload.newStatus).length > 0) return
            state.customerNotificationHistory.push(action.payload)
        }

    }
})


export const { insertNotification, removeNotification, insertIntoSellerNotificationHistory, insertIntoCustomerNotificationHistory } = notificationsSlice.actions;
export default notificationsSlice.reducer;


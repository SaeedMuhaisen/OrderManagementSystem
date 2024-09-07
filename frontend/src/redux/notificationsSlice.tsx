import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { BuyerOrderDTO, UpdateOrderItemStatusDTO, UpdateStatusNotification } from '@/Types';
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
    notificationHistory: any[];
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
    notificationHistory: [],
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
            // alert(JSON.stringify(result));

            if (result.data !== null && result.data.length > 0 && result.data[0] !== null) {
                alert("notification stringified:" + JSON.stringify((JSON.parse(result.data[0].message) as UpdateStatusNotification)));
                // alert("received notification history from backend queue: data:" + JSON.parse(result.data));
                // alert("hm," + JSON.stringify(result.data));
                // alert("damn bro" + JSON.parse(JSON.parse(result.data)[0].message))
                let notificationsArray: any[] = result.data;
                for (var i = 0; i < result.data.length; i++) {
                    alert('okay?')
                    let n :UpdateStatusNotification= JSON.parse(result.data[i].message);
                    notificationsArray.push(n);
                    alert('hmm')
                    console.log(notificationsArray)
                }
                alert('da?')
                alert(notificationsArray)
                dispatch(initializeNotificationList(notificationsArray));
                // for (var notification in notificationsArray) {
                //     let n.message: UpdateOrderItemStatusDTO = JSON.parse(notification.message);
                // }
                // dispatch(initializeNotificationList(result.data));
                // dispatch(setUpSellerOrders(result.data));
                // dispatch(setUpSellerOrderItems([]))
            } else {
                // dispatch(setUpSellerOrders([]));
                // dispatch(setUpSellerOrderItems([]))
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

        initializeNotificationList: (state, action: PayloadAction<UpdateStatusNotification[]>) => {
            state.notificationHistory = action.payload
        },

        insertIntoNotificationHistory: (state, action: PayloadAction<UpdateStatusNotification[]>) => {
            state.notificationHistory.push(action.payload)
        }

    }
})


export const { insertNotification, removeNotification, initializeNotificationList, insertIntoNotificationHistory } = notificationsSlice.actions;
export default notificationsSlice.reducer;


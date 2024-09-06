import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { BuyerOrderDTO } from '@/Types';

export interface NotificationsState {
    BUYER: {
        Orders: {
            notificationAvailable: boolean
        }
    },
    SELLER: {
        Orders: {
            notificationAvailable: boolean
        },
    }

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
    }
}



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

    }
})


export const { insertNotification, removeNotification } = notificationsSlice.actions;
export default notificationsSlice.reducer;


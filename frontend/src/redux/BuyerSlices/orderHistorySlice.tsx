import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { CustomFetchResult, fetchWithRefresh } from '../userSlice';
import { BuyerOrderDTO } from '../../Types';



export const fetchOrderHistory = createAsyncThunk(
    'buyer/orderHistory',
    async (worklet, { getState, dispatch }) => {
        const config = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/buyer/v1/orders", config: config })).unwrap()
        if (result.status === 200) {
            // console.log(JSON.stringify(result));
            dispatch(setUpOrders(result.data as BuyerOrderDTO));
        }
        else {
            // console.log(JSON.stringify(result))
        }
    }
);

export interface OrderHistoryState {
    orders: BuyerOrderDTO[];
    
}
const initialState: OrderHistoryState = {
    orders: [],
    
}
export const orderHistorySlice = createSlice({
    name: "orderHistory",
    initialState,
    reducers: {
        setUpOrders: (state, action: PayloadAction<any>) => {
            state.orders = action.payload
        },

        updateOrderItemStatus(state, action) {
            alert('updating status redux!' + JSON.stringify(action.payload))
            for (var order in state.orders) {
                if (state.orders[order].orderId === action.payload.orderId) {
                    for (var item in state.orders[order].orderItems) {
                        if (state.orders[order].orderItems[item].productId === action.payload.productId) {
                            state.orders[order].orderItems[item].status = action.payload.newStatus
                            console.log('finished!');
                            
                            return;
                        }
                    }
                }
            }

        }
    }
})


export const { setUpOrders, updateOrderItemStatus } = orderHistorySlice.actions;
export default orderHistorySlice.reducer;


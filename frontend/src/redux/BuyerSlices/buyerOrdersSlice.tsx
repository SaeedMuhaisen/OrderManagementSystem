import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { CustomFetchResult, fetchWithRefresh } from '../userSlice';
import { BuyerOrderDTO } from '../../Types';



export const fetchOrders = createAsyncThunk(
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
            dispatch(setUpOrders(result.data as BuyerOrderDTO[]));
        }
        else {
            // console.log(JSON.stringify(result))
        }
    }
);
export const fetchOrderHistory = createAsyncThunk(
    'buyer/orderHistory',
    async (worklet, { getState, dispatch }) => {
        const config = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/buyer/v1/orders/history", config: config })).unwrap()
        if (result.status === 200) {
            // console.log(JSON.stringify(result));
            dispatch(setUpOrderHistory(result.data as BuyerOrderDTO[]));
        }
        else {
            // console.log(JSON.stringify(result))
        }
    }
);
export interface BuyerOrdersState {
    orders: BuyerOrderDTO[];
    orderHistory: BuyerOrderDTO[];
}
const initialState: BuyerOrdersState = {
    orders: [],
    orderHistory: [],

}
export const buyerOrdersSlice = createSlice({
    name: "buyerOrders",
    initialState,
    reducers: {
        setUpOrders: (state, action: PayloadAction<BuyerOrderDTO[]>) => {
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

        },
        setUpOrderHistory(state, action: PayloadAction<BuyerOrderDTO[]>) {
            state.orderHistory = action.payload
        }
    }
})


export const { setUpOrders, updateOrderItemStatus, setUpOrderHistory } = buyerOrdersSlice.actions;
export default buyerOrdersSlice.reducer;


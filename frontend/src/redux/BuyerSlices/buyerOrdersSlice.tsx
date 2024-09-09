import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { CustomFetchResult, fetchWithRefresh } from '../userSlice';
import { BuyerOrderDTO, UpdateOrderItemStatusDTO } from '../../Types';



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
            dispatch(clearOrders());
            for (var order in result.data) {
                dispatch(insertIntoOrders(result.data[order] as BuyerOrderDTO));
            }
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
            dispatch(clearOrdersHistory());
            for (var order in result.data) {

                dispatch(insertIntoOrderHistory(result.data[order] as BuyerOrderDTO));
            }

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

        insertIntoOrders: (state, action: PayloadAction<BuyerOrderDTO>) => {
            if (state.orders.filter((order) => order.orderId === action.payload.orderId).length === 0) {
                state.orders.push(action.payload);
            }
        },
        updateOrderItemStatus(state, action) {

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

        insertIntoOrderHistory(state, action: PayloadAction<BuyerOrderDTO>) {
            if (state.orderHistory.filter((order) => order.orderId === action.payload.orderId).length === 0) {

                state.orderHistory.push(action.payload);
            }
        },
        clearOrders(state) {
            state.orders = [];
        },
        clearOrdersHistory(state) {
            state.orderHistory = [];
        }
    }
})


export const { updateOrderItemStatus, insertIntoOrders, insertIntoOrderHistory, clearOrders, clearOrdersHistory } = buyerOrdersSlice.actions;
export default buyerOrdersSlice.reducer;


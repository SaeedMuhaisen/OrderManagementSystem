import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { OrderItemDTO, OrderItemStatus, SellerOrderDTO, StoreOrderDTO } from '../../Types';
import { CustomFetchResult, fetchWithRefresh } from '../userSlice';


export const fetchAllSellerOrders = createAsyncThunk(
    'sellerOrders/fetchAllSellerOrders',
    async (worklet, { getState, dispatch }) => {
        const config = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/seller/v1/orders", config: config })).unwrap()
        if (result.status === 200) {
            // alert(JSON.stringify(result));
            if (result.data !== null && result.data.length > 0 && result.data[0] !== null) {
                alert("received something from backend:" + JSON.stringify(result.data))
                dispatch(setUpSellerOrders(result.data));
                dispatch(setUpSellerOrderItems([]))
            } else {
                dispatch(setUpSellerOrders([]));
                dispatch(setUpSellerOrderItems([]))
            }

        }
        else {
            // console.log(JSON.stringify(result))
        }
    }
);
export const fetchAllSellerHistoryOrders = createAsyncThunk(
    'sellerOrders/fetchAllSellerHistoryOrders',
    async (worklet, { getState, dispatch }) => {
        const config = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/seller/v1/orders/history", config: config })).unwrap()
        if (result.status === 200) {
            // console.log(JSON.stringify(result));
            dispatch(setUpSellerHistoryOrders(result.data));
        }
        else {
            // console.log(JSON.stringify(result))
        }
    }
);
export const fetchOrderItemsByOrderId = createAsyncThunk(
    'sellerOrders/fetchOrderItemsByOrderId',
    async ({ orderId, }: { orderId: string, }, { getState, dispatch }) => {
        const config = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: `/api/seller/v1/orders/${orderId}`, config: config })).unwrap()
        if (result.status === 200) {
            // console.log(JSON.stringify(result));
            dispatch(setUpSellerOrderItems(result.data));

        }
        else {
            // console.log(JSON.stringify(result))
        }
    }
);
export const fetchHistoryOrderItemsByOrderId = createAsyncThunk(
    'sellerOrders/fetchOrderItemsByOrderId',
    async ({ orderId, }: { orderId: string, }, { getState, dispatch }) => {
        const config = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: `/api/seller/v1/orders/history/${orderId}`, config: config })).unwrap()
        if (result.status === 200) {
            // console.log(JSON.stringify(result));
            dispatch(setUpSellerOrderItems(result.data));
        }
        else {
            // console.log(JSON.stringify(result))
        }
    }
);

export interface SellerOrdersState {
    orders: StoreOrderDTO[];
    orderItems: SellerOrderDTO[];
    historyOrders: StoreOrderDTO[];
    historyOrderItems: SellerOrderDTO[];
}

const initialState: SellerOrdersState = {
    orders: [],
    orderItems: [],
    historyOrders: [],
    historyOrderItems: []
}



export const sellerOrdersSlice = createSlice({
    name: "sellerOrders",
    initialState,
    reducers: {

        setUpSellerOrders: (state, action: PayloadAction<StoreOrderDTO[]>) => {
            state.orders = action.payload
        },
        setUpSellerHistoryOrders: (state, action: PayloadAction<StoreOrderDTO[]>) => {
            state.historyOrders = action.payload
        },
        setUpSellerOrderItems: (state, action: PayloadAction<SellerOrderDTO[]>) => {
            state.orderItems = action.payload;
        },
        setUpSellerHistoryOrderItems: (state, action: PayloadAction<SellerOrderDTO[]>) => {
            state.historyOrderItems = action.payload;
        },
        insertIntoSellerOrders: (state, action: PayloadAction<StoreOrderDTO>) => {
            let newOrder = state.orders.filter(item => item.orderId === action.payload.orderId);
            if (newOrder.length === 0) {
                state.orders.push(action.payload);
            } else {
                let index = state.orders.findIndex(item => item.orderId === action.payload.orderId);
                state.orders[index] = action.payload
            }
        },
        updateSellerOrderItemStatus(state, action: PayloadAction<{ orderItemId: string, status: OrderItemStatus }>) {
            let index = state.orderItems.findIndex(item => item.orderItemId === action.payload.orderItemId);
            state.orderItems[index].status = action.payload.status;
        }

    }
})


export const { setUpSellerOrders, insertIntoSellerOrders, updateSellerOrderItemStatus, setUpSellerOrderItems, setUpSellerHistoryOrders, setUpSellerHistoryOrderItems } = sellerOrdersSlice.actions;
export default sellerOrdersSlice.reducer;


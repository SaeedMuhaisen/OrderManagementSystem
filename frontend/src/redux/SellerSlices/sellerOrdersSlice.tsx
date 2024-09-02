import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { SellerOrderDTO } from '../../Types';
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
            // console.log(JSON.stringify(result));
            dispatch(setUpSellerOrders(result.data));
        }
        else {
            // console.log(JSON.stringify(result))
        }
    }
);


export interface SellerOrdersState {
    orders: SellerOrderDTO[]
}

const initialState: SellerOrdersState = {
    orders: []
}



export const sellerOrdersSlice = createSlice({
    name: "sellerOrders",
    initialState,
    reducers: {

        setUpSellerOrders: (state, action: PayloadAction<SellerOrderDTO[]>) => {
            state.orders = action.payload
        },
        insertIntoSellerOrders: (state, action: PayloadAction<SellerOrderDTO[]>) => {
            state.orders = state.orders.concat(action.payload);
        },
        updateSellerOrderItemStatus(state, action: PayloadAction<{ orderItemId: string, status: string }>) {
            let index = state.orders.findIndex(item => item.orderItemId === action.payload.orderItemId);
            state.orders[index].status = action.payload.status;
        }

    }
})


export const { setUpSellerOrders, insertIntoSellerOrders, updateSellerOrderItemStatus } = sellerOrdersSlice.actions;
export default sellerOrdersSlice.reducer;


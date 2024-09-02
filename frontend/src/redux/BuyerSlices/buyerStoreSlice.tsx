import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { CustomFetchResult, fetchWithRefresh } from '../userSlice';
import { SellerDTO } from '../../Types';

export interface BuyerStoreState {
    stores: [SellerDTO],
}

export const fetchAvailableStores = createAsyncThunk(
    'buyerStores/fetchAvailableStores',
    async (worklet, { getState, dispatch }) => {
        const config = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/buyer/v1/stores", config: config })).unwrap()
        if (result.status === 200) {
            // console.log('Stores has been fetched for buyer');
            dispatch(setUpBuyerStores(result.data));
        }
        else {
            // console.log(JSON.stringify(result))
        }
    }
);

export const buyerStoreSlice = createSlice({
    name: "buyerStore",
    initialState: {
        stores: [],
    },
    reducers: {
        setUpBuyerStores(state: any, action: PayloadAction<[SellerDTO]>) {
            state.stores = action.payload;
        }
    }
})


export const { setUpBuyerStores } = buyerStoreSlice.actions;
export default buyerStoreSlice.reducer;


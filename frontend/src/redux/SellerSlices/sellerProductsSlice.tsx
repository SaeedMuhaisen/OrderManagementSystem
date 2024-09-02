import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { ProductDTO } from '../../Types';
import { CustomFetchResult, fetchWithRefresh } from '../userSlice';


export const fetchAllProductsForSeller = createAsyncThunk(
    'sellerProducts/fetchAllProducts',
    async (worklet, { getState, dispatch }) => {
        const config = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/seller/v1/products", config: config })).unwrap()
        if (result.status === 200) {
            // console.log(JSON.stringify(result));
            dispatch(setUpProducts(result.data));
        }
        else {
            // console.log(JSON.stringify(result))
        }
    }
);


export interface SellerProductsState {
    products: ProductDTO[]

}

const initialState: SellerProductsState = {
    products: [],
}



export const sellerProductsSlice = createSlice({
    name: "sellerProducts",
    initialState,
    reducers: {

        setUpProducts(state, action: PayloadAction<ProductDTO[]>) {
            state.products = action.payload
        },
        insertIntoProducts(state, action: PayloadAction<ProductDTO>) {
            state.products.push(action.payload);
        },
        removeFromProducts(state, action: PayloadAction<string>) {
            state.products = state.products.filter((item) => item.id !== action.payload);
        }
    }
});


export const { setUpProducts, insertIntoProducts, removeFromProducts } = sellerProductsSlice.actions;
export default sellerProductsSlice.reducer;

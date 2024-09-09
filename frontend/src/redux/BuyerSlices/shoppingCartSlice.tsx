import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';

import { CustomFetchResult, fetchWithRefresh } from '../userSlice';
import { StoreProductDTO } from '@/Types';
import { fetchOrders } from './buyerOrdersSlice';


export interface CartItem {
    product: StoreProductDTO,
    quantity: number
}
export interface ShoppingCartState {
    products: Array<CartItem>;
}


export const confirmPurchase = createAsyncThunk(
    'shoppingCart/confirmPurchase',
    async (worklet, { getState, dispatch }) => {
        const { shoppingCart }: any = getState();
        const sc: ShoppingCartState = shoppingCart;
        let orderProducts = [];
        for (var p in sc.products) {
            orderProducts.push({ productId: sc.products[p].product.id, quantity: sc.products[p].quantity, productPrice: 0, status: "" })
        }
        

        const config = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                products: orderProducts
            })
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/buyer/v1/order", config: config })).unwrap()
        if (result.status === 200) {

            dispatch(clearCart());
            dispatch(fetchOrders())
        }
        else {
            alert('something went wrong!');
        }
    }
);

export const shoppingCartSlice = createSlice({
    name: "shoppingCart",
    initialState: {
        products: null,

    },
    reducers: {
        insertIntoShoppingCart(state: ShoppingCartState, action: PayloadAction<StoreProductDTO>) {

            for (var product in state.products) {
                if (state.products[product].product.id === action.payload.id) {
                    state.products[product].quantity += 1;
                    return;
                }
            }
            if (state.products === null) {
                state.products = [{ product: action.payload, quantity: 1 }];
                return;
            }
            else {
                state.products.push({ product: action.payload, quantity: 1 });
                return
            }
        },
        removeFromCart(state, action: PayloadAction<string>) {
            state.products = state.products.filter((item: CartItem) => item.product.id !== action.payload);
        },
        incrementQuantity(state, action: PayloadAction<string>) {
            const item = state.products.find((item: CartItem) => item.product.id === action.payload);
            if (item) {
                item.quantity += 1;
            }
        },

        updateQuantity(state, action: PayloadAction<{ id: string; quantity: number }>) {
            const item = state.products.find(item => item.product.id === action.payload.id);
            if (item) {
                item.quantity = action.payload.quantity;

            }
        },
        clearCart(state) {
            state.products = null
        }
    }
});

export const { insertIntoShoppingCart, removeFromCart, incrementQuantity, updateQuantity, clearCart } = shoppingCartSlice.actions;

export default shoppingCartSlice.reducer;
import { useDispatch, useSelector } from "react-redux"
import { UserState } from "../userSlice"
import { useEffect } from "react"
import { fetchAvailableStores } from "../BuyerSlices/buyerStoreSlice"
import { fetchOrderHistory } from "../BuyerSlices/orderHistorySlice"
import { fetchAllProductsForSeller } from "../SellerSlices/sellerProductsSlice"
import { fetchAllSellerHistoryOrders, fetchAllSellerOrders } from "../SellerSlices/sellerOrdersSlice"


export const ReduxLoader = ({ children }) => {
    const userState: UserState = useSelector((state: any) => state.user)
    const dispatch = useDispatch<any>();
    const fetchAllForBuyer = async () => {
        await dispatch(fetchAvailableStores()).unwrap();
        await dispatch(fetchOrderHistory()).unwrap();
    };
    const fetchAllForSeller = async () => {
        console.log('fetching all for seller!!')
        await dispatch(fetchAllProductsForSeller()).unwrap();
        await dispatch(fetchAllSellerOrders()).unwrap();
        await dispatch(fetchAllSellerHistoryOrders()).unwrap();

    }
    useEffect(() => {
        console.log('setting up redux states!')


        if (userState.signedIn) {
            if (userState.role === "ADMIN") {

            } else if (userState.role === "SELLER") {
                fetchAllForSeller();

            } else if (userState.role === "BUYER") {
                fetchAllForBuyer();
            }
        }
    }, [userState.signedIn])




    return children
}
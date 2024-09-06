import { useEffect } from "react"
import { useDispatch, useSelector } from "react-redux"
import { fetchAvailableStores } from "../BuyerSlices/buyerStoreSlice"
import { fetchOrderHistory, fetchOrders } from "../BuyerSlices/buyerOrdersSlice"
import { fetchAllSellerHistoryOrders, fetchAllSellerOrders } from "../SellerSlices/sellerOrdersSlice"
import { fetchAllProductsForSeller } from "../SellerSlices/sellerProductsSlice"
import { UserState } from "../userSlice"


export const ReduxLoader = ({ children }) => {
    const userState: UserState = useSelector((state: any) => state.user)
    const dispatch = useDispatch<any>();
    const fetchAllForBuyer = async () => {

        await dispatch(fetchAvailableStores())
        await dispatch(fetchOrders())
        await dispatch(fetchOrderHistory())
        
    };
    const fetchAllForSeller = async () => {

        await dispatch(fetchAllProductsForSeller())
        await dispatch(fetchAllSellerOrders())
        await dispatch(fetchAllSellerHistoryOrders())

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
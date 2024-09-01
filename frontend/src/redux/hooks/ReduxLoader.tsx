import { useDispatch, useSelector } from "react-redux"
import { UserState } from "../userSlice"
import { useEffect } from "react"
import { fetchAvailableStores } from "../buyerStoreSlice"

export const ReduxLoader = ({ children }) => {
    const userState: UserState = useSelector((state: any) => state.user)
    const dispatch = useDispatch<any>()
    useEffect(() => {
        console.log('setting up redux states!')
        const fetchAllForBuyer = async () => {
            await dispatch(fetchAvailableStores()).unwrap();
        }

        if (userState.signedIn) {
            if (userState.role === "ADMIN") {

            } else if (userState.role === "SELLER") {

            } else if (userState.role === "BUYER") {
                fetchAllForBuyer();
            }
        }
    }, [userState.signedIn])

    return children
}
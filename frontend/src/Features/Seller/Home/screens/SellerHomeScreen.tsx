import { useSelector } from "react-redux";
import { UserState } from "../../../../redux";
import "./SellerHomeScreen.css"
export const SellerHomeScreen = () => {
    const user: UserState = useSelector((state: any) => state.user);
    return (
        <div className="sellerHomeScreen-container">
            <span>Seller Homsse</span>
            <span>{user?.role}</span>
        </div >
    )
}
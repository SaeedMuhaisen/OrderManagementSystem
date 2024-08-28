import { useSelector } from "react-redux";
import { UserState } from "../../../../redux";

export const SellerHomeScreen = () => {
    const user: UserState = useSelector((state: any) => state.user);
    return (
        <div>
            <span>Seller Homsse</span>
            <span>{user?.role}</span>
        </div >
    )
}
import { useSelector } from "react-redux";
import { UserState } from "../../../../redux";

export const BuyerHomeScreen = () => {
    const user: UserState = useSelector((state: any) => state.user);
    return (
        <div>
            <span>Buyer Home</span>
            <span>{user?.role}</span>
        </div >
    )
}
import { useSelector } from "react-redux";
import { UserState } from "@/Redux";

export const AdminHomeScreen = () => {
    const user: UserState = useSelector((state: any) => state.user);
    return (
        <div>
            <span>Admin Home</span>
            <span>{user?.role}</span>
        </div >
    )
}
import React from "react";
import { useSelector } from "react-redux";
import { UserState } from "../../../redux";

export const HomeScreen = () => {
    const user: UserState = useSelector((state: any) => state.user);
    return (
        <div>
            <span>{user?.role}</span>
        </div >
    )
}

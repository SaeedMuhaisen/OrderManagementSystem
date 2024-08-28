import React from "react";
import { useSelector } from "react-redux";
import { UserState } from "../../../redux";

export const HomeScreen = () => {
    const user: UserState = useSelector((state: any) => state.user);
    return (
        <div>
            <span>{user.email}</span>
            <span>{user.firstname}</span>
            <span>{user.lastname}</span>
            <span>{user.access_token}</span>
            <span>{user.refresh_token}</span>
        </div >
    )
}

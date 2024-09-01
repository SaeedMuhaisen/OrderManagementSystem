import { chdir } from "process"
import { useWebSocket } from "../utils/ClientWebSocket";
import { useEffect } from "react";
import { UserState } from "../../../../redux";
import { useSelector } from "react-redux";

export const Notifications = ({ children }) => {
    const stompClient = useWebSocket();
    const user: UserState = useSelector((state: any) => state.user);
    useEffect(() => {
        console.log('connected!!');
        if (stompClient !== null) {
            stompClient.connect({}, (frame) => {
                stompClient.subscribe(`/topic/notification/${user.userId}`, (message) => {
                    alert(message.body)
                });
            });
        }
    }, [stompClient, user.userId]);

    return children
}
import { useDispatch, useSelector } from "react-redux";
import useWebSocket from "./useWebSocket";
import { useEffect } from "react";
import { StoreOrderDTO, UpdateStatusNotification } from "@/Types";
import { insertIntoSellerOrders, insertNotification, updateOrderItemStatus } from "@/Redux";
import { chdir } from "process";

export const Notifications = ({ children }) => {
    // const { stompClient, connected, ready } = useWebSocket();
    // const user = useSelector((state: any) => state.user);
    // const dispatch = useDispatch();

    // useEffect(() => {
    //     let subscription = null;
    //     alert("---");
    //     if (ready === true) {
    //         alert("Subscribing to notifications");
    //         subscription = stompClient.subscribe(`/topic/notification/${user.userId}`, (message) => {
    //             let obj = JSON.parse(message.body);
    //             if (obj.notificationType === 'BUYER_UPDATE_ORDER_STATUS') {
    //                 let obj: UpdateStatusNotification = JSON.parse(message.body).message;
    //                 dispatch(updateOrderItemStatus(obj));
    //                 dispatch(insertNotification({ userType: 'BUYER', screen: 'Orders' }));
    //             } else if (obj.notificationType === 'SELLER_ORDER') {
    //                 let obj: StoreOrderDTO = JSON.parse(message.body).message;
    //                 dispatch(insertIntoSellerOrders(obj));
    //                 dispatch(insertNotification({ userType: 'SELLER', screen: 'Orders' }));
    //             }
    //         });
    //     }
    //     return () => {
    //         if (subscription) {
    //             subscription.unsubscribe();
    //         }
    //     };
    // }, [ready]);

    // return children;
    return children
};
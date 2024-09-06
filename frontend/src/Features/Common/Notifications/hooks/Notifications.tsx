import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { UserState } from "../../../../redux";
import { updateOrderItemStatus } from "../../../../redux/BuyerSlices/buyerOrdersSlice";
import { UpdateStatusNotification } from "../../../../Types/Notifications";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import { host } from "../../connectionConfig";
import { insertNotification } from "../../../../redux/notificationsSlice";
import { insertIntoSellerOrders } from "../../../../redux/SellerSlices/sellerOrdersSlice";
import { OrderItemDTO, SellerOrderDTO, StoreOrderDTO } from "../../../../Types";

const useWebSocket = () => {
    const [stompClient, setStompClient] = useState(null);
    const [connected, setConnected] = useState(false); 

    useEffect(() => {
        const socket = new SockJS(`${host}/socket`);
        const client = Stomp.over(socket);

        client.connect({}, () => {
            setConnected(true); 
        });

        setStompClient(client);

        return () => {
            if (client !== null) {
                client.disconnect();
            }
        };
    }, []);

    return { stompClient, connected };
};
export const Notifications = ({ children }) => {
    const { stompClient, connected } = useWebSocket();
    const user: UserState = useSelector((state: any) => state.user);
    const dispatch = useDispatch();

    useEffect(() => {
        if (!connected || !user.userId || !user.signedIn) {
            //alert('websocket not going to connect')
            return;
        }
        //alert('subscribing now')
        const subscription = stompClient.subscribe(`/topic/notification/${user.userId}`, (message) => {
            alert('websocket connected')
            let obj = JSON.parse(message.body);
            if (obj.notificationType === 'BUYER_UPDATE_ORDER_STATUS') {
                let obj: UpdateStatusNotification = JSON.parse(message.body).message;
                dispatch(updateOrderItemStatus(obj));
                dispatch(insertNotification({ userType: 'BUYER', screen: 'Orders' }));
            } else if (obj.notificationType === 'SELLER_ORDER') {
                let obj: StoreOrderDTO = JSON.parse(message.body).message;
                dispatch(insertIntoSellerOrders(obj));
                dispatch(insertNotification({ userType: 'SELLER', screen: 'Orders' }));
            }
        });


        return () => {
            if (subscription) {
                alert('unsubscribing now')
                subscription.unsubscribe();
            }
        };
    }, [connected]);

    useEffect(() => {
        if (!user.signedIn && stompClient) {
            alert('disconnecting from websocket')
            stompClient.disconnect();
        }
    }, [user.signedIn, stompClient]);

    return children;
};

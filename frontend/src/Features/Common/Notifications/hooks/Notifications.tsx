import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { UserState } from "../../../../redux";
import { updateOrderItemStatus } from "../../../../redux/BuyerSlices/orderHistorySlice";
import { UpdateStatusNotification } from "../../../../Types/Notifications";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import { host } from "../../connectionConfig";
import { insertNotification } from "../../../../redux/notificationsSlice";
import { insertIntoSellerOrders } from "../../../../redux/SellerSlices/sellerOrdersSlice";
import { OrderItemDTO, SellerOrderDTO, StoreOrderDTO } from "../../../../Types";

const useWebSocket = () => {
    const [stompClient, setStompClient] = useState(null);
    const [connected, setConnected] = useState(false); // Track connection status

    useEffect(() => {
        const socket = new SockJS(`${host}/socket`);
        const client = Stomp.over(socket);

        client.connect({}, () => {
            setConnected(true); // Set connected after the client connects
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
        if (!connected || !user.userId || !user.signedIn) return;

        stompClient.subscribe(`/topic/notification/${user.userId}`, (message) => {
            alert('received something')
            let obj = JSON.parse(message.body);
            if (obj.notificationType === 'BUYER_UPDATE_ORDER_STATUS') {
                let obj: UpdateStatusNotification = JSON.parse(message.body).message;
                dispatch(updateOrderItemStatus(obj));
                dispatch(insertNotification({ userType: 'BUYER', screen: 'Orders' }))
            }
            else if(obj.notificationType==='SELLER_NEW_ORDER'){
                let obj: StoreOrderDTO[] = JSON.parse(message.body).message;
                dispatch(insertIntoSellerOrders(obj))
                dispatch(insertNotification({ userType: 'SELLER', screen: 'Orders' }))
                alert(JSON.parse(message.body).notificationType)
            }
        });
    }, [connected, stompClient, user.userId, user.signedIn]);
    return children
}
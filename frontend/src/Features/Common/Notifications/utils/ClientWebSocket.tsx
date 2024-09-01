import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { host } from "../../connectionConfig";
import { Stomp } from "@stomp/stompjs";

export const useWebSocket = () => {
    const [stompClient, setStompClient] = useState(null);
    useEffect(() => {
        const socket = new SockJS(`${host}/socket`);
        const stompClient = Stomp.over(socket);
        setStompClient(stompClient);
        return () => {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
        };
    }, []);
    return stompClient;
};

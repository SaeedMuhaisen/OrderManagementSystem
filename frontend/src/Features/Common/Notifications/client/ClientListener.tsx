import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

export const WebSocketService = () => {
    // Open connection with the back-end socket
    const connect = () => {
        let socket = new SockJS(`http://localhost:8080/socket`);
        let stompClient = Stomp.over(socket);
        return stompClient;
    };

    return {
        connect
    };
};
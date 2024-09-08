import React, { useEffect, useState, useCallback, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { host } from '../../Common/connectionConfig'
import { insertIntoCustomerNotificationHistory, insertIntoSellerNotificationHistory, insertIntoSellerOrders, insertNotification, NotificationsState, updateOrderItemStatus, UserState } from '@/Redux';
import { useDispatch, useSelector } from 'react-redux';
import { StoreOrderDTO, UpdateOrderItemStatusDTO, UpdateStatusNotification } from '@/Types';

export const SockJSWrapper = ({ children }) => {
    const user: UserState = useSelector((state: any) => state.user);
    const stompClientRef = useRef(null);
    const isConnectedRef = useRef(false);
    const hasConnectedRef = useRef(false); // New ref to track if `useEffect` has executed
    const isUserSignedIn = user.signedIn;
    const dispatch = useDispatch()
    const connect = useCallback(() => {
        if (!isUserSignedIn || stompClientRef.current) return;

        const socket = new SockJS(`${host}/socket`);
        const client = Stomp.over(socket);
        const headers = {
            Authorization: `Bearer ${user.access_token}`,
        };

        client.connect(headers, () => {
            isConnectedRef.current = true;
            stompClientRef.current = client;

            client.subscribe(`/topic/notification/${user.userId}`, (message) => {
                let obj: any = JSON.parse(message.body);



                if (obj.notificationType === 'BUYER_UPDATE_ORDER_STATUS') {
                    let updateStatus: UpdateStatusNotification = JSON.parse(obj.message);

                    dispatch(updateOrderItemStatus(updateStatus));
                    dispatch(insertIntoCustomerNotificationHistory(updateStatus));
                    dispatch(insertNotification({ userType: 'BUYER', screen: 'Orders' }));
                } else if (obj.notificationType === 'SELLER_ORDER') {
                    let storeOrderDTO: StoreOrderDTO = JSON.parse(obj.message);

                    dispatch(insertIntoSellerOrders(storeOrderDTO));
                    dispatch(insertIntoSellerNotificationHistory(storeOrderDTO));
                    dispatch(insertNotification({ userType: 'SELLER', screen: 'Orders' }));
                }
            }, headers);
        });
    }, [isUserSignedIn, user.access_token, user.userId]);

    const disconnect = useCallback(() => {
        if (stompClientRef.current) {
            stompClientRef.current.disconnect();
            stompClientRef.current = null;
            isConnectedRef.current = false;
        }
    }, []);

    useEffect(() => {
        // Prevent multiple connections due to StrictMode
        if (hasConnectedRef.current) {
            return;
        }

        if (isUserSignedIn && !isConnectedRef.current && !stompClientRef.current) {

            connect();
            hasConnectedRef.current = true
        } else if (!isUserSignedIn && isConnectedRef.current) {

            disconnect();
        }

        return () => {
            if (isConnectedRef.current) {

                disconnect();
            }
        };
    }, [isUserSignedIn, connect, disconnect]);
    return (
        children
    );
};


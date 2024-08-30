import { TabCard } from "../../../Common/components/cards/TabCard"
import { GenericTable } from "../../../Common/components/cards/GenericTable"
import "../styles.css"
import { CreateProductButton } from "../components/buttons/CreateProductButton";
import { useEffect, useRef, useState } from "react";
import { CreateProductModal } from "../components/modals/CreateProductModal";
import { WebSocketService } from "../../../Notifications";
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { useDispatch, useSelector } from "react-redux";
import { CustomFetchResult, fetchWithRefresh } from "../../../../redux";


// export const useWebSocket = () => {
//     const [stompClient, setStompClient] = useState(null);

//     useEffect(() => {
//         const socket = new SockJS("http://localhost:8080/socket");
//         const stompClient = Stomp.over(socket);
//         setStompClient(stompClient);

//         return () => {
//             if (stompClient !== null) {
//                 stompClient.disconnect();
//             }
//         };
//     }, []);

//     return stompClient;
// };
export const ManageProducts = () => {
    const [data, setData] = useState(['-', '-', '-', '-', '-', '-', '-', '-'],);


    let token = useSelector((state: any) => state.user.access_token);


    const [connected, setConnected] = useState(false);
    const user = useSelector((state: any) => state.user);
    const stompClientRef = useRef(null);

    const [notifications, setNotifications] = useState(0);
    // const stompClient = useWebSocket();
    const dispatch = useDispatch<any>();
    // useEffect(() => {
    //     if (stompClient !== null) {
    //         stompClient.connect({}, (frame) => {
    //             stompClient.subscribe("/topic/notification", (message) => {
    //                 const body = JSON.parse(message.body);
    //                 setNotifications(body);
    //             });
    //         });
    //     }
    // }, [stompClient]);


    useEffect(() => {
        const fetchAll = async () => {
            const config = {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            }
            const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/seller/v1/products/all", config: config })).unwrap()
            if (result.status === 200) {
                console.log(JSON.stringify(result));
                setData(result.data);
            }
            else {
                console.log(JSON.stringify(result))
            }
        }
        fetchAll();
    }, [])




    const columns = ['ID', 'Name', 'Description', 'Price', 'Available Quantity', 'Amount Sold', 'Amount Returned', 'Visible'];

    const [createProductModalVisible, setCreateProductModalVisible] = useState(false)
    return (
        <TabCard title="Manage Products" rightComp={<CreateProductButton toggleView={setCreateProductModalVisible} />}>

            <GenericTable columns={columns} data={data} />
            {
                createProductModalVisible && <CreateProductModal onClose={() => setCreateProductModalVisible(false)} />}

        </TabCard >
    )

}
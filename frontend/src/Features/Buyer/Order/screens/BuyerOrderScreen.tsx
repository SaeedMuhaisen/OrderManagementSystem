import { useEffect, useState } from "react";
import { GenericTable } from "../../../Common/components/cards/GenericTable";
import { TabCard } from "../../../Common/components/cards/TabCard"
import { CustomFetchResult, fetchWithRefresh } from "../../../../redux";
import { useDispatch } from "react-redux";


export const BuyerOrderScreen = () => {
    const [data, setData] = useState([]);
    const [orderId, setOrderId] = useState(null)
    const [currentStatus, setCurrentStatus] = useState(null)
    const dispatch = useDispatch<any>();

    useEffect(() => {
        const fetchAll = async () => {
            const config = {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            }
            const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/buyer/v1/orders", config: config })).unwrap()
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

    const setUpModal = (index) => {
        setOrderId(data[index].orderId);
        setCurrentStatus(data[index].status);
        setUpdateStatusModalVisible(true)
    }


    const columns = ['ID', 'Name', 'Description', 'Price', 'Available Quantity', 'Amount Sold', 'Amount Returned', 'Visible'];

    const [updateStatusModalVisible, setUpdateStatusModalVisible] = useState(false)
    return (
        <TabCard title="Manage Products" >

            <GenericTable columns={columns} data={data} handleRowClick={(row) => setUpModal(row)} />
            {/* {updateStatusModalVisible && <UpdateStatusModal onClose={() => setUpdateStatusModalVisible(false)} orderId={orderId} currentStatus={currentStatus} />} */}
        </TabCard >
    )
}
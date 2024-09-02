import { useEffect, useState } from "react";
import { GenericTable } from "../../../Common/components/cards/GenericTable";
import { TabCard } from "../../../Common/components/cards/TabCard"
import { CustomFetchResult, fetchWithRefresh } from "../../../../redux";
import { useDispatch, useSelector } from "react-redux";
import { UpdateStatusModal } from "../modals/UpdateStatusModal";
import { SellerOrdersState } from "../../../../redux/SellerSlices/sellerOrdersSlice";

export const SellerOdersScreen = () => {
    const sellerOrders: SellerOrdersState = useSelector((state: any) => state.sellerOrders);
    const [orderItemId, setOrderItemId] = useState(null)
    const [currentStatus, setCurrentStatus] = useState(null)

    const columns = ['ID', 'Name', 'Description', 'Price', 'Available Quantity', 'Amount Sold', 'Amount Returned', 'Visible'];



    const setUpModal = (index: number) => {
        console.log(sellerOrders.orders[index])
        setOrderItemId(sellerOrders.orders[index].orderItemId);
        setCurrentStatus(sellerOrders.orders[index].status);
        setUpdateStatusModalVisible(true)
    }

    const [updateStatusModalVisible, setUpdateStatusModalVisible] = useState(false)
    return (
        <TabCard title="Manage Products" >

            <GenericTable columns={columns} data={sellerOrders.orders} handleRowClick={(row) => setUpModal(row)} />
            {updateStatusModalVisible && <UpdateStatusModal onClose={() => setUpdateStatusModalVisible(false)} orderItemId={orderItemId} currentStatus={currentStatus} />}
        </TabCard >
    )
}
import { useEffect, useState } from "react";
import { GenericTable } from "../../../Common/components/cards/GenericTable";
import { TabCard } from "../../../Common/components/cards/TabCard"
import { CustomFetchResult, fetchWithRefresh } from "../../../../redux";
import { useDispatch, useSelector } from "react-redux";
import { UpdateStatusModal } from "../modals/UpdateStatusModal";
import { fetchOrderItemsByOrderId, SellerOrdersState } from "../../../../redux/SellerSlices/sellerOrdersSlice";
import "./SellerOrderScreen.css"
import { IconSettings } from "../../../Common/components/svg/Icons";
import { statusColor, statusMapping } from "../../../../Types/Mappers";
export const SellerOrderHistoryScreen = () => {
    const sellerOrders: SellerOrdersState = useSelector((state: any) => state.sellerOrders);
    const [orderItemId, setOrderItemId] = useState(null)
    const [currentStatus, setCurrentStatus] = useState(null)

    const setUpModal = (orderItemId, currentStatus) => {
        setOrderItemId(orderItemId);
        setCurrentStatus(currentStatus);
        setUpdateStatusModalVisible(true);
    }

    const [updateStatusModalVisible, setUpdateStatusModalVisible] = useState(false);
    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
    };
    const dispatch = useDispatch<any>()
    return (
        <div className="active-orders-container">
            <div className="active-orders-header">
                <h1>Manage Products</h1>
            </div>
            <div className="active-orders-details">
                <h2>You have {sellerOrders?.historyOrders?.length} active orders</h2>
            </div>
            {sellerOrders?.historyOrders?.length === 0 && <div className="active-orders-details">
                <h4>Click on the button below to create a new product</h4>
            </div>}
            <div className="active-orders-both-tables-container">

                <div className="rwd-active-orders-table-container">
                    {sellerOrders?.historyOrders?.length > 0 &&
                        <table className="rwd-active-orders-table">
                            <tr>
                                <th>Date</th>
                                <th>Id</th>
                                <th>Client Email</th>
                            </tr>
                            {sellerOrders.historyOrders.slice().sort((a, b) => new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime()).map((row, index) => (
                                <tr key={index} onClick={() => dispatch(fetchOrderItemsByOrderId({ orderId: row.orderId }))} >
                                    <td data-th="Date">{formatDate(row.orderDate)}</td>
                                    <td data-th="Id">{row.orderId.slice(0, 6)}</td>
                                    <td data-th="Client Email">{row.customerEmail}</td>
                                </tr>
                            ))}
                        </table>
                    }
                </div>
                <div className="rwd-table-container">
                    {/* {sellerOrders?.orderItems?.length > 0 &&
                        <table className="rwd-table">
                            <tr>
                                <th>Date</th>
                                <th>Id</th>
                                <th>Product Id</th>
                                <th>Client Name</th>
                                <th className="orderItem-status-td">Status</th>
                            </tr>
                            {sellerOrders.orderItems && sellerOrders?.orderItems?.slice().sort((a, b) => new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime()).map((orderItem, index) => (
                                <tr key={index}  >

                                    <td data-th="Date">{formatDate(orderItem.orderDate)}</td>
                                    <td data-th="Id">{orderItem.orderItemId.slice(0, 6)}</td>
                                    <td data-th="Product Id">{orderItem.productId}</td>
                                    <td data-th="Client Name">{orderItem.firstName}</td>
                                    <td data-th="Status" className="orderItem-status-td">
                                        <div className="orderItem-status-container">
                                            {
                                                orderItem.status !== 'DELIVERED'
                                                && orderItem.status !== 'CANCELED_BY_BUYER'
                                                && orderItem.status !== 'CANCELED_BY_SELLER'
                                                && orderItem.status !== 'CANCELED_BY_ADMIN_MANUALLY'
                                                &&

                                                < div >
                                                    <button onClick={() => setUpModal(orderItem.orderItemId, orderItem.status)} className="orderItem-status-settings-btn">
                                                        <IconSettings />
                                                    </button>
                                                </div>}

                                            <div style={{}} className="orderItem-status">
                                                <div
                                                    style={{
                                                        width: '10px',
                                                        height: '10px',
                                                        borderRadius: '100%',
                                                        backgroundColor: statusColor[orderItem.status],
                                                        boxShadow: `1px 1px 1px 1px  lightgray`
                                                    }} />


                                                {statusMapping[orderItem.status]}
                                            </div>

                                        </div>

                                    </td>
                                </tr>
                            ))}
                        </table>
                    } */}
                </div>
            </div>
            {updateStatusModalVisible && <UpdateStatusModal onClose={() => setUpdateStatusModalVisible(false)} orderItemId={orderItemId} currentStatus={currentStatus} />}
        </div >
    )
}
//
// <TabCard title="Manage Products" >

// <GenericTable columns={columns} data={sellerOrders.orders} handleRowClick={(row) => setUpModal(row)} />
//
// </TabCard >
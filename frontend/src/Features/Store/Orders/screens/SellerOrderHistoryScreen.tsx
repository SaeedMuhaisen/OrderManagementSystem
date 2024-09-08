import { fetchHistoryOrderItemsByOrderId, SellerOrdersState } from "@/Redux";
import { statusColor, statusMapping } from "@/Types";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import "./SellerOrderScreen.css";
export const SellerOrderHistoryScreen = () => {
    const sellerOrders: SellerOrdersState = useSelector((state: any) => state.sellerOrders);
    const [orderItemId, setOrderItemId] = useState(null)
    const [currentStatus, setCurrentStatus] = useState(null)

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
                <h2>You have {sellerOrders?.historyOrders !== null &&
                    sellerOrders?.historyOrders.length !== null ? sellerOrders?.historyOrders.length : 0} previous orders</h2>
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
                            {sellerOrders?.historyOrders?.length > 0 && sellerOrders.historyOrders.slice().sort(
                                (a, b) => new Date(b?.orderDate ?? 0).getTime() - new Date(a?.orderDate ?? 0).getTime()).map((row, index) => (
                                    <tr key={index} onClick={() => dispatch(fetchHistoryOrderItemsByOrderId({ orderId: row.orderId }))} >
                                        <td data-th="Date">{formatDate(row.orderDate)}</td>
                                        <td data-th="Id">{row.orderId.slice(0, 6)}</td>
                                        <td data-th="Client Email">{row.customerEmail}</td>
                                    </tr>
                                ))}
                        </table>
                    }
                </div>
                <div className="rwd-table-container">
                    {sellerOrders?.historyOrderItems?.length > 0 &&
                        <table className="rwd-table">
                            <tr>
                                <th>Date</th>
                                <th>Id</th>
                                <th>Product Id</th>
                                <th>Client Name</th>
                                <th className="orderItem-status-td">Status</th>
                            </tr>
                            {sellerOrders.historyOrderItems && sellerOrders?.historyOrderItems?.slice().sort((a, b) => new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime()).map((historyOrderItems, index) => (
                                <tr key={index}  >

                                    <td data-th="Date">{formatDate(historyOrderItems.orderDate)}</td>
                                    <td data-th="Id">{historyOrderItems.orderItemId.slice(0, 6)}</td>
                                    <td data-th="Product Id">{historyOrderItems.productId}</td>
                                    <td data-th="Client Name">{historyOrderItems.firstName}</td>
                                    <td data-th="Status" className="orderItem-status-td">
                                        <div className="orderItem-status-container">


                                            <div style={{}} className="orderItem-status">
                                                <div
                                                    style={{
                                                        width: '10px',
                                                        height: '10px',
                                                        borderRadius: '100%',
                                                        backgroundColor: statusColor[historyOrderItems.status],
                                                        boxShadow: `1px 1px 1px 1px  lightgray`
                                                    }} />


                                                {statusMapping[historyOrderItems.status]}
                                            </div>

                                        </div>

                                    </td>
                                </tr>
                            ))}
                        </table>
                    }
                </div>
            </div>

        </div >
    )
}
//
// <TabCard title="Manage Products" >

// <GenericTable columns={columns} data={sellerOrders.orders} handleRowClick={(row) => setUpModal(row)} />
//
// </TabCard >
import { useState } from "react";


import { useSelector } from "react-redux";
import { BuyerOrdersState } from "@/Redux";
import { statusColor, statusMapping } from "@/Types";
import { IconProduct } from "@/Features/Common/Componenets";


export const OrderHistoryScreen = () => {
    const buyerOrders: BuyerOrdersState = useSelector((state: any) => state.buyerOrders);
    const [visibleOrder, setVisibleOrder] = useState<string | null>(null);

    const toggleOrderDetails = (id: string) => {
        setVisibleOrder((prevState) => (prevState === id ? null : id));
    };

    return (
        <div className="orders-container">
            <div className="orders-header">
                <h1>Preview Order History</h1>
            </div>
            <div className="orders-active-orders">

                <span style={{ padding: '20px', fontSize: '20px', }}>Your Current Active Orders</span>
                {buyerOrders.orders.map((row, index) => (
                    <div className="order-list-container" key={row.orderId}>
                        <div className="order-row-container">
                            <div className="order-row-label">
                                <div>
                                    {new Date(row.orderDate).getDate() + "/" + (new Date(row.orderDate).getMonth() + 1) + "/" + new Date(row.orderDate).getFullYear()}
                                </div>
                                <div>
                                    Active
                                </div>
                                <div className="order-row-label-productids">
                                    {row.orderItems.map((item) => item.productId.slice(0, 5)).join(", ")}
                                </div>
                            </div>
                            <div>
                                <button className="order-row-label-details-button" onClick={() => toggleOrderDetails(row.orderId)}>
                                    Order Details
                                </button>
                            </div>
                        </div>
                        {visibleOrder === row.orderId && (
                            <div className="order-extra-details-container">
                                {row.orderItems !== null && row.orderItems.length > 0 && row.orderItems.map((item, index) => {
                                    return (
                                        <div className="order-extra-details-item" key={index}>
                                            <div className="order-extra-details-status">
                                                <div style={{ width: '10px', height: '10px', borderRadius: '100%', backgroundColor: statusColor[item.status], boxShadow: `1px 1px 1px 1px lightgray` }} />
                                                {statusMapping[item.status]}
                                            </div>
                                            <div className="order-extra-details-productid-and-icon">
                                                <IconProduct />
                                                <div style={{ flex: 1 }}>
                                                    {item.productId.slice(0, 5)}
                                                </div>
                                                <div className="order-extra-details-product-price">
                                                    <span style={{ color: 'lightgray', fontSize: '12px', fontWeight: '500' }}>{item.quantity}x</span> {item.productPrice} $
                                                </div>
                                            </div>
                                        </div>
                                    );
                                })}
                                <div className="order-details-total">
                                    Total: {row.orderItems && row.orderItems.length > 0 && `${row.orderItems.reduce((total, item) => total + item.productPrice * item.quantity, 0)}$`}
                                </div>
                            </div>
                        )}
                    </div>
                ))}
                {buyerOrders.orderHistory.map((row, index) => (
                    <div className="order-list-container" key={row.orderId}>
                        <div className="order-row-container">
                            <div className="order-row-label">
                                <div>
                                    {new Date(row.orderDate).getDate() + "/" + (new Date(row.orderDate).getMonth() + 1) + "/" + new Date(row.orderDate).getFullYear()}
                                </div>
                                <div>
                                    Finished
                                </div>
                                <div className="order-row-label-productids">
                                    {row.orderItems.map((item) => item.productId.slice(0, 5)).join(", ")}
                                </div>
                            </div>
                            <div >
                                <button className="order-row-label-details-button" onClick={() => toggleOrderDetails(row.orderId)}>
                                    Order Details
                                </button>
                            </div>
                        </div>
                        {visibleOrder === row.orderId &&
                            <div className="order-extra-details-container">
                                {row.orderItems !== null && row.orderItems.length > 0 && row.orderItems.map((item, index) => {
                                    return (
                                        <div className="order-extra-details-item" key={index + buyerOrders.orders.length} >
                                            <div className="order-extra-details-status">
                                                <div style={{ width: '10px', height: '10px', borderRadius: '100%', backgroundColor: statusColor[item.status], boxShadow: `1px 1px 1px 1px  lightgray` }} />
                                                {statusMapping[item.status]}
                                            </div>
                                            <div className="order-extra-details-productid-and-icon">
                                                <IconProduct />
                                                <div style={{ flex: 1 }}>
                                                    {item.productId.slice(0, 5)}
                                                </div>
                                                <div className="order-extra-details-product-price">
                                                    <span style={{ color: 'lightgray', fontSize: '12px', fontWeight: '500' }}>{item.quantity}x</span> {item.productPrice} $
                                                </div>
                                            </div>
                                        </div>
                                    )
                                })}
                                <div className="order-details-total">
                                    total {row.orderItems !== null && row.orderItems.length > 0 && `${row.orderItems.map((item) => item.productPrice * item.quantity).reduce((a, b) => a + b, 0)}$`}
                                </div>
                            </div>
                        }
                    </div>
                )
                )
                }

            </div>
        </div >
    );
};


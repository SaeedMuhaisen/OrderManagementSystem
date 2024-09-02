import { useState } from "react";


import { useDispatch, useSelector } from "react-redux";
import { OrderHistoryState } from "../../../../redux/BuyerSlices/orderHistorySlice";
import { IconProduct } from "../../../Common/components/svg/Icons";


export const OrderHistoryScreen = () => {
    const orderHistory: OrderHistoryState = useSelector((state: any) => state.orderHistory)
    const [visibleOrderDetails, setVisibleOrderDetails] = useState({});

    const toggleOrderDetails = (index) => {
        setVisibleOrderDetails((prevState) => ({
            ...prevState,
            [index]: !prevState[index],
        }));
    };

    return (
        <div className="order-screen-container">
            <h1>Preview Order History</h1>
            {orderHistory.orders.map((row, index) => (
                <div className="order-row-container" key={index}>
                    <div className="order-container">
                        <div className="order-label">
                            <div>
                                {new Date(row.orderDate).getDate() + "/" + (new Date(row.orderDate).getMonth() + 1) + "/" + new Date(row.orderDate).getFullYear()}
                            </div>
                            <div>
                                status
                            </div>
                            <div className="order-label-items">
                                {row.orderItems.map((item) => item.productId.slice(0, 5)).join(", ")}
                            </div>
                        </div>

                        <div >
                            <button className="order-details-button" onClick={() => toggleOrderDetails(index)}>
                                Order Details
                            </button>
                        </div>
                    </div>
                    {visibleOrderDetails[index] &&
                        <div className="order-details">
                            {row.orderItems !== null && row.orderItems.length > 0 && row.orderItems.map((item, index) => (
                                <div className="order-details-item" key={index} >
                                    {item.status}
                                    <IconProduct />
                                    <div style={{ flex: 1 }}>
                                        {item.productId.slice(0, 5)}
                                    </div>
                                    <div >
                                        {item.quantity}x {item.productPrice} $
                                    </div>
                                </div>
                            ))}
                            <div className="order-details-total">
                                total {row.orderItems !== null && row.orderItems.length > 0 && row.orderItems.map((item) => item.productPrice * item.quantity).reduce((a, b) => a + b, 0)}
                            </div>
                        </div>
                    }
                </div>
            )
            )
            }
        </div >
    );
};


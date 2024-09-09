import { CustomFetchResult, fetchWithRefresh, updateSellerOrderItemStatus } from '@/Redux';
import { UpdateOrderItemStatusDTO } from '@/Types';
import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';

export const UpdateStatusModal = ({ onClose, orderItemId, currentStatus }) => {
    const [newStatus, setNewStatus] = useState(null);
    const [statusOptions, setStatusOptions] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    const dispatch = useDispatch<any>();
    const handleSubmit = async (e) => {
        e.preventDefault();
        let updateOrderStatus: UpdateOrderItemStatusDTO = {
            orderItemId: orderItemId,
            status: newStatus
        }
        const config = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updateOrderStatus),
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/seller/v1/order/status", config: config })).unwrap()
        if (result.status === 200) {
            onClose();
            dispatch(updateSellerOrderItemStatus({ orderItemId: orderItemId, status: newStatus }));
        }
        else {
            setErrorMessage("something unexpected happened:" + result.status + " ," + result.statusText);

        }
    };
    const handleContentClick = (event) => {
        event.stopPropagation();
    };
    const handleModalClick = (event) => {
        event.stopPropagation();
        onClose();
    };


    useEffect(() => {
        setStatusOptions(filterStatusOptions(currentStatus));
        setNewStatus(filterStatusOptions(currentStatus)[0]);
    }, [])

    const filterStatusOptions = (currentStatus) => {
        if (currentStatus === 'PENDING') {
            return [
                'ACCEPTED',
                'DISPATCHED',
                'DELIVERED',
                'CANCELED_BY_SELLER'
            ];
        } else if (currentStatus === 'ACCEPTED') {
            return [
                'DISPATCHED',
                'DELIVERED',
                'CANCELED_BY_SELLER',
            ]
        }
        else if (currentStatus === 'DISPATCHED') {
            return [
                'DELIVERED',
                'CANCELED_BY_SELLER'
            ]
        }
        else {
            return []
        }
    };

    return (
        <div className="createproduct-modal" onClick={handleModalClick}>
            <div className="createproduct-modal-content" onClick={handleContentClick}>
                <h2>Update Order Status</h2>
                <div>
                    <p>Order ID: <span>{orderItemId}</span></p>
                    <p>Current Status: <span>{currentStatus}</span></p>
                </div>
                {statusOptions !== null && statusOptions.length !== 0 &&
                    <>
                        <label htmlFor="status-select">Update Status:</label>
                        < select
                            id="status-select"
                            value={newStatus}
                            onChange={(e) => setNewStatus(e.target.value)}
                        >
                            {statusOptions.map((option) => (
                                <option key={option} value={option}>
                                    {option}
                                </option>
                            ))}
                        </select>
                        {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
                        <button onClick={handleSubmit} disabled={newStatus === ''}>Update Status</button>

                    </>
                }
            </div>
        </div >
    );
};
import React, { useState } from 'react';

import { useDispatch } from 'react-redux';
import { CustomFetchResult, fetchWithRefresh } from '../../../../../redux';
import { uuidv7 } from 'uuidv7';
import "./PurchaseProductModal.css";
import { CreateOrderDTO } from '../../../../../Types/ProductTypes';
export const PurchaseProductModal = ({ onClose, name, description, price, productId }) => {

    const [orderQuantity, setOrderQuantity] = useState(1);
    const [visible, setVisible] = useState(true);
    const dispatch = useDispatch<any>();

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        let product: CreateOrderDTO = {
            productId: productId,
            quantity: 1,
        }
        const config = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(product),
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/buyer/v1/store/order", config: config })).unwrap()
        if (result.status === 200) { onClose() }
        else {
            alert(result.status);
        }

    };
    const handleContentClick = (event) => {
        event.stopPropagation();
    };
    const handleModalClick = (event) => {
        event.stopPropagation();
        onClose();
    };
    return (
        <div className="purchaseproduct-modal" onClick={handleModalClick}>
            <div className="purchaseproduct-modal-content" onClick={handleContentClick}>
                <h2>Create a New Product</h2>
                <form onSubmit={handleSubmit}>
                    <div className="purchaseproduct-modal-form-group">
                        <label>Product Name</label>
                        <input
                            type="text"
                            value={name}
                            disabled
                        />
                    </div>
                    <div className="purchaseproduct-modal-form-group">
                        <label>Description:</label>
                        <textarea
                            value={description}
                            disabled
                            required
                        ></textarea>
                    </div>
                    <div className="purchaseproduct-modal-form-group">
                        <label>Price:</label>
                        <input
                            type="number"
                            value={price}
                            disabled
                            required
                        />
                    </div>
                    <div className="purchaseproduct-modal-actions" >
                        <button type="submit" onClick={async (e) => await handleSubmit(e)}>Order now</button>
                        <button type="button" onClick={onClose} >Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    );
};
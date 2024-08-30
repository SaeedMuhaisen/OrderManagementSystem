import React, { useState } from 'react';
import "../../styles.css"
import { useDispatch } from 'react-redux';
import { CustomFetchResult, fetchWithRefresh } from '../../../../../redux';
import { uuidv7 } from 'uuidv7';
export const CreateProductModal = ({ onClose }) => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [availableQuantity, setAvailableQuantity] = useState('');
    const [visible, setVisible] = useState(true);
    const dispatch = useDispatch<any>();

    const handleSubmit = async (e) => {
        e.preventDefault();
        let id = uuidv7();

        const config = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: uuidv7(),
                name: name,
                description: description,
                price: price,
                availableQuantity: availableQuantity,
                visible: visible,
            }),
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/seller/v1/create", config: config })).unwrap()
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
        <div className="createproduct-modal" onClick={handleModalClick}>
            <div className="createproduct-modal-content" onClick={handleContentClick}>
                <h2>Create a New Product</h2>
                <form onSubmit={handleSubmit}>
                    <div className="createproduct-modal-form-group">
                        <label>Product Name</label>
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </div>
                    <div className="createproduct-modal-form-group">
                        <label>Description:</label>
                        <textarea
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            required
                        ></textarea>
                    </div>
                    <div className="createproduct-modal-form-group">
                        <label>Price:</label>
                        <input
                            type="number"
                            value={price}
                            onChange={(e) => setPrice(e.target.value)}
                            required
                        />
                    </div>
                    <div className="createproduct-modal-form-group">
                        <label>Available Quantity:</label>
                        <input
                            type="number"
                            value={availableQuantity}
                            onChange={(e) => setAvailableQuantity(e.target.value)}
                            required
                        />
                    </div>
                    <div className="createproduct-modal-form-group">

                        <div className='createproduct-modal-VisibleCheck'>
                            <input
                                type="checkbox"
                                checked={visible}
                                onChange={(e) => setVisible(e.target.checked)}
                            />
                            <label>Make it Visible to public immidiatly</label>

                        </div>
                    </div>
                    <div className="createproduct-modal-actions" >
                        <button type="submit" onClick={async (e) => await handleSubmit(e)}>Save</button>
                        <button type="button" onClick={onClose} >Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    );
};
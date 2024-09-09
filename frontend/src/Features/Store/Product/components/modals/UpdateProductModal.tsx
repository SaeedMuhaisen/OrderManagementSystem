import { CustomFetchResult, fetchWithRefresh, insertIntoProducts } from '@/Redux';
import { CreateProductDTO, ProductDTO } from '@/Types';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { uuidv7 } from 'uuidv7';


export const UpdateProductModal = ({ onClose, p }: { onClose: any, p: ProductDTO }) => {
    const [name, setName] = useState(p.name ?? '');
    const [description, setDescription] = useState(p.description ?? '');
    const [price, setPrice] = useState(p.price.toString() ?? '');
    const [availableQuantity, setAvailableQuantity] = useState(p.availableQuantity.toString() ?? '');
    const [visible, setVisible] = useState(true);
    const dispatch = useDispatch<any>();

    const handleSubmit = async (e) => {
        e.preventDefault();

        let product: CreateProductDTO = {
            id: p.id,
            name: name.toString(),
            description: description,
            price: parseFloat(price),
            availableQuantity: parseFloat(availableQuantity),
            visible: visible,
        }
        const config = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(
                product
            )
            ,
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/seller/v1/product", config: config })).unwrap()
        if (result.status === 200) {
            onClose()
            dispatch(insertIntoProducts({
                id: product.id,
                name: product.name,
                description: product.description,
                price: product.price,
                created_t: Date(),
                availableQuantity: product.availableQuantity,
                amountSold: p.amountSold,
                amountReturned: p.amountReturned,
                visible: product.visible
            }))

        }
        else {

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
                <h2>Update your Product</h2>
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
                            <label>Visible</label>

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
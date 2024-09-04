import "./ManageProducts.css"
import { GenericTable } from "../../../Common/components/cards/GenericTable";
import { TabCard } from "../../../Common/components/cards/TabCard";
import { useEffect, useState } from "react";

import { CreateProductModal } from "../components/modals/CreateProductModal";
import { useDispatch, useSelector } from "react-redux";
import { CustomFetchResult, fetchWithRefresh } from "../../../../redux";
import { SellerProductsState } from "../../../../redux/SellerSlices/sellerProductsSlice";
import { IconReceipt, IconSettings } from "../../../Common/components/svg/Icons";
import { flattenDiagnosticMessageText } from "typescript";
export const ManageProducts = () => {

    const dispatch = useDispatch<any>();
    const sellerProducts: SellerProductsState = useSelector((state: any) => state.sellerProducts);

    const columns = ['ID', 'Name', 'Description', 'Price', 'Available Quantity', 'Amount Sold', 'Amount Returned', 'Visible'];

    const [createProductModalVisible, setCreateProductModalVisible] = useState(false);
    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
    };
    return (
        <div className="manage-products-container">
            <div className="manage-products-header">
                <h1>Manage Products</h1>
            </div>
            <div className="manage-products-details">

                <h2>You have {sellerProducts?.products?.length} products</h2>

                <div className="manage-products-buttons">
                    <button onClick={() => { setCreateProductModalVisible(true) }} >
                        <span>Create Product</span>
                    </button>
                </div>

            </div>
            {sellerProducts?.products?.length === 0 && <div className="manage-products-details">
                <h4>Click on the button below to create a new product</h4>
            </div>}
            <div className="rwd-table-container">
                {sellerProducts?.products?.length > 0 &&
                    <table className="rwd-table">
                        <tr>
                            <th data-th="Adjust" >
                                <div className="status-container">
                                    <IconSettings className="edit-icon" />
                                </div>
                            </th>
                            <th>Date</th>
                            <th>Id</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Price</th>
                            <th>#Quantity</th>
                            <th>#Sales</th>
                            <th>#Returns</th>
                            <th>Status</th>
                        </tr>
                        {sellerProducts.products.slice().sort((a, b) => new Date(b.created_t).getTime() - new Date(a.created_t).getTime()).map((product) => (
                            <tr key={product.id} >

                                <td data-th="Adjust" onClick={() => alert(JSON.stringify(product))}>
                                    <div className="status-container" >
                                        <IconSettings className="edit-icon" />
                                    </div>
                                </td>
                                <td data-th="Date">{formatDate(product.created_t)}</td>
                                <td data-th="Id">{product.id.slice(0, 6)}</td>
                                <td data-th="Name">{product.name}</td>
                                <td data-th="Description" className="description">{product.description}</td>
                                <td data-th="Price">${product.price}</td>
                                <td data-th="#Quantity">{product.availableQuantity}</td>
                                <td data-th="#Sales">{product.amountSold}</td>
                                <td data-th="#Returns">{product.amountReturned}</td>
                                <td data-th="Status">
                                    <div className="status-container">
                                        <div className={`status-indicator ${product.visible ? 'visible' : 'hidden'}`}></div>
                                        <span>{product.visible ? "Visible" : "Hidden"}</span>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </table>
                }
            </div>
            {
                createProductModalVisible && <CreateProductModal onClose={() => setCreateProductModalVisible(false)} />
            }
        </div >

    )

}
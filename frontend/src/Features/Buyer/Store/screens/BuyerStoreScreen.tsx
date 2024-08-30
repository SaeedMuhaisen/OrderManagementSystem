import { useEffect, useState } from "react";
import { TabCard } from "../../../Common/components/cards/TabCard"
import { useDispatch } from "react-redux";
import { CustomFetchResult, fetchWithRefresh } from "../../../../redux";
import "./BuyerStoreScreen.css";
import FlatList from 'flatlist-react';
import { GenericTable } from "../../../Common/components/cards/GenericTable";
import { StoreProduct } from "../../../../Types/ProductTypes";
import { PurchaseProductModal } from "../components/modals/PurchaseProductModal";
export const BuyerStoreScreen = () => {
    const initProduct: StoreProduct = {
        id: "",
        name: "",
        description: "",
        price: 0,
        availableQuantity: 0
    }
    const [product, setProduct] = useState(initProduct);
    const [data, setData] = useState([initProduct]);
    const [purchaseModalVisible, setPurchaseModalVisible] = useState(false);
    const dispatch = useDispatch<any>();
    useEffect(() => {
        const fetchAll = async () => {
            const config = {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            }
            const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/buyer/v1/store/products", config: config })).unwrap()
            if (result.status === 200) {
                console.log(JSON.stringify(result));
                setData(result.data);
            }
            else {
                console.log(JSON.stringify(result))
            }
        }
        fetchAll();
    }, []);

    const columns = ['Name', 'Description', 'Price', 'Available Quantity'];
    const setUpModal = (index) => {
        setProduct(data[index]);
        setPurchaseModalVisible(true)
    }
    return (
        <TabCard title="Store">
            <GenericTable columns={columns} data={data} handleRowClick={(index) => { setUpModal(index) }} />
            {purchaseModalVisible && <PurchaseProductModal onClose={() => setPurchaseModalVisible(false)} name={product.name} description={product.description} price={product.price} productId={product.id} />}
        </TabCard >
    )
}
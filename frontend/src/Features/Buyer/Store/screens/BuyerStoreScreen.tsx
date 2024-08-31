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
    const store = {
        sellerId: "",
        sellerName: "",
    }
    const storeProduct: StoreProduct = {
        id: "",
        name: "",
        description: "",
        price: 0,
        availableQuantity: 0
    }
    const [stores, setStores] = useState([store]);
    const [product, setProduct] = useState(storeProduct);
    const [storeProducts, setStoreProducts] = useState([storeProduct]);

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
            const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/buyer/v1/stores", config: config })).unwrap()
            if (result.status === 200) {
                console.log(JSON.stringify(result));
                setStores(result.data);
            }
            else {
                console.log(JSON.stringify(result))
            }
        }
        fetchAll();
    }, []);

    const columns = ['Name', 'Description', 'Price', 'Available Quantity'];
    const setUpModal = (index) => {
        setProduct(storeProducts[index]);
        setPurchaseModalVisible(true)
    }
    const fetchProducts = async (id: string) => {
        const config = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: id
        }
        const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: "/api/buyer/v1/store/products", config: config })).unwrap()
        if (result.status === 200) {
            console.log(JSON.stringify(result));
            setStoreProducts(result.data);
        }
        else {
            console.log(JSON.stringify(result))
        }
    }
    const handleRowClicks = async (index) => {
        console.log('seller id: ', stores[index].sellerId)
        await fetchProducts(stores[index].sellerId);

    }
    return (
        <TabCard title="Store">
            <GenericTable columns={columns} data={stores} handleRowClick={(index) => { handleRowClicks(index) }} />
            <GenericTable columns={columns} data={storeProducts} handleRowClick={(index) => { setUpModal(index) }} />
            {purchaseModalVisible && <PurchaseProductModal onClose={() => setPurchaseModalVisible(false)} name={product.name} description={product.description} price={product.price} productId={product.id} />}

        </TabCard >
    )
}
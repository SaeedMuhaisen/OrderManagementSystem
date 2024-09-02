import { GenericTable } from "../../../Common/components/cards/GenericTable";
import { TabCard } from "../../../Common/components/cards/TabCard";
import { useEffect, useState } from "react";
import { CreateProductButton } from "../components/buttons/CreateProductButton";
import { CreateProductModal } from "../components/modals/CreateProductModal";
import { useDispatch, useSelector } from "react-redux";
import { CustomFetchResult, fetchWithRefresh } from "../../../../redux";
import { SellerProductsState } from "../../../../redux/SellerSlices/sellerProductsSlice";

export const ManageProducts = () => {

    const dispatch = useDispatch<any>();
    const sellerProducts: SellerProductsState = useSelector((state: any) => state.sellerProducts);

    const columns = ['ID', 'Name', 'Description', 'Price', 'Available Quantity', 'Amount Sold', 'Amount Returned', 'Visible'];

    const [createProductModalVisible, setCreateProductModalVisible] = useState(false)
    return (
        <TabCard title="Manage Products" rightComp={<CreateProductButton toggleView={setCreateProductModalVisible} />}>

            <GenericTable columns={columns} data={sellerProducts.products || []} />
            {
                createProductModalVisible && <CreateProductModal onClose={() => setCreateProductModalVisible(false)} />
            }

        </TabCard >
    )

}
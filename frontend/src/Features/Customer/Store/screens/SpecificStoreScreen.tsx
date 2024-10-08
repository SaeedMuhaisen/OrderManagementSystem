import { IconProduct } from '@/Features/Common/Componenets';
import { CartItem, CustomFetchResult, fetchWithRefresh, insertIntoProducts, insertIntoShoppingCart, removeFromCart, ShoppingCartState, updateQuantity } from '@/Redux';
import { CreateProductDTO, StoreProductDTO } from '@/Types';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useLocation } from 'react-router-dom';

export const SpecificStoreScreen = () => {
    const location = useLocation();
    const { sellerName, sellerId } = location.state || {};
    const storeProduct: CreateProductDTO = {
        id: "",
        name: "",
        description: "",
        price: 0,
        availableQuantity: 0,
        visible: false
    }
    const [products, setProducts] = useState([storeProduct]);
    const shoppingCart: ShoppingCartState = useSelector((state: any) => state.shoppingCart)
    const dispatch = useDispatch<any>();

    useEffect(() => {
        const fetchStoreProducts = async () => {
            const config = {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            }
            const result: CustomFetchResult = await dispatch(fetchWithRefresh({ endpoint: `/api/buyer/v1/store/${sellerId}`, config: config })).unwrap()
            if (result.status === 200) {
                console.log(JSON.stringify(result));
                setProducts(result.data);
                
            }
            else {
                console.log(JSON.stringify(result))
            }
        }
        fetchStoreProducts();
    }, [sellerName, sellerId]);


    const getItemQuantity = (productId: string) => {
        if (!shoppingCart.products || shoppingCart.products === null) {
            return 0;
        }
        const item = shoppingCart.products.find((item: CartItem) => item.product.id === productId);
        return item ? item.quantity : 0;
    };

    const handleQuantityChange = (product: StoreProductDTO, newQuantity: number) => {
        if (newQuantity === 0) {
            dispatch(removeFromCart(product.id));
        } else {
            dispatch(updateQuantity({ id: product.id, quantity: newQuantity }));
        }
    };
    if (products === null) {
        return <></>
    }
    return (

        <div className='store-container'>
            <div>
                <h1>{sellerName}'s Store</h1>
            </div>
            <div className='store-list-container'>
                {products.map((product) => {
                    const quantity = getItemQuantity(product.id);
                    return (
                        <div key={product.id} className="store-item-card">
                            <div className='stores-store-card-icon'>
                                <IconProduct width={'4rem'} height={'4rem'} />
                            </div>
                            <div className='store-products-card-action'>
                                <div className="store-products-card-label">
                                    <div className='store-products-card-title'>{product.name}</div>
                                    <div className='store-products-card-price'>{product.price}$</div>
                                </div>
                                {quantity !== 0 && "Added To Basket"}
                                {quantity === 0 ?
                                    <button
                                        className='store-products-card-button'
                                        onClick={() => dispatch(insertIntoShoppingCart(product))}
                                    >
                                        Add to Basket
                                    </button>
                                    : <div className='quantity-control'>
                                        <button onClick={() => handleQuantityChange(product, quantity - 1)} style={{ flex: 1, backgroundColor: 'lightgray', color: 'black' }}>-</button>
                                        <span style={{ flex: 1 }}>{quantity}</span>
                                        <button onClick={() => handleQuantityChange(product, quantity + 1)} style={{ flex: 1, }}>+</button>
                                    </div>
                                }
                            </div>
                        </div>
                    );
                })}
            </div>
        </div >
    );
}
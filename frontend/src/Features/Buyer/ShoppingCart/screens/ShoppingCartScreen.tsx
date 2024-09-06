import { confirmPurchase, ShoppingCartState } from "@/Redux"
import { useDispatch, useSelector } from "react-redux"

import { IconProduct } from "@/Features/Common/Componenets"
export const ShoppingCartScreen = () => {
    const shopingCart: ShoppingCartState = useSelector((state: any) => state.shoppingCart)
    const dispatch = useDispatch<any>()
    const total = () => {
        return shopingCart.products !== null ? shopingCart.products.map((row) => row.product.price * row.quantity).reduce((a, b) => a + b, 0)
            : 0;
    }

    const hanleConfirmPurchase = async () => {
        await dispatch(confirmPurchase())
    }
    return (
        <div className="cart-container">
            <div className="cart-header">
                <h1 >Checkout</h1 >
            </div>
            {shopingCart?.products?.length > 0 ?
                <div className="cart-product-list-container">
                    Checkout your {shopingCart.products.length} items
                    {shopingCart.products !== null && shopingCart.products.map((row, index) => (
                        <CartItem productName={row.product.name} price={row.product.price} quantity={row.quantity} key={index} />
                    ))
                    }
                    <div className="cart-product-list-item" style={{ border: "none" }}>
                        <div className="cart-product-list-item-name"></div>
                        <div className="cart-product-list-item-priceAndQuantity">Total Price</div>
                        <div className="cart-product-list-item-priceAndQuantity">{total()} $</div>
                    </div>

                    <div className="cart-purchase-button-container">
                        <button className="cart-purchase-button" onClick={hanleConfirmPurchase}>
                            Place Order
                        </button>
                    </div>
                </div>
                :
                <div className="cart-product-empty-container">
                    <span>
                        Your Cart Is Empty!
                    </span>
                    <span>
                        Add Some items to your cart
                    </span>
                </div>
            }
        </div >
    )
}

const CartItem = ({ productName, price, quantity }) => {
    return (
        <div className="cart-product-list-item">
            <IconProduct />
            <div className="cart-product-list-item-name">{productName}</div>
            {quantity > 1 && < div className="cart-product-list-item-priceAndQuantity">{quantity}x</div>}
            <div className="cart-product-list-item-priceAndQuantity">{price} $</div>
        </div >

    )
}
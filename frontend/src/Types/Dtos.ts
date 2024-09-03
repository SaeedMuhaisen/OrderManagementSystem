export interface BuyerOrderDTO {
    orderId: string,
    orderDate: string,
    orderItems: OrderItemDTO[],
}

export type OrderItemStatus =
    | "PENDING"
    | "ACCEPTED"
    | "DISPATCHED"
    | "DELIVERED"
    | "CANCELED_BY_BUYER"
    | "CANCELED_BY_SELLER"
    | "CANCELED_BY_ADMIN_MANUALLY";
export interface OrderItemDTO {
    productId: string,
    quantity: number,
    productPrice: number,
    status: OrderItemStatus

}

export interface CreateOrderDTO {
    products: OrderItemDTO[]
}

export interface CreateProductDTO {
    id: string,
    name: string,
    description: string,
    price: number,
    availableQuantity: number,
    visible: boolean,
}


export interface ProductDTO {
    id: string,
    name: string,
    description: string,
    price: number,
    created_t: string,
    availableQuantity: number,
    amountSold: number,
    amountReturned: number,
    visible: boolean,
}

export interface SellerDTO {
    sellerName: string,
    sellerId: string,
}

export interface SellerOrderDTO {
    orderItemId: string,
    firstName: string,
    productId: string,
    orderDate: string,
    status: string
}


export interface StoreProductDTO {
    id: string,
    name: string,
    description: string,
    price: number,
    availableQuantity: number,

}
export interface UpdateOrderItemStatusDTO {
    orderItemId: string,
    status: string
}
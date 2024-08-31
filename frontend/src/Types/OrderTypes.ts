export interface CreateOrderDTO {
    productId: string,
    quantity: number
}

export interface OrderDTO {

}

export interface UpdateOrderStatusDTO {
    orderId: string,
    status: string
}
export interface CreateProductDTO {
    id: string,
    name: string,
    description: string,
    price: number,
    availableQuantity: number,
    visible: boolean,
}

export interface StoreProduct {
    id: string,
    name: string,
    description: string,
    price: number,
    availableQuantity: number
}


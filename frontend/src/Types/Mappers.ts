import { OrderItemStatus } from "./Dtos";

export const statusMapping: { [key in OrderItemStatus]: string } = {
    PENDING: "Pending",
    ACCEPTED: "Accepted",
    DISPATCHED: "Dispatched",
    DELIVERED: "Delivered",
    CANCELED_BY_BUYER: "canceled",
    CANCELED_BY_SELLER: "canceled",
    CANCELED_BY_ADMIN_MANUALLY: "canceled",
};
export const statusColor: { [key in OrderItemStatus]: string } = {
    PENDING: 'orange',
    ACCEPTED: "forestgreen",
    DISPATCHED: "forestgreen",
    DELIVERED: "green",
    CANCELED_BY_BUYER: "crimson",
    CANCELED_BY_SELLER: "crimson",
    CANCELED_BY_ADMIN_MANUALLY: "crimson",
};
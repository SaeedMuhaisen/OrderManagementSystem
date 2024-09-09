import { NotificationsState } from "@/Redux"
import { StoreOrderDTO, UpdateStatusNotification } from "@/Types"
import { useSelector } from "react-redux"
function timeAgo(date: Date) {
    const now = new Date();
    const seconds = Math.floor((now.getTime() - date.getTime()) / 1000);

    const intervals = {
        year: 31536000, // 365 * 24 * 60 * 60
        month: 2592000, // 30 * 24 * 60 * 60
        week: 604800, // 7 * 24 * 60 * 60
        day: 86400, // 24 * 60 * 60
        hour: 3600, // 60 * 60
        minute: 60,
        second: 1
    };

    for (let key in intervals) {
        const interval = Math.floor(seconds / intervals[key]);
        if (interval >= 1) {
            return interval === 1 ? `1 ${key} ago` : `${interval} ${key}s ago`;
        }
    }

    return 'just now';
}

export const SellerNotificationStack = () => {
    const notifications: NotificationsState = useSelector((state: any) => state.notifications)

    const array: StoreOrderDTO[] = notifications.sellerNotificationHistory ?? []


    return (
        <div>
            <div className="notification-items-list" >
                {
                    array.slice().sort(
                        (a, b) => new Date(b?.orderDate ?? 0).getTime() - new Date(a?.orderDate ?? 0).getTime()).map((not) => {
                            return (
                                <div className="notification-item-container" >
                                    <div className="notification-item-notification-title" >
                                        <span>
                                            new order received!
                                        </span>
                                    </div>
                                    <div className="notification-item-body" >

                                        <span style={{ fontSize: "x-small" }}>
                                            {timeAgo(new Date(not.orderDate))}
                                        </span>
                                    </div>
                                </div>
                            )
                        })
                }
            </div >
        </div>
    )
}

export const BuyerNotificationStack = () => {
    const notifications: NotificationsState = useSelector((state: any) => state.notifications)

    const array: UpdateStatusNotification[] = notifications.customerNotificationHistory ?? []


    return (
        <div className="">

            <div className="notification-items-list" >
                {
                    array.map((not) => {
                        return (
                            <div className="notification-item-container" >
                                <div className="notification-item-notification-title" >
                                    <span>
                                        Order Status Changed
                                    </span>
                                </div>
                                <div className="notification-item-body" >

                                    <span style={{ fontSize: "x-small" }}>
                                        {timeAgo(new Date())}
                                    </span>
                                </div>
                            </div>
                        )
                    })
                }
            </div >
        </div>
    )
}
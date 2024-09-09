import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import "./sidebar.css"; // Import your CSS file
import { SideBarButton } from "../buttons/SideBarButton";
import { UserState } from "@/Redux";
import { IconCart, IconHome, IconLogout, IconNotificationsCircle, IconProduct, IconReceipt, IconStore, IconUser } from "@/Features/Common/Componenets";
import { NotificationsState, removeNotification } from "@/Redux";
import { useNavigate } from "react-router-dom";
import { StoreOrderDTO, UpdateOrderItemStatusDTO, UpdateStatusNotification } from "@/Types";
import { BuyerNotificationStack, SellerNotificationStack } from "@/Features/Notifications";


export const NavigationBar = () => {
    const user: UserState = useSelector((state: any) => state.user);
    const dispatch = useDispatch()
    if (user.role === 'ADMIN') {
        return <AdminSidebar dispatch={dispatch} />;
    } else if (user.role === 'SELLER') {
        return <SellerSideBar dispatch={dispatch} />;
    }
    else {
        return (<BuyerSideBar dispatch={dispatch} />);
    }
};

const AdminSidebar = ({ dispatch }) => {
    const [active, setActive] = useState('Home');
    return (
        <div className="sidebar-container">
            <nav>
                <div className="sidebar-buttonsList">
                    <SideBarButton setActive={setActive} active={active} title={"Home"} />
                    <SideBarButton setActive={setActive} active={active} title={"DashBoard"} />
                    <SideBarButton setActive={setActive} active={active} title={"Products"} />
                    <SideBarButton setActive={setActive} active={active} title={"Orders"} />
                    <SideBarButton setActive={setActive} active={active} title={"Sellers"} />
                    <SideBarButton setActive={setActive} active={active} title={"Buyers"} />
                </div>
                <div >
                    <SideBarButton setActive={setActive} active={active} title={"User"} />
                    <SideBarButton setActive={(value) => { setActive(value); dispatch({ type: "store/reset" }) }} active={active} title={"Logout"} />
                </div>
            </nav>
        </div>
    );
};

const SellerSideBar = ({ dispatch }) => {
    const [active, setActive] = useState('Home');
    const [modalVisible, setModalVisible] = useState(true);
    return (
        <div className="navigationbar-container">
            <div className="navigationbar-header">
                <IconStore />
                <h1 >MarketPlace</h1>
            </div>
            <div className="navigationbar-body">
                <nav>
                    <BuyerSideBarButton setActive={setActive} active={active} title={"Products"} navigateTo="/seller/products" icon={<IconProduct />} />
                    <BuyerSideBarButton setActive={setActive} active={active} title={"Orders"} navigateTo="/seller/orders" icon={<IconReceipt />} />
                    <BuyerSideBarButton setActive={setActive} active={active} title={"History"} navigateTo="/seller/orders/history" icon={<IconHome />} />
                    <BuyerSideBarButton setActive={() => setModalVisible(modalVisible ? false : true)} active={false} title={"Notifications"} icon={<IconNotificationsCircle />} />

                    <div className="navigationbar-notification-container">
                        <SellerNotificationStack />

                    </div>

                    <BuyerSideBarButton setActive={setActive} active={active} title={"User"} icon={<IconUser />} />
                    <BuyerSideBarButton setActive={(value) => { setActive(value); dispatch({ type: "store/reset" }) }} active={active} title={"Logout"} icon={<IconLogout />} />

                </nav>
            </div>
        </div>
    );
};

const BuyerSideBar = ({ dispatch }) => {
    const [active, setActive] = useState('Home');
    const notifications: NotificationsState = useSelector((state: any) => state.notifications);
    const [modalVisible, setModalVisible] = useState(true)
    return (
        <div className="navigationbar-container">
            <div className="navigationbar-header">
                <IconStore />
                <h1 >MarketPlace</h1>
            </div>
            <div className="navigationbar-body">
                <nav>
                    <BuyerSideBarButton
                        setActive={setActive}
                        active={active}
                        title={"Store"}
                        icon={<IconStore />}
                        navigateTo="/store"
                    />
                    <BuyerSideBarButton
                        setActive={setActive}
                        active={active}
                        title={"Orders"}
                        navigateTo="/orders"
                        icon={<IconReceipt />}
                        notificationReset={() => dispatch(removeNotification({ userType: 'BUYER', screen: 'Orders' }))}
                    />
                    <BuyerSideBarButton
                        setActive={setActive}
                        active={active}
                        title={"Cart"}
                        navigateTo="/cart"
                        icon={<IconCart />} />
                    <BuyerSideBarButton setActive={() => setModalVisible(modalVisible ? false : true)} active={false} title={"Notifications"} icon={<svg
                        viewBox="0 0 512 512"
                        fill="currentColor"
                        height="1em"
                        width="1em"
                    >
                        <path d="M256 48C141.31 48 48 141.31 48 256s93.31 208 208 208 208-93.31 208-208S370.69 48 256 48zm0 336c-20.9 0-37.52-8.86-39.75-27.58a4 4 0 014-4.42h71.45a4 4 0 014 4.48C293.15 374.85 276.68 384 256 384zm98-48H158c-11.84 0-18-15-11.19-23 16.33-19.34 27.87-27.47 27.87-80.8 0-48.87 25.74-66.21 47-74.67a11.35 11.35 0 006.33-6.68C231.7 138.6 242.14 128 256 128s24.28 10.6 28 22.86a11.39 11.39 0 006.34 6.68c21.21 8.44 47 25.81 47 74.67 0 53.33 11.53 61.46 27.86 80.8 6.74 7.99.57 22.99-11.2 22.99z" />
                    </svg>} />

                    <div className="navigationbar-notification-container">
                        <BuyerNotificationStack />
                    </div>


                    <BuyerSideBarButton setActive={setActive} active={active} title={"User"} icon={<IconUser />} />
                    <BuyerSideBarButton setActive={(value) => { setActive(value); dispatch({ type: "store/reset" }) }} active={active} title={"Logout"} icon={<IconLogout />} />
                </nav>
            </div>
        </div>
    );
};



const BuyerSideBarButton = ({ title, setActive, active, navigateTo = "", icon = null, notificationReset = () => { } }) => {
    const navigate = useNavigate()
    return (
        <button className={active !== title ? "buyer-sidebar-button" : "buyer-sidebar-button buyer-sidebar-button-active"} onClick={() => {
            setActive(title);
            if (navigateTo !== "")
                navigate(navigateTo);
            notificationReset()
        }}>
            {icon}
            <span>{title}</span>
        </button>
    );
};


import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import "./sidebar.css"; // Import your CSS file
import { SideBarButton } from "../buttons/SideBarButton";
import { UserState } from "../../../../redux";

export const NavigationBar = () => {
    const user: UserState = useSelector((state: any) => state.user);

    if (user.role === 'ADMIN') {
        return <AdminSidebar />;
    } else if (user.role === 'SELLER') {
        return <SellerSideBar />;
    }
    else {
        return (<BuyerSideBar />)
    }
};

const AdminSidebar = () => {
    const [active, setActive] = useState('Home');
    return (
        <div className="container">
            <nav className="sidebar">
                <div className="menu">
                    <SideBarButton setActive={setActive} active={active} title={"Home"} />
                    <SideBarButton setActive={setActive} active={active} title={"DashBoard"} />
                    <SideBarButton setActive={setActive} active={active} title={"Products"} />
                    <SideBarButton setActive={setActive} active={active} title={"Orders"} />
                    <SideBarButton setActive={setActive} active={active} title={"Sellers"} />
                    <SideBarButton setActive={setActive} active={active} title={"Buyers"} />
                </div>
                <div className="user-menu">
                    <SideBarButton setActive={setActive} active={active} title={"User"} />
                </div>
            </nav>
        </div>
    );
};

const SellerSideBar = () => {
    const [active, setActive] = useState('Home');
    return (
        <div className="sidebar-container">
            <nav >
                <div className="sidebar-buttonsList">
                    <SideBarButton setActive={setActive} active={active} title={"Home"} navigateTo="/seller/home" />
                    <SideBarButton setActive={setActive} active={active} title={"Products"} navigateTo="/seller/products" />
                    <SideBarButton setActive={setActive} active={active} title={"Orders"} navigateTo="/seller/orders" />
                    <SideBarButton setActive={setActive} active={active} title={"Customers"} navigateTo="/seller/customers" />
                </div>
                <div >
                    <SideBarButton setActive={setActive} active={active} title={"User"} />
                </div>
            </nav>
        </div>
    );
};

const BuyerSideBar = () => {
    const [active, setActive] = useState('Home');
    return (
        <div className="container">
            <nav>
                <div >
                    <SideBarButton setActive={setActive} active={active} title={"Home"} />
                    <SideBarButton setActive={setActive} active={active} title={"Store"} />
                    <SideBarButton setActive={setActive} active={active} title={"Orders"} />
                    <SideBarButton setActive={setActive} active={active} title={"History"} />
                </div>
                <div >
                    <SideBarButton setActive={setActive} active={active} title={"User"} />
                </div>
            </nav>
        </div>
    );
};




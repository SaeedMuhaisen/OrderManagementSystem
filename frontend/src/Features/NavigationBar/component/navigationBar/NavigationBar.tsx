import React, { useState } from "react";
import { useSelector } from "react-redux";
import "./sidebar.css"; // Import your CSS file
import { SideBarButton } from "../buttons/SideBarButton";

export const NavigationBar = () => {
    //const userRole = useSelector((state: any) => state.user.role);
    let userRole = 'admin'

    if (userRole === 'admin') {
        return <AdminSidebar />;
    } else if (userRole === 'seller') {
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
        <div className="container">
            <nav className="sidebar">
                <div className="menu">
                    <SideBarButton setActive={setActive} active={active} title={"Home"} />
                    <SideBarButton setActive={setActive} active={active} title={"Products"} />
                    <SideBarButton setActive={setActive} active={active} title={"Orders"} />
                    <SideBarButton setActive={setActive} active={active} title={"Customers"} />
                </div>
                <div className="user-menu">
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
            <nav className="sidebar">
                <div className="menu">
                    <SideBarButton setActive={setActive} active={active} title={"Home"} />
                    <SideBarButton setActive={setActive} active={active} title={"Store"} />
                    <SideBarButton setActive={setActive} active={active} title={"Orders"} />
                    <SideBarButton setActive={setActive} active={active} title={"History"} />
                </div>
                <div className="user-menu">
                    <SideBarButton setActive={setActive} active={active} title={"User"} />
                </div>
            </nav>
        </div>
    );
};




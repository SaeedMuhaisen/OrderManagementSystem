import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import "./sidebar.css"; // Import your CSS file
import { SideBarButton } from "../buttons/SideBarButton";
import { UserState } from "../../../../redux";

export const NavigationBar = () => {
    const user: UserState = useSelector((state: any) => state.user);
    const dispatch = useDispatch()
    if (user.role === 'ADMIN') {
        return <AdminSidebar dispatch={dispatch} />;
    } else if (user.role === 'SELLER') {
        return <SellerSideBar dispatch={dispatch} />;
    }
    else {
        return (<BuyerSideBar dispatch={dispatch} />)
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
                    <SideBarButton setActive={(value) => { setActive(value); dispatch({ type: "store/reset" }) }} active={active} title={"Logout"} />

                </div>
            </nav>
        </div>
    );
};

const BuyerSideBar = ({ dispatch }) => {
    const [active, setActive] = useState('Home');
    return (
        <div className="sidebar-container">
            <nav>
                <div className="sidebar-buttonsList">
                    <SideBarButton setActive={setActive} active={active} title={"Home"} navigateTo="/home" />
                    <SideBarButton setActive={setActive} active={active} title={"Store"} navigateTo="/store" />
                     <SideBarButton setActive={setActive} active={active} title={"Orders"} navigateTo="/orders" />
                   {/* <SideBarButton setActive={setActive} active={active} title={"History"} navigateTo="/store" /> */}
                </div>
                <div >
                    <SideBarButton setActive={setActive} active={active} title={"User"} />
                    <SideBarButton setActive={(value) => { setActive(value); dispatch({ type: "store/reset" }) }} active={active} title={"Logout"} />
                </div>
            </nav>
        </div>
    );
};




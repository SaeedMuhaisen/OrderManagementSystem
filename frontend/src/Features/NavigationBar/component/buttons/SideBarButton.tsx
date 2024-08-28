import { Navigate, useNavigate } from "react-router-dom";

export const SideBarButton = ({ title, setActive, active, navigateTo = "" }) => {
    const navigate = useNavigate()
    return (
        <button className={active !== title ? "menu-item" : "menu-item active"} onClick={() => { setActive(title); navigate(navigateTo) }}>
            {title}
        </button>
    );
};
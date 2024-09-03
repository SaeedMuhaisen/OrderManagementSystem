import { Navigate, useNavigate } from "react-router-dom";

export const SideBarButton = ({ title, setActive, active, navigateTo = "", icon = null }) => {
    const navigate = useNavigate()
    return (
        <button className={active !== title ? "sidebar-button" : "sidebar-button sidebar-button-active"} onClick={() => { setActive(title); navigate(navigateTo) }}>
            {icon !== null ? icon : title} {title}
        </button>
    );
};
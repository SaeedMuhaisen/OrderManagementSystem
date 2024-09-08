import { useNavigate } from "react-router-dom";

export const SideBarButton = ({ title, setActive, active, navigateTo = "", icon = null, notificationReset = () => { } }) => {
    const navigate = useNavigate()
    return (
        <button className={active !== title ? "sidebar-button" : "sidebar-button sidebar-button-active"} onClick={() => {
            setActive(title);
            navigate(navigateTo);
            notificationReset()
        }}>
            {icon ? icon : title}
        </button>
    );
};
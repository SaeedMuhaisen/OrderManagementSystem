
export const SideBarButton = ({ title, setActive, active }) => {
    return (
        <button className={active !== title ? "menu-item" : "menu-item active"} onClick={() => setActive(title)}>
            {title}
        </button>
    );
};
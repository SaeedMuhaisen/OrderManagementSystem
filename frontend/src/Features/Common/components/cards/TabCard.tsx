import "../styles.css"

export const TabCard = ({ title = "AYY", children, rightComp = null }) => {
    return (

        <div className="tabCard-container">
            <div className="tabCard-header">
                <h1>{title}</h1>
                <div>
                    {rightComp !== null && rightComp}
                </div>
            </div>
            <div className="tabCard-body">
                {children}
            </div>
        </div>

    )
}
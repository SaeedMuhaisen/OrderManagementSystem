import React from "react";
import "../../styles.css"
export const CreateProductButton = ({ toggleView }) => {

    return (
        <button className="createProductBtn" onClick={() => { toggleView(true) }}>
            Create Button
        </button>

    )
}
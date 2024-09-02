

export const CreateProductButton = ({ toggleView }) => {

    return (
        <button className="createProductBtn" onClick={() => { toggleView(true) }}>
            Create Button
        </button>

    )
}
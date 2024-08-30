import { useEffect } from "react";
import "../styles.css"
import { useDispatch } from "react-redux";
import { setUser } from "../../../../redux";
export const GenericTable = ({ columns, data, handleRowClick = (val) => { } }) => {
    const importData = (products) => {
        if (products.length === 0) {
            // If products array is empty, use the columns prop
            return {
                columns: columns,
                data: []
            };
        } else {
            // Extract column names from the first product object
            const extractedColumns = Object.keys(products[0]);

            // Transform products into rows of data
            const rows = products.map(product =>
                extractedColumns.map(column => product[column])
            );

            return {
                columns: extractedColumns,
                data: rows
            };
        }
    };
    const dispatch = useDispatch()


    const { columns: tableColumns, data: tableData } = importData(data);

    return (

        <div className={"product-table-container"}>
            <table className="product-table">
                <thead className="product-table-header">
                    <tr className="product-table-header-row">
                        {tableColumns.map((column, index) => (
                            <th className="product-table-header-cell" key={index}>{column}</th>
                        ))}
                    </tr>
                </thead>
                <tbody className="product-table-body">
                    {tableData.map((row, index) => (
                        <tr className="product-table-row" key={index} onClick={() => handleRowClick(index)} >
                            {row.map((cell, cellIndex) => (
                                <td className="product-table-cell" key={cellIndex}>{cell}</td>
                            ))}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>

    );
};
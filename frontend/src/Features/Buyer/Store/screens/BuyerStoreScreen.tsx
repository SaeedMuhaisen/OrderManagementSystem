import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { SellerDTO } from "../../../../Types";

export const BuyerStoreScreen = () => {
    const stores: [SellerDTO] = useSelector((state: any) => state.buyerStore.stores)
    const navigate = useNavigate()
    return (
        <div>
            <span>Shop By Store!</span>
            <div className="stores-container">

                {stores.map((row, index) => (
                    <div
                        key={index}
                        className="stores-store-card"
                        onClick={() => navigate(`/store/${row.sellerId}`, {
                            state: { sellerName: row.sellerName, sellerId: row.sellerId }
                        })}
                    >
                        <div className="stores-store-card-icon">
                            <svg
                                viewBox="0 0 640 512"
                                fill="currentColor"
                                height="3em"
                                width="3em"
                            >
                                <path d="M36.8 192h566.4c20.3 0 36.8-16.5 36.8-36.8 0-7.3-2.2-14.4-6.2-20.4L558.2 21.4C549.3 8 534.4 0 518.3 0H121.7c-16 0-31 8-39.9 21.4L6.2 134.7c-4 6.1-6.2 13.2-6.2 20.4C0 175.5 16.5 192 36.8 192zM64 224v240c0 26.5 21.5 48 48 48h224c26.5 0 48-21.5 48-48V224h-64v160H128V224H64zm448 0v256c0 17.7 14.3 32 32 32s32-14.3 32-32V224h-64z" />
                            </svg>
                        </div>
                        <div className="stores-store-card-name">
                            {row.sellerName}
                        </div>
                    </div>
                ))}
            </div>
        </div>






    )
}

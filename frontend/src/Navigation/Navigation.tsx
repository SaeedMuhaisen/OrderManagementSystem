import { InitialScreen } from '@/Features/Authentication';
import { NavigationBar } from 'src/Features/NavigationBar';
import { OrderHistoryScreen } from 'src/Features/Customer/OrderHistory';
import { ShoppingCartScreen } from 'src/Features/Customer/ShoppingCart';
import { BuyerStoreScreen, SpecificStoreScreen } from 'src/Features/Customer/Store';
import { SellerOdersScreen, SellerOrderHistoryScreen } from 'src/Features/Store/Orders';
import { ManageProducts } from "src/Features/Store/Product";
import { UserState } from '@/Redux';
import { useSelector } from 'react-redux';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import "./Navigation.css";

export const Navigation = () => {
  const user: UserState = useSelector((state: any) => state.user);
  if (!user.signedIn) {
    return (
      <BrowserRouter>
        <Routes>

          <Route path="*" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<InitialScreen />} />
        </Routes>
      </BrowserRouter>
    );
  }
  switch (user.role) {
    case 'SELLER':
      return <SellerRoutes />;
    case 'BUYER':
      return <BuyerRoutes />;
  }
};


const BuyerRoutes = () => {
  return (
    <BrowserRouter>
      <div className="buyer-navigation-container">
        <div className='navigation-bar'>
          <NavigationBar />
        </div>
        <Routes>
          <Route path="/orders" element={< OrderHistoryScreen />} />
          <Route path="/store" element={< BuyerStoreScreen />} />
          <Route path="/store/:specificStore" element={<SpecificStoreScreen />} />
          <Route path="/cart" element={<ShoppingCartScreen />} />
          <Route path="*" element={<Navigate to="/store" replace />} />
        </Routes>
      </div>
    </BrowserRouter >
  )
}

const SellerRoutes = () => {
  const user: UserState = useSelector((state: any) => state.user);
  const sellerOrders = useSelector((state: any) => state.sellerOrders)
  const buyerOrders = useSelector((state: any) => state.orderHistory)

  return (

    <BrowserRouter>
      <div className="buyer-navigation-container">
        <div className='navigation-bar'>
          <NavigationBar />
        </div>
        <Routes>
          <Route path="/seller/orders" element={<SellerOdersScreen />} />
          <Route path="/seller/orders/history" element={<SellerOrderHistoryScreen />} />
          <Route path="/seller/products" element={<ManageProducts />} />
          <Route path="*" element={<Navigate to="/seller/products" replace />} />
        </Routes>
      </div >
    </BrowserRouter>

  )

}

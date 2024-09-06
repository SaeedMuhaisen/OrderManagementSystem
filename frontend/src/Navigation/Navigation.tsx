import { useSelector } from 'react-redux';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { AdminHomeScreen } from '../Features/Admin/Home';
import { OrderHistoryScreen } from '../Features/Buyer/OrderHistory';
import { BuyerStoreScreen } from '../Features/Buyer/Store';
import { InitialScreen } from '../Features/Common/Authentication';
import { NavigationBar } from '../Features/Common/NavigationBar';
import { SellerOdersScreen } from '../Features/Seller/Orders';
import { ManageProducts } from '../Features/Seller/Product';
import { UserState } from '../redux';
import "./Navigation.css";
import { SpecificStoreScreen } from '../Features/Buyer/Store/screens/SpecificStoreScreen';
import { ShoppingCartScreen } from '../Features/Buyer/ShoppingCart';
import { Notifications } from '../Features/Common/Notifications';
import { SellerOrderHistoryScreen } from '../Features/Seller/Orders/screens/SellerOrderHistoryScreen';

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
    case 'ADMIN':
      return <AdminRoutes />;
    case 'BUYER':
      return <BuyerRoutes />;
  }
};


const BuyerRoutes = () => {
  const user: UserState = useSelector((state: any) => state.user);
  const sellerOrders = useSelector((state: any) => state.sellerOrders)

  return (
    <Notifications>

      <BrowserRouter>

        <div className="buyer-navigation-container">
          <NavigationBar />

          <Routes>
            <Route path="/orders" element={< OrderHistoryScreen />} />
            <Route path="/store" element={< BuyerStoreScreen />} />
            <Route path="/store/:specificStore" element={<SpecificStoreScreen />} />
            <Route path="/cart" element={<ShoppingCartScreen />} />
            <Route path="*" element={<Navigate to="/home" replace />} />
          </Routes>
        </div>
      </BrowserRouter >
    </Notifications>
  )
}


const SellerRoutes = () => {
  const user: UserState = useSelector((state: any) => state.user);
  const sellerOrders = useSelector((state: any) => state.sellerOrders)
  const buyerOrders = useSelector((state: any) => state.orderHistory)
  return (
    <Notifications>
      <BrowserRouter>
        
        <div className="buyer-navigation-container">

          <NavigationBar />
          <Routes>
            <Route path="/seller/orders" element={<SellerOdersScreen />} />
            <Route path="/seller/orders/history" element={<SellerOrderHistoryScreen />} />
            <Route path="/seller/products" element={<ManageProducts />} />
            <Route path="*" element={<Navigate to="/seller/products" replace />} />
          </Routes>

        </div >
      </BrowserRouter>
    </Notifications>
  )

}

const AdminRoutes = () => (
  // <Notifications>
  <BrowserRouter>
    <div className="navigation-container">
      <NavigationBar />

      <div className='navigation-child'>
        <Routes>
          <Route path="/admin/home" element={<AdminHomeScreen />} />
          <Route path="*" element={<Navigate to="/admin/home" replace />} />
        </Routes>
      </div>
    </div>
  </BrowserRouter>
  // </Notifications>
);
import { InitialScreen } from '@/Authentication';
import { NavigationBar } from '@/Features/Common/NavigationBar';
import { OrderHistoryScreen } from '@/Features/Customer/OrderHistory';
import { ShoppingCartScreen } from '@/Features/Customer/ShoppingCart';
import { BuyerStoreScreen, SpecificStoreScreen } from '@/Features/Customer/Store';
import { SellerOdersScreen, SellerOrderHistoryScreen } from '@/Features/Seller/Orders';
import { ManageProducts } from "@/Features/Seller/Product";
import { UserState } from '@/Redux';
import { useSelector } from 'react-redux';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { AdminHomeScreen } from '../Features/Admin/Home';
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
          <Route path="*" element={<Navigate to="/home" replace />} />
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

const AdminRoutes = () => (
  // <Notifications>
  <BrowserRouter>
    <div className="navigation-container">
      <div className='navigation-bar'>
        <NavigationBar />
      </div>

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
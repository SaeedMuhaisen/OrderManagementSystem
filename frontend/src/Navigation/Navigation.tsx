import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { InitialScreen } from '../Features/Authentication/index';
import { HomeScreen } from '../Features/Home/index';
import { NavigationBar } from '../Features/NavigationBar';
import { SellerHomeScreen } from '../Features/Seller/Home';
import { AdminHomeScreen } from '../Features/Admin/Home';
import { UserState } from '../redux';
import { BuyerHomeScreen } from '../Features/Buyer/Home';
import { ManageProducts } from '../Features/Seller/Product';
import "./Navigation.css"

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

const AppRoutes = ({ user }: { user: UserState }) => {
  if (!user.signedIn) {
    return (
      <Routes>
        <Route path="*" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<InitialScreen />} />
      </Routes>
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

const BuyerRoutes = () => (
  <BrowserRouter>
    <div className="navigation-container">
      <NavigationBar />
    </div>
    <div className='navigation-child'>
      <Routes>
        <Route path="/home" element={<HomeScreen />} />
        <Route path="*" element={<Navigate to="/home" replace />} />
      </Routes>
    </div>

  </BrowserRouter >
);

const SellerRoutes = () => (
  <BrowserRouter>
    <div className="navigation-container">
      <NavigationBar />
      <div className='navigation-child'>
        <Routes>
          <Route path="/seller/products" element={<ManageProducts />} />
          <Route path="/seller/home" element={<SellerHomeScreen />} />
          <Route path="*" element={<Navigate to="/seller/home" replace />} />
        </Routes>
      </div>
    </div >
  </BrowserRouter>
);

const AdminRoutes = () => (
  <BrowserRouter>
    <div className="navigation-container">
      <NavigationBar />
    </div>
    <div className='navigation-child'>
      <Routes>
        <Route path="/admin/home" element={<AdminHomeScreen />} />
        <Route path="*" element={<Navigate to="/admin/home" replace />} />
      </Routes>
    </div>
  </BrowserRouter>
);
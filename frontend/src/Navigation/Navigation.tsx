import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { InitialScreen } from '../Features/Authentication/index';
import { HomeScreen } from '../Features/Home/index';
import { NavigationBar } from '../Features/NavigationBar';
import { SellerHomeScreen } from '../Features/Seller/Home';
import { AdminHomeScreen } from '../Features/Admin/Home';
import { UserState } from '../redux';
import { BuyerHomeScreen } from '../Features/Buyer/Home';

export const Navigation = () => {
  const user: UserState = useSelector((state: any) => state.user);

  return (
    <BrowserRouter>
      <div style={{
        display: 'flex',
        flex: 1,
        height: '100vh',
        width: '100vw',
        flexDirection: 'row',
        backgroundColor: 'white',
        overflow: 'hidden',
      }}>
        {user.signedIn && <NavigationBar />}
        <div style={{ flex: 1, overflow: 'hidden' }}>
          <AppRoutes user={user} />
        </div>
      </div>
    </BrowserRouter>
  );
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
  <Routes>

    <Route path="/home" element={<HomeScreen />} />
    <Route path="*" element={<Navigate to="/home" replace />} />
  </Routes>
);

const SellerRoutes = () => (
  <Routes>
    <Route path="/seller/home" element={<SellerHomeScreen />} />
    <Route path="*" element={<Navigate to="/seller/home" replace />} />
  </Routes>
);

const AdminRoutes = () => (
  <Routes>
    <Route path="/admin/home" element={<AdminHomeScreen />} />
    <Route path="*" element={<Navigate to="/admin/home" replace />} />
  </Routes>
);
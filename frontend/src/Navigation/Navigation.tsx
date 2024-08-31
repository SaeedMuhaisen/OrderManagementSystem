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
import { BuyerStoreScreen } from '../Features/Buyer/Store';
import { SellerOdersScreen } from '../Features/Seller/Orders';
import { BuyerOrderScreen } from '../Features/Buyer/Order';
import { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { host } from '../Features/Common/connectionConfig';
import { Stomp } from '@stomp/stompjs';

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
  const stompClient = useWebSocket();
  useEffect(() => {
    if (stompClient !== null) {
      stompClient.connect({}, (frame) => {
        stompClient.subscribe(`/topic/notification/${user.userId}`, (message) => {
          // const body = JSON.parse(message.body);
          //setNotifications(body);
          alert(message.body)
        });
      });
    }
  }, [stompClient]);
  return (

    <BrowserRouter>
      <div className="navigation-container">
        <NavigationBar />
        <div className='navigation-child'>
          <Routes>
            <Route path="/orders" element={< BuyerOrderScreen />} />
            <Route path="/store" element={< BuyerStoreScreen />} />
            <Route path="/home" element={<HomeScreen />} />
            <Route path="*" element={<Navigate to="/home" replace />} />
          </Routes>
        </div>
      </div>
    </BrowserRouter >
  )
}

export const useWebSocket = () => {
  const [stompClient, setStompClient] = useState(null);
  useEffect(() => {
    const socket = new SockJS(`${host}/socket`);
    const stompClient = Stomp.over(socket);
    setStompClient(stompClient);
    return () => {
      if (stompClient !== null) {
        stompClient.disconnect();
      }
    };
  }, []);
  return stompClient;
};


const SellerRoutes = () => {
  const user: UserState = useSelector((state: any) => state.user);
  const stompClient = useWebSocket();
  useEffect(() => {
    if (stompClient !== null) {
      stompClient.connect({}, (frame) => {
        stompClient.subscribe(`/topic/notification/${user.userId}`, (message) => {
          // const body = JSON.parse(message.body);
          //setNotifications(body);
          alert(message.body)
        });
      });
    }
  }, [stompClient]);
  return (

    <BrowserRouter>
      <div className="navigation-container">
        <NavigationBar />
        <div className='navigation-child'>
          <Routes>
            <Route path="/seller/orders" element={<SellerOdersScreen />} />
            <Route path="/seller/products" element={<ManageProducts />} />
            <Route path="/seller/home" element={<SellerHomeScreen />} />
            <Route path="*" element={<Navigate to="/seller/home" replace />} />
          </Routes>
        </div>
      </div >
    </BrowserRouter>
  )

}

const AdminRoutes = () => (
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
);
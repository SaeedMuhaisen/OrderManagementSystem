import { BrowserRouter, Routes, Route, useNavigate, Navigate } from 'react-router-dom';
import { InitialScreen } from '../Features/Authentication/index';
import { HomeScreen } from '../Features/Home/index';
import { useSelector } from 'react-redux';
import { UserState } from '../redux';
import { NavigationBar } from '../Features/NavigationBar';
// replace with your boolean value

export const Navigation = () => {
  const user: UserState = useSelector((state: any) => state.user);

  return (
    <BrowserRouter>
      <div style={{
        display: 'flex',
        flex: 1,
        height: '100vh', // Set height to full screen
        width: '100vw', // Set width to full screen
        flexDirection: 'row',
        backgroundColor: 'white',
        overflow: 'hidden', // Remove scrolling
      }}>
        {user.signedIn && < NavigationBar />}
        <div style={{
          flex: 1, // Make content take up remaining space
          overflow: 'hidden', // Remove scrolling
        }}>
          <Routes>

            <Route path="/" element={user.signedIn ? <HomeScreen /> : <InitialScreen />} />
            <Route path="/home" element={<HomeScreen />} />
            <Route path="/login" element={<InitialScreen />} />
          </Routes>
        </div>
      </div>
    </BrowserRouter>
  );
};

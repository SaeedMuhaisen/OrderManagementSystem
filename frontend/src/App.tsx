import { useEffect, useState } from 'react';
import { Navigation } from './Navigation/Navigation';
import { store, persistor } from './redux/store';
import { Provider } from "react-redux";
import { PersistGate } from 'redux-persist/integration/react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { host } from './Features/Common/connectionConfig';

function App() {

  return (
    <Provider store={store}>
      <PersistGate persistor={persistor}>
        <Navigation />
      </PersistGate>
    </Provider>
  );
}

export default App;


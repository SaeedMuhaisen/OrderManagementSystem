import { Navigation } from '@/Navigation';
import { persistor, ReduxLoader, store } from '@/Redux';
import { Provider } from "react-redux";
import { PersistGate } from 'redux-persist/integration/react';
import SockJSWrapper from './Features/Common/Notifications/hooks/SockJSWrapper';

function App() {

  return (
    <Provider store={store}>
      <PersistGate persistor={persistor}>
        <ReduxLoader>
          <SockJSWrapper>
            <Navigation />
          </SockJSWrapper>
        </ReduxLoader>
      </PersistGate>
    </Provider >

  );
}

export default App;


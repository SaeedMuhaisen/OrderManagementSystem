import { Provider } from "react-redux";
import { PersistGate } from 'redux-persist/integration/react';
import { Notifications } from './Features/Common/Notifications';
import { Navigation } from './Navigation';
import { ReduxLoader } from './redux/hooks/ReduxLoader';
import { persistor, store } from './redux/store';

function App() {

  return (
    <Provider store={store}>
      <PersistGate persistor={persistor}>
        <ReduxLoader>
          <Notifications>
            <Navigation />
          </Notifications>
        </ReduxLoader>
      </PersistGate>
    </Provider >
  );
}

export default App;


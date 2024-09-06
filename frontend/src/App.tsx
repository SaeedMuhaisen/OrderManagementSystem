import { Navigation } from '@/Navigation';
import { persistor, ReduxLoader, store } from '@/Redux';
import { Provider } from "react-redux";
import { PersistGate } from 'redux-persist/integration/react';

function App() {

  return (
    <Provider store={store}>
      <PersistGate persistor={persistor}>

        <ReduxLoader>

          <Navigation />
        </ReduxLoader>
      </PersistGate>
    </Provider >
  );
}

export default App;


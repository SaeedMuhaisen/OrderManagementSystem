// App.tsx
import './App.css';
import { Navigation } from './Navigation/Navigation';
import { store, persistor } from './redux/store';
import { Provider } from "react-redux";
import { PersistGate } from 'redux-persist/integration/react';
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


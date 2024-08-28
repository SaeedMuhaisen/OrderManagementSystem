import { configureStore, combineReducers } from '@reduxjs/toolkit';
import { persistReducer, persistStore } from 'redux-persist';
import storage from 'redux-persist/lib/storage'; // or whatever storage you want to use
import userReducer from "./userSlice";
import { authMessageMiddleware } from './middleware/authMessageMiddleware';

const rootReducer = combineReducers({
  user: userReducer,
});

const persistConfig = {
  key: 'root',
  storage,
};

const resettableRootReducer = (state, action) => {
  if (action.type === 'store/reset') { return rootReducer(undefined, action); }
  return rootReducer(state, action);
};
const persistedReducer = persistReducer(persistConfig, resettableRootReducer);

const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(authMessageMiddleware),
});

const persistor = persistStore(store);

export { store, persistor };
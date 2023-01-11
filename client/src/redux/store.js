import { configureStore } from '@reduxjs/toolkit';
import userReducer from '../redux/slice/userSlice';

const store = configureStore({
  reducer: {
    user: userReducer,
  },
});

export default store;

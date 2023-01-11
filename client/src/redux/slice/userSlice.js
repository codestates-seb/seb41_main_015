// 로컬스토리지에 저장한 토큰을 이용해 로그인, 로그아웃 관리하는 슬라이스
import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  loginStatus: false,
};

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    login: (state, payload) => {
      let { accessToken, refreshToken } = payload;
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
      state.loginStatus = true;
    },
  },
});

export const { login } = userSlice.actions;
export default userSlice.reducer;

import { createSlice } from '@reduxjs/toolkit';

const sessionAccessToken = sessionStorage.getItem('accessToken');
const sessionloginStatus = sessionStorage.getItem('loginStatus');

const initialState = {
  loginStatus: !sessionloginStatus,
  accessToken: sessionAccessToken,
  refreshToken: '',
  membership: null,
};

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    login: (state, { payload }) => {
      let accessToken = payload.accessToken;
      let refreshToken = payload.refreshToken;
      let membership = payload.membership;
      sessionStorage.setItem('accessToken', accessToken);
      sessionStorage.setItem('loginStatus', true);
      state.refreshToken = refreshToken;
      state.loginStatus = true;
      state.membership = membership;
    },
    logout: () => {
      sessionStorage.clear();
      return initialState;
    },
    // 닉네임을 입력한 유저의 membership 상태를 existing으로 바꾸는 리듀서
    // 메인에 다시 접속했을 때 닉네임 설정 모달이 다시 뜨지 않도록 하기 위함
    setExisting: (state) => {
      state.membership = 'existing';
    },
  },
});

export const { login, logout } = userSlice.actions;
export default userSlice.reducer;

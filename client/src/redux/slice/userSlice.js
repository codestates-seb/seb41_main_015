import { createSlice } from '@reduxjs/toolkit';

const sessionAccessToken = sessionStorage.getItem('accessToken');

const initialState = {
  accessToken: sessionAccessToken,
  refreshToken: null,
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
      sessionStorage.setItem('membership', membership);
      state.accessToken = accessToken;
      state.refreshToken = refreshToken;
      state.membership = membership;
    },
    logout: (state) => {
      console.log('세션 스토리지 삭제!');
      sessionStorage.clear();
      state.accessToken = null;
      state.refreshToken = null;
      state.membership = null;
    },
    // 닉네임을 입력한 유저의 membership 상태를 existing으로 바꾸는 리듀서
    // 메인에 다시 접속했을 때 닉네임 설정 모달이 다시 뜨지 않도록 하기 위함
    setExisting: (state) => {
      sessionStorage.setItem('membership', 'existing');
      state.membership = 'existing';
    },
  },
});

export const { login, logout } = userSlice.actions;
export default userSlice.reducer;

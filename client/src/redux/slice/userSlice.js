import { createSlice } from '@reduxjs/toolkit';
import { getCookie, setCookie, removeCookie } from '../../util/cookie/cookie';
import instanceAxios from '../../reissue/InstanceAxios';

const sessionAccessToken = sessionStorage.getItem('accessToken');
const sessionStorageRefreshToken = sessionStorage.getItem('refreshToken');
// const cookieRefreshToken = getCookie('refreshToken');
const initialState = {
  accessToken: sessionAccessToken,
  refreshToken: sessionStorageRefreshToken,
  membership: null,
};

// 로그인 시 닉네임 정보 가져오기
const getDisplayName = async () => {
  try {
    const res = await instanceAxios.get('v1/members');
    const displayName = res.data.data.displayName;
    return displayName;
  } catch (err) {
    console.error(err);
  }
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
      if (membership === 'existing') {
        // 세션 스토리지에 닉네임 저장
        getDisplayName().then((res) => {
          sessionStorage.setItem('displayName', res);
        });
      }
      sessionStorage.setItem('membership', membership);
      sessionStorage.setItem('refreshToken', refreshToken);
      // setCookie('refreshToken', refreshToken, {
      //   path: '/',
      //   secure: true,
      //   sameSite: 'none',
      // });
      state.accessToken = accessToken;
      state.refreshToken = refreshToken;
      state.membership = membership;
    },
    logout: (state) => {
      console.log('세션 스토리지 삭제!');
      sessionStorage.clear();
      // removeCookie('refreshToken');
      state.accessToken = null;
      state.refreshToken = null;
      state.membership = null;
    },
    // 닉네임을 입력한 신규회원의 membership 상태를 existing으로 바꾸는 리듀서
    // 메인에 다시 접속했을 때 닉네임 설정 모달이 다시 뜨지 않도록 하기 위함
    // 새로 작성한 닉네임도 세션 스토리지에 같이 저장
    setExisting: (state, { payload }) => {
      sessionStorage.setItem('membership', 'existing');
      sessionStorage.setItem('displayName', payload.displayName);
      state.membership = 'existing';
    },
  },
});

export const { login, logout, setExisting } = userSlice.actions;
export default userSlice.reducer;

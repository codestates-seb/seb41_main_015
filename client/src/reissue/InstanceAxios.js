//Axios를 이용해서 Access 토큰과 Refresh 토큰을 갱신하는 방법
import axios from 'axios';

//###1. axios에 특별한 설정을 하기 위해 axios.create 메소드를 이용해서 인스턴스를 만든다.
const instanceAxios = axios.create({
  baseURL: 'https://serverbookvillage.kro.kr',
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
    Accept: 'application/json',
  },
});

//###2. 요청 인터셉터 추가하기(모든 요청의 header에 accessToken을 넣어서 보낸다.)
instanceAxios.interceptors.request.use(
  (config) => {
    // 요청이 전달되기 전에 작업 수행
    const sessionAccessToken = sessionStorage.getItem('accessToken'); // access 토큰을 가져오는 함수
    if (sessionAccessToken) {
      config.headers['Authorization'] = `Bearer ${sessionAccessToken}`;
    }
    return config;
  },
  (error) => {
    // 요청 오류가 있는 작업 수행
    return Promise.reject(error);
  }
);

//###3. 응답 인터셉터 추가하기
instanceAxios.interceptors.response.use(
  (response) => {
    //응답데이터 가공
    return response;
  },
  async (error) => {
    //오류응답 처리
    const { response: errorResponse } = error;
    // const originalRequest = error.config;

    // 인증 에러 발생시
    if (errorResponse.status === 401) {
      return await resetTokenAndReattemptRequest(error);
    }

    return Promise.reject(error);
  }
);

//###4. 토큰갱신과 재요청 함수
let isAlreadyFetchingAccessToken = false; //기존에 받아온 엑세스토큰
let subscribers = []; //가입자
const sessionRefreshToken = sessionStorage.getItem('refreshToken');
const resetTokenAndReattemptRequest = async (error) => {
  try {
    const { response: errorResponse } = error;

    const retryOriginalRequest = new Promise((resolve, reject) => {
      addSubscriber(async (sessionAccessToken) => {
        try {
          errorResponse.config.headers['Authorization'] =
            'Bearer ' + sessionAccessToken;
          resolve(instanceAxios(errorResponse.config));
        } catch (err) {
          reject(err);
        }
      });
    });

    // refresh token을 이용해서 access token 요청
    if (!isAlreadyFetchingAccessToken) {
      isAlreadyFetchingAccessToken = true; // 문닫기 (한 번만 요청)

      const { data } = await axios.post(
        'https://serverbookvillage.kro.kr/auth/token',
        {
          headers: {
            Authorization: { sessionRefreshToken },
          },
        }
      );
      sessionStorage.setItem('newAccessToken', data.newCreatedAccessToken);
      sessionStorage.setItem('newRefresh', data.refreshToken);

      isAlreadyFetchingAccessToken = false; // 문열기 (초기화)
      onAccessTokenFetched(data.newCreatedAccessToken);
    }

    return retryOriginalRequest; // pending 됐다가 onAccessTokenFetched가 호출될 때 resolve
  } catch (error) {
    // signOut();
    return Promise.reject(error);
  }
};
//callback: 엑세스토큰을 인자로 받는 새로운 요청함수
const addSubscriber = (callback) => {
  subscribers.push(callback);
};

const onAccessTokenFetched = (accessToken) => {
  subscribers.forEach((callback) => callback(accessToken));
  subscribers = [];
};
//로그아웃
// function signOut() {
//   removeUserToken('access');
//   removeUserToken('refresh');
//   window.location.href = '/signin';
// }
export default instanceAxios;

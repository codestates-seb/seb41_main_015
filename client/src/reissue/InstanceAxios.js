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
      console.log('sessionAccessToken:', sessionAccessToken); //=?!!!! 엑세스토큰이 만료되면 여기가 undefined로 들어오고 있다...
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
  function (response) {
    //응답데이터 가공
    console.log('get response', response); //res.config.headers.Authorization === Bearer {엑세스토큰}
    return response;
  },
  async function (error) {
    // 오류 응답처리
    const { response: errorResponse } = error;
    const originalRequest = error.config;

    // 인증 에러 발생시
    if (errorResponse.status === 401) {
      return await resetTokenAndReattemptRequest(error);
    }

    return Promise.reject(error);
  }
);
let isAlreadyFetchingAccessToken = false;
let subscribers = [];

async function resetTokenAndReattemptRequest(error) {
  console.log('error:', error);
  try {
    const { response: errorResponse } = error;

    // subscribers에 access token을 받은 이후 재요청할 함수 추가 (401로 실패했던)
    // retryOriginalRequest는 pending 상태로 있다가
    // access token을 받은 이후 onAccessTokenFetched가 호출될 때
    // access token을 넘겨 다시 axios로 요청하고
    // 결과값을 처음 요청했던 promise의 resolve로 settle시킨다.
    const retryOriginalRequest = new Promise((resolve, reject) => {
      addSubscriber(async (accessToken) => {
        console.log('accessToken', accessToken);
        try {
          errorResponse.config.headers['Authorization'] =
            'Bearer ' + accessToken;
          resolve(instanceAxios(errorResponse.config));
        } catch (err) {
          reject(err);
        }
      });
    });

    // refresh token을 이용해서 access token 요청
    if (!isAlreadyFetchingAccessToken) {
      isAlreadyFetchingAccessToken = true; // 문닫기 (한 번만 요청)
      const sessionRefreshToken = sessionStorage.getItem('refreshToken');
      const { data } = await axios.post(
        'https://serverbookvillage.kro.kr/auth/token',
        {
          headers: {
            Authorization: sessionRefreshToken,
          },
        }
      );

      isAlreadyFetchingAccessToken = false; // 문열기 (초기화)

      onAccessTokenFetched(data.headers.get('Authorization'));
    }

    return retryOriginalRequest; // pending 됐다가 onAccessTokenFetched가 호출될 때 resolve
  } catch (error) {
    // signOut();
    return Promise.reject(error);
  }
}

function addSubscriber(callback) {
  subscribers.push(callback);
}
console.log('subscribers', subscribers);

function onAccessTokenFetched(accessToken) {
  subscribers.forEach((callback) => callback(accessToken));
  subscribers = [];
}

// function signOut() {
//   removeUserToken('access');
//   removeUserToken('refresh');
//   window.location.href = '/signin';
// }

export default instanceAxios;

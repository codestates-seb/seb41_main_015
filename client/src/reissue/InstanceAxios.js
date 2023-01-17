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
    const { response: errorResponse } = error; //구조분해할당
    // 인증 에러 발생시
    if (errorResponse.status === 401) {
      const originalRequest = errorResponse.config; //response.config
      const sessionRefreshToken = sessionStorage.getItem('refreshToken');
      const { data } = await axios.post(
        'https://serverbookvillage.kro.kr/auth/token',
        {},
        {
          headers: {
            Authorization: { sessionRefreshToken },
          },
        }
      );
      const {
        Authorization: newCreatedAccessToken,
        // refreshToken: newRefreshToken,
      } = data;

      sessionStorage.setItem('accessToken', newCreatedAccessToken);

      originalRequest.headers[
        'Authorization'
      ] = `Bearer ${newCreatedAccessToken}`;
      return axios(originalRequest);
    }
    return Promise.reject(error);
  }
);

export default instanceAxios;

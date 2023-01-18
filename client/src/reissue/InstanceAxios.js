//Axios를 이용해서 Access 토큰과 Refresh 토큰을 갱신하는 방법
import axios from 'axios';
import { useDispatch } from 'react-redux';

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
      // console.log('sessionAccessToken:', sessionAccessToken); //=?!!!! 엑세스토큰이 만료되면 여기가 undefined로 들어오고 있다...
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
// instanceAxios.interceptors.response.use(
//   (response) => {
//     return response;
//   },
//   async (error) => {
//
//     const { response: errorResponse } = error;
//     if (errorResponse.status === 401) {
//       const originalRequest = errorResponse.config;
//       const sessionRefreshToken = await sessionStorage.getItem('refreshToken');

//       // token refresh 요청
//       const { data } = await axios.post(
//         'https://serverbookvillage.kro.kr/auth/token', // token refresh api
//         {},
//         { headers: { Authorization: sessionRefreshToken } }
//       );
//       // // 새로운 토큰 저장
//
//       sessionStorage.setItem(
//         'newAccessToken',
//         data.headers.get('Authorization')
//       );
//       const newToken = sessionStorage.getItem('newAccessToken');
//       originalRequest.headers.authorization = `Bearer ${newToken}`;
//       // 401로 요청 실패했던 요청 새로운 토큰으로 재요청
//       return axios(originalRequest);
//     }

//     return Promise.reject(error);
//   }
// );
export default instanceAxios;

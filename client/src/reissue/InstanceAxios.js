//Axios를 이용해서 Access 토큰과 Refresh 토큰을 갱신하는 방법
import axios from 'axios';
// import { useDispatch } from 'react-redux';
import jwtDecode from 'jwt-decode';

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

    //유효시간 확인 후 토큰 갱신
    const decoded = jwtDecode(sessionAccessToken); //디코딩
    const now = Math.floor(Date.now() / 1000); //현재시간(초 단위)
    console.log('남은 만료기간', (decoded.exp - now) / 60);
    //기존 토큰 유효시간이 만료되기 5분전일때 리프레쉬 토큰을 이용하여 갱신 요청을 보낸다
    // decoded.exp - now < 5 * 60  => 5 * 60  : 5분전
    // 60* 5 => 5분
    if (decoded.exp - now < 60 * 5) {
      const sessionRefreshToken = sessionStorage.getItem('refreshToken');
      const token = async () => {
        await axios
          .post(
            'https://serverbookvillage.kro.kr/auth/token',
            {},
            {
              headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                Accept: 'application/json',
                Authorization: sessionRefreshToken,
              },
            }
          )
          .then((res) => {
            const newAccessToken = res.headers.get('authorization'); //새로운 토큰 저장
            sessionStorage.setItem('accessToken', newAccessToken);
            console.log('newAccessToken:', newAccessToken);
            if (newAccessToken) {
              config.headers['Authorization'] = `Bearer ${newAccessToken}`;
            }
          })
          .catch((err) => {
            console.error('갱신요청 실패!', err);
          });
      };
      token();
    }
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

export default instanceAxios;

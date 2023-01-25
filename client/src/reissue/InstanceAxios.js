//Axios를 이용해서 Access 토큰과 Refresh 토큰을 갱신하는 방법
import axios from 'axios';
import jwtDecode from 'jwt-decode';
import { getCookie, removeCookie } from '../util/cookie/cookie';
import Swal from 'sweetalert2';

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
    //##요청이 전달되기 전에 작업 수행
    // 토큰 가져오기
    const sessionAccessToken = sessionStorage.getItem('accessToken');
    const cookieRefreshToken = getCookie('refreshToken');
    //토큰 디코딩
    const accessDecoded = jwtDecode(sessionAccessToken);
    const refreshDecode = jwtDecode(cookieRefreshToken);
    //현재시간(초 단위)
    const now = Math.floor(Date.now() / 1000);
    // console.log(
    //   '엑세스 토큰 남은 만료시간(분)',
    //   (accessDecoded.exp - now) / 60
    // );
    // console.log(
    //   '리프레쉬 토큰 남은 만료시간(시)',
    //   (refreshDecode.exp - now) / 60
    // );

    //###3. 엑세스토큰의 유효기간이 끝나기 전에 갱신처리/리프레쉬토큰은 로그아웃 처리
    // 60* 5 => 5분
    if (accessDecoded.exp - now < 60 * 5) {
      //if(리프레쉬토큰 만료되기 1분 전이라면)
      if (refreshDecode.exp - now < 60 * 1) {
        //로그아웃
        sessionStorage.clear();
        removeCookie('refreshToken');
        window.location.reload();
      } else {
        //리프레쉬토큰 만료기간이 남았다면 엑세스토큰 갱신 요청
        const cookieRefreshToken = getCookie('refreshToken');
        const token = async () => {
          await axios
            .post(
              'https://serverbookvillage.kro.kr/auth/token',
              {},
              {
                headers: {
                  'Content-Type': 'application/json;charset=UTF-8',
                  Accept: 'application/json',
                  Authorization: cookieRefreshToken,
                },
              }
            )
            .then((res) => {
              //새로운 토큰 저장
              const newAccessToken = res.headers.get('authorization');
              sessionStorage.setItem('accessToken', newAccessToken);
              if (newAccessToken) {
                config.headers['Authorization'] = `Bearer ${newAccessToken}`;
              }
            })
            .catch((err) => {
              Swal.fire(
                '죄송합니다',
                '로그아웃 후 다시 이용해주세요.',
                'warning'
              );
            });
        };
        token();
      }
    }
    //엑세스토큰 만료기간이 지나지 않은 경우
    if (sessionAccessToken) {
      config.headers['Authorization'] = `Bearer ${sessionAccessToken}`;
    }

    return config;
  },

  (error) => {
    //##요청 오류가 있는 작업 수행
    return Promise.reject(error);
  }
);

export default instanceAxios;

//Axios를 이용해서 Access 토큰과 Refresh 토큰을 갱신하는 방법
import axios from 'axios';
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
    //##요청이 전달되기 전에 작업 수행
    const sessionAccessToken = sessionStorage.getItem('accessToken'); // access 토큰가져오기
    const sessionRefreshToken = sessionStorage.getItem('refreshToken'); //refresh 토큰기져오기

    const accessDecoded = jwtDecode(sessionAccessToken); // access 토큰 디코딩
    const refreshDecode = jwtDecode(sessionRefreshToken); //refresh 토큰 디코딩
    const now = Math.floor(Date.now() / 1000); //현재시간(초 단위)
    console.log(
      '엑세스 토큰 남은 만료시간(분)',
      (accessDecoded.exp - now) / 60
    );
    console.log(
      '리프레쉬 토큰 남은 만료시간(시)',
      (refreshDecode.exp - now) / 60
    );
    //###3. 엑세스토큰 유효기간 확인 후 만료시간이 5분 미만으로 내려갈 때 리프레쉬 토큰으로 갱신해주기
    // 60* 5 => 5분
    //갱신된 토큰으로 요청
    if (accessDecoded.exp - now < 60 * 5) {
      console.log('1');
      //if(리프레쉬토큰 만료되기 1분 전이라면){로그아웃}else{엑세스토큰 갱신 요청}
      if (refreshDecode.exp - now < 60 * 1) {
        console.log('2');
        //로그아웃
        sessionStorage.clear();
        window.location.reload();
      } else {
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
              console.log('res', res);
              const newAccessToken = res.headers.get('authorization'); //새로운 토큰 저장
              sessionStorage.setItem('accessToken', newAccessToken);
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
    }
    //만료기간이 지나지 않은 토큰으로 요청
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

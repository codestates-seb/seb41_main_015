//Axios를 이용해서 Access 토큰과 Refresh 토큰을 갱신하는 방법
import axios from 'axios';
import { useDispatch } from 'react-redux';
import { logout } from '../redux/slice/userSlice';
import jwtDecode from 'jwt-decode';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';

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
    const sessionAccessToken = sessionStorage.getItem('accessToken'); // access 토큰을 가져오는 함수

    //###3. 엑세스토큰 유효기간 확인 후 만료시간이 5분 미만으로 내려갈 때 리프레쉬 토큰으로 갱신해주기
    const decoded = jwtDecode(sessionAccessToken); //디코딩
    const now = Math.floor(Date.now() / 1000); //현재시간(초 단위)
    console.log('남은 만료기간', (decoded.exp - now) / 60);
    //기존 토큰 유효시간이 만료되기 5분전일때 리프레쉬 토큰을 이용하여 갱신 요청을 보낸다
    // 60* 5 => 5분
    //갱신된 토큰으로 요청
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

//###4. 리프레쉬 토큰이 만료된 응답으로 돌아올 때 자동 로그아웃
instanceAxios.interceptors.response.use(
  (response) => {
    //##응답 데이터가 있는 작업 수행
    console.log('get res!', response.status);
    return response;
  },
  (error) => {
    //## 응답 오류가 있는 작업 수행
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { data } = error;
    // 리프레쉬토큰 만료를 알 수 있는 응답을 if문 안에 넣을 것!
    if (data.status === 401) {
      const handleLogout = () => {
        instanceAxios
          .post('/v1/members/auth/logout')
          .then(() => {
            Swal.fire(
              '로그인이 만료되었습니다',
              '다시 로그인 후 이용해주세요.',
              'warning'
            );
            dispatch(logout());
            sessionStorage.clear();
            navigate('/');
            window.location.reload();
          })
          .catch((err) => {
            console.error(err);
          });
      };
      handleLogout();
    }

    return Promise.reject(error);
  }
);
export default instanceAxios;

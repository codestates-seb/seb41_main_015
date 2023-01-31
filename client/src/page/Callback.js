import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { login } from '../redux/slice/userSlice';
import { useNavigate } from 'react-router-dom';

const Callback = () => {
  // 서버에서 쿼리파라미터로 보내주는 토큰을 받아서 스토리지에 저장하는 페이지
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const url = new URL(window.location.href);
  const accessToken = url.searchParams.get('access_token');
  const refreshToken = url.searchParams.get('refresh_token');
  const membership = url.searchParams.get('membership');

  const sessionAccessToken = sessionStorage.getItem('accessToken');

  useEffect(() => {
    // dispatch 써서 로그인 상태 변경
    dispatch(login({ accessToken, refreshToken, membership }));
    // existing, new에 따라서 홈으로 리다이렉트 되었을 때 모달을 띄울지 결정한다
    // main page에서 accessToken이 존재하고 new일 때만 모달 띄우도록 하기
  }, []);

  // 홈으로 이동
  useEffect(() => {
    navigate('/');
  }, [sessionAccessToken]);

  return;
};

export default Callback;

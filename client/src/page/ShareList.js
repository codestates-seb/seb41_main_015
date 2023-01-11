import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { login } from '../redux/slice/userSlice';

const ShareList = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    const url = new URL(window.location.href);
    const accessToken = url.searchParams.get('access_token');
    const refreshToken = url.searchParams.get('refresh_token');

    // dispatch 써서 로그인 상태 변경
    dispatch(login({ accessToken: accessToken, refreshToken: refreshToken }));
  }, []);

  return <p>조회 부분입니다</p>;
};
export default ShareList;

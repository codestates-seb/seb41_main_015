import { useState, useEffect } from 'react';
import LoginModal from './LoginModal';
import styled from 'styled-components';
import { ReactComponent as Logo } from '../image/logo.svg';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { logout } from '../redux/slice/userSlice';
import instanceAxios from '../reissue/InstanceAxios';

const StyledHeader = styled.header`
  width: 100%;
  height: 60px;
  padding: 3px 10px;
  display: flex;
  align-items: center;
  box-shadow: 0px 4px 4px 0px #b9b9b940;
  background-color: #ffffff;
  position: sticky;
  top: 0;
  z-index: 2;
`;

const SHeaderLogo = styled.a`
  display: flex;
  align-items: center;
  position: fixed;
  left: 3%;
  .logo {
    fill: #bb2649;
  }
  @media screen and (max-width: 1030px) {
    .logo {
      font-size: 15px;
      width: 150px;
    }
  }
`;

const SNavContainer = styled.ol`
  width: 444px;
  display: flex;
  flex-direction: row;
  text-align: center;
  padding: 0;
  position: fixed;
  left: 20%;
  .olItem {
    margin: 5px 33px;
    font-size: 18px;
    font-weight: 600;
  }
  .focused {
    border-bottom: 3px solid #bb2649;
  }

  .preparing {
    color: #acacac;
    &:hover {
      cursor: default;
    }
  }

  .balloon {
    display: none;
    position: absolute;
    width: 120px;
    padding: 8px;
    top: 28px;
    left: 305px;
    -webkit-border-radius: 8px;
    -moz-border-radius: 8px;
    border-radius: 8px;
    background: #333333db;
    color: #fff;
    font-size: 14px;
    font-weight: 500;
    @media screen and (max-width: 1030px) {
      top: 25px;
      left: 120px;
    }
  }

  .balloon:after {
    position: absolute;
    bottom: 100%;
    left: 50%;
    width: 0;
    height: 0;
    margin-left: -10px;
    border: solid transparent;
    border-color: rgba(51, 51, 51, 0);
    border-bottom-color: #333333db;
    border-width: 10px;
    pointer-events: none;
    content: ' ';
  }

  div:hover + p.balloon {
    display: block;
  }

  @media screen and (max-width: 1030px) {
    left: 32%;
    .olItem {
      margin: 5px 10px;
      font-size: 15px;
      font-weight: 600;
      display: flex;
    }
  }
`;

const SLoginBtn = styled.button`
  width: 90px;
  height: 33px;
  font-size: 16px;
  font-weight: 600;
  color: #bb2649;
  border: 1px solid #bb2649;
  border-radius: 20px;
  position: fixed;
  right: 3%;
  :hover {
    color: #ffffff;
    background-color: #bb2649;
  }
`;

const SLogout = styled.div`
  display: flex;
  align-items: center;
  position: fixed;
  right: 2%;
  .mypage {
    display: flex;
    margin-right: 15px;
    width: 30px;
    height: 30px;
    border-radius: 50%;
    @media screen and (max-width: 550px) {
      display: none;
    }
  }
`;

const SLogoutBtn = styled.button`
  width: 90px;
  height: 33px;
  font-size: 16px;
  font-weight: 600;
  color: #ffffff;
  background-color: #bb2649;
  border: 1px solid #bb2649;
  border-radius: 20px;
  :hover {
    color: #bb2649;
    border: 1px solid #bb2649;
    background-color: #ffffff;
  }
`;

const Header = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const accessToken = useSelector((state) => state.user.accessToken);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [profileData, setProfileData] = useState('');

  const handleOpenModal = () => {
    setIsModalOpen(true);
  };
  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  const handleLogout = () => {
    instanceAxios
      .post('/v1/members/auth/logout')
      .then(() => {
        dispatch(logout());
        navigate();
        window.location.reload();
      })
      .catch((err) => {
        console.error(err);
      });
  };

  // 헤더 바깥부분 클릭해도 유지되는 로직 (수정 후: pathname 이용)
  // 나눔 관련 경로: shareList, shareAdd, shareDetail, shareEdit
  // 요청 관련 경로: reqList, reqAdd, reqDetail, reqEdit
  const currentLocation = (pathname) => {
    if (pathname.slice(1, 4) === 'req') {
      return 'request';
    } else if (pathname.slice(1, 6) === 'share') {
      return 'share';
    } else if (pathname.slice(1, 5) === 'rate') {
      return 'rate';
    }
  };

  const shareClassName =
    currentLocation(pathname) === 'share' ? 'olItem focused' : 'olItem';
  const requestClassName =
    currentLocation(pathname) === 'request' ? 'olItem focused' : 'olItem';
  const rateClassName =
    currentLocation(pathname) === 'rate' ? 'olItem focused' : 'olItem';

  //프로필 이미지 가져오기
  useEffect(() => {
    const profileData = async () => {
      try {
        const res = await instanceAxios.get('/v1/members');
        setProfileData(res.data.data.imgUrl);
      } catch (error) {
        console.error(error);
      }
    };
    // 로그인한 상태일 때만 profileData 함수가 작동
    const accessToken = sessionStorage.getItem('accessToken');
    if (accessToken) {
      profileData();
    }
  }, []);

  return (
    <StyledHeader>
      <SHeaderLogo href="/">
        <Logo className="logo" />
      </SHeaderLogo>
      <SNavContainer>
        <Link to="/shareList" className={shareClassName}>
          나눔
        </Link>
        <Link to="/reqList" className={requestClassName}>
          요청
        </Link>
        <Link to="/rateList" className={rateClassName}>
          평점
        </Link>
        <div className="olItem preparing">커뮤니티</div>
        <p className="balloon">준비 중입니다.</p>
      </SNavContainer>
      {!accessToken ? (
        <>
          <SLoginBtn onClick={handleOpenModal}>로그인</SLoginBtn>
          <LoginModal
            isModalOpen={isModalOpen}
            handleCloseModal={handleCloseModal}
          />
        </>
      ) : (
        <SLogout>
          <Link to="/mypage">
            <img src={profileData} alt="mypage" className="mypage" />
          </Link>
          <SLogoutBtn onClick={handleLogout}>로그아웃</SLogoutBtn>
        </SLogout>
      )}
    </StyledHeader>
  );
};

export default Header;

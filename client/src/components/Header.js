import { useState } from 'react';
import LoginModal from './LoginModal';
import styled from 'styled-components';
import logo from '../image/logo.svg';
import { Link, useNavigate } from 'react-router-dom';
import mypage from '../image/mypage.svg';
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

// 로그인
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

// 로그아웃
const SLogout = styled.div`
  display: flex;
  align-items: center;
  position: fixed;
  right: 2%;
  .mypage {
    display: flex;
    margin-right: 15px;
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
  const accessToken = useSelector((state) => state.user.accessToken);
  const [isModalOpen, setIsModalOpen] = useState(false);

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
        console.log('로그아웃 됨!');
      })
      .catch((err) => {
        console.error(err);
      });
  };

  // 헤더 바깥부분 클릭해도 유지되는 로직
  const [currentTab, setCurrentTab] = useState(0);
  const menus = [
    { index: 0, name: '나눔', route: '/shareList' },
    { index: 1, name: '요청', route: '/reqList' },
    { index: 2, name: '평점', route: '/' },
    { index: 3, name: '커뮤니티', route: '/' },
  ];
  const handleMenuSelect = (index) => {
    setCurrentTab(index);
  };

  return (
    <StyledHeader>
      <SHeaderLogo href="/">
        <img src={logo} alt="logo" className="logo" />
      </SHeaderLogo>
      <SNavContainer>
        {menus.map((el) => {
          const isFocused =
            currentTab === el.index ? 'olItem focused' : 'olItem';
          return (
            <Link
              to={el.route}
              key={el.index}
              onClick={() => handleMenuSelect(el.index)}
              className={isFocused}
            >
              {el.name}
            </Link>
          );
        })}
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
            <img src={mypage} alt="mypage" className="mypage" />
          </Link>
          <SLogoutBtn onClick={handleLogout}>로그아웃</SLogoutBtn>
        </SLogout>
      )}
    </StyledHeader>
  );
};

export default Header;

import { useState } from 'react';
import LoginModal from './LoginModal';
import styled from 'styled-components';
import logo from '../image/logo.svg';
import { Link } from 'react-router-dom';
import mypage from '../image/mypage.svg';

const StyledHeader = styled.header`
  width: 100%;
  height: 60px;
  padding: 3px 10px;
  display: flex;
  align-items: center;
  box-shadow: 0px 4px 4px 0px #b9b9b940;
  background-color: #ffffff;
  /* position: fixed; */
`;

const SHeaderLogo = styled.a`
  display: flex;
  align-items: center;
  position: fixed;
  left: 3%;
  .logoFont {
    margin: 0 10px;
    font-size: 24px;
    font-weight: 500;
  }
  @media screen and (max-width: 930px) {
    .logoFont {
      font-size: 15px;
    }
  }
`;

const SNavContainer = styled.ol`
  width: 444px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  padding: 0;
  position: fixed;
  left: 20%;
  .olItem {
    padding: 5px 33px;
    font-size: 18px;
    font-weight: 600;
  }

  @media screen and (max-width: 930px) {
    position: fixed;
    left: 11%;

    .olItem {
      padding: 5px 10px;
      font-size: 15px;
      font-weight: 600;
      display: flex;
    }
  }
`;

// 로그인
// const SLoginBtn = styled.button`
//   width: 90px;
//   height: 33px;
//   font-size: 16px;
//   font-weight: 600;
//   color: #bb2649;
//   border: 1px solid #bb2649;
//   border-radius: 20px;
//   position: fixed;
//   right: 3%;
//   :hover {
//     color: #ffffff;
//     background-color: #bb2649;
//   }
// `;

// 로그아웃
const SLogout = styled.div`
  display: flex;
  align-items: center;
  align-items: center;
  position: fixed;
  right: 3%;
  @media screen and (max-width: 550px) {
    .mypage {
      display: none;
    }
  }
  .mypage {
    display: flex;
    margin-right: 15px;
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
  const [isModalOpen, setIsModalOpen] = useState(false);
  const handleOpenModal = () => {
    setIsModalOpen(true);
  };
  const handleCloseModal = () => {
    setIsModalOpen(false);
  };
  return (
    <StyledHeader>
      <SHeaderLogo href="/">
        <img src={logo} alt="logo" className="logo" />
        <div className="logoFont">book village</div>
      </SHeaderLogo>
      <SNavContainer>
        <Link to="/shareList" className="olItem">
          나눔
        </Link>
        <Link to="/reqList" className="olItem">
          요청
        </Link>
        <Link to="/" className="olItem">
          평점
        </Link>
        <Link to="/" className="olItem">
          커뮤니티
        </Link>
      </SNavContainer>
      {/* 로그인 */}
      {/* <SLoginBtn onClick={handleOpenModal}>로그인</SLoginBtn>
      <LoginModal
        isModalOpen={isModalOpen}
        handleCloseModal={handleCloseModal}
      /> */}
      {/* 로그아웃 */}
      <SLogout>
        <Link to="/mypage">
          <img src={mypage} alt="mypage" className="mypage" />
        </Link>
        <SLogoutBtn>로그아웃</SLogoutBtn>
      </SLogout>
    </StyledHeader>
  );
};

export default Header;

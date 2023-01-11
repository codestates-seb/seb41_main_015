import { useState } from 'react';
import LoginModal from '../page/Login';
import styled from 'styled-components';
import logo from '../image/logo.svg';
import { Link } from 'react-router-dom';

const StyledHeader = styled.header`
  width: 100%;
  height: 60px;
  padding: 3px 10px;
  display: flex;
  align-items: center;
  box-shadow: 0px 4px 4px 0px #b9b9b940;
  /* position: fixed; */
`;
const SHeaderLogo = styled.div`
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
      <SHeaderLogo>
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
      <SLoginBtn onClick={handleOpenModal}>로그인</SLoginBtn>
      <LoginModal
        isModalOpen={isModalOpen}
        handleCloseModal={handleCloseModal}
      />
    </StyledHeader>
  );
};

export default Header;

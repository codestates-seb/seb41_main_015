import styled from 'styled-components';
import { ReactComponent as Google } from '../image/google.svg';
import { ReactComponent as Kakao } from '../image/kakao.svg';
import { ReactComponent as Naver } from '../image/naver.svg';

const SModalBackground = styled.div`
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.6);
`;

const SLoginModal = styled.div`
  width: 448px;
  height: 430px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border-radius: 5px;
  border: 1px solid #aaaaaa;
  background-color: #ffffff;

  .modalContent {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding-top: 2rem;

    h2 {
      margin-bottom: 30px;
      color: #bb2649;
    }
  }

  .close {
    padding-top: 3px;
    position: absolute;
    right: 13px;
    font-size: 2rem;
    color: #aaaaaa;
    cursor: pointer;
  }

  .bottomText {
    font-size: 0.8rem;
  }

  button {
    margin: 10px 0px;
    display: flex;
    justify-content: center;
    gap: 40px;
    align-items: center;
    border: none;
    border-radius: 5px;
    width: 360px;
    height: 58px;
    font-weight: 700;
  }

  .withGoogle {
    margin-top: 20px;
    border: 1px solid #c0c0c0;
    .googleText {
      padding: 0 0.5rem;
    }
  }

  .withKakao {
    background-color: #f9e000;
  }

  .withNaver {
    background-color: #2db400;
    color: white;
  }
`;

const LoginModal = ({ isModalOpen, handleCloseModal }) => {
  const handleSocialLogin = (type) => {

    window.location.assign(
      `https://serverbookvillage.kro.kr/oauth2/authorization/${type}`
    );

    // 테스트용
    //   window.location.assign(
    //     'http://localhost:3000/oauth?access_token=&refresh_token=&membership=existing'
    //   );

  };

  return (
    <>
      {isModalOpen ? (
        <SModalBackground onClick={handleCloseModal}>
          <SLoginModal onClick={(e) => e.stopPropagation()}>
            <div className="close" onClick={handleCloseModal}>
              &times;
            </div>
            <div className="modalContent">
              <h2>로그인</h2>
              <div className="bottomText">
                복잡한 과정 없이 간편하게 로그인하세요
              </div>
              <button
                className="withGoogle"
                onClick={() => handleSocialLogin('google')}
              >
                <Google />
                <div className="googleText">구글 계정으로 로그인하기</div>
              </button>
              <button
                className="withKakao"
                onClick={() => handleSocialLogin('kakao')}
              >
                <Kakao />
                카카오 계정으로 로그인하기
              </button>
              <button
                className="withNaver"
                onClick={() => handleSocialLogin('naver')}
              >
                <Naver />
                네이버 계정으로 로그인하기
              </button>
            </div>
          </SLoginModal>
        </SModalBackground>
      ) : null}
    </>
  );
};

export default LoginModal;

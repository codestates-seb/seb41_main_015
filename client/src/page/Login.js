import styled from 'styled-components';
import { ReactComponent as Google } from '../image/google.svg';
import { ReactComponent as Kakao } from '../image/kakao.svg';
import { ReactComponent as Naver } from '../image/naver.svg';

const SLoginLayout = styled.main`
  height: 90vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  h2 {
    font-size: 27px;
    font-weight: 700;
    color: #bb2649;
  }

  .bottomText {
    margin-top: 30px;
  }

  .loginForm {
    width: 448px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
  }
`;

const SLoginBox = styled.div`
  width: 448px;
  height: 430px;
  border-radius: 20px;
  box-shadow: 0px 5px 20px 10px rgba(0, 0, 0, 0.16);

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 40px;

  button {
    display: flex;
    justify-content: space-evenly;
    align-items: center;
    border: none;
    border-radius: 5px;
    width: 313px;
    height: 58px;
    font-weight: 700;
  }

  .withGoogle {
    border: 1px solid #c0c0c0;
  }

  .withKakao {
    background-color: #f9e000;
  }

  .withNaver {
    background-color: #2db400;
    color: white;
  }
`;

const Login = () => {
  const handleSocialLogin = (type) => {
    // window.location.assign(
    //   `https://serverbookvillage.kro.kr/oauth2/authorization/${type}`
    // );

    // 테스트용
    window.location.assign(
      'http://localhost:3000/oauth?access_token=something&refresh_token=something2&membership=new'
    );
  };

  return (
    <SLoginLayout>
      <div className="loginForm">
        <h2>로그인</h2>
        <SLoginBox>
          <button
            className="withGoogle"
            onClick={() => handleSocialLogin('google')}
          >
            <Google />
            구글 계정으로 로그인하기
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
        </SLoginBox>
        <div className="bottomText">복잡한 과정 없이 간편하게 로그인하세요</div>
      </div>
    </SLoginLayout>
  );
};

export default Login;

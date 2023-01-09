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
  text-align: center;

  h2 {
    font-size: 27px;
    font-weight: 700;
    color: #bb2649;
  }

  div {
    margin-top: 30px;
  }
`;

const SLoginBox = styled.div`
  width: 448px;
  height: 450px;
  border-radius: 20px;
  box-shadow: 0px 5px 20px 10px rgba(0, 0, 0, 0.16);

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 45px;

  button {
    display: flex;
    justify-content: space-evenly;
    align-items: center;
    border: none;
    border-radius: 5px;
    width: 313px;
    height: 53px;
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
  return (
    <SLoginLayout>
      <h2>로그인</h2>
      <SLoginBox>
        <button className="withGoogle">
          <Google />
          구글 계정으로 로그인하기
        </button>
        <button className="withKakao">
          <Kakao />
          카카오 계정으로 로그인하기
        </button>
        <button className="withNaver">
          <Naver />
          네이버 계정으로 로그인하기
        </button>
      </SLoginBox>
      <div>복잡한 과정 없이 간편하게 로그인하세요</div>
    </SLoginLayout>
  );
};

export default Login;

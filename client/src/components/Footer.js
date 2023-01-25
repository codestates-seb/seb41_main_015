import styled from 'styled-components';
import { ReactComponent as Logo } from '../image/logo.svg';
import github from '../image/github.svg';

const StyledFooter = styled.footer`
  width: 100%;
  height: 280px;
  padding: 20px;
  background-color: #f2f2f2;
  display: flex;
  flex-flow: row wrap;
  bottom: 0;
  justify-content: center;
  margin-top: 5%;

  @media screen and (max-width: 1245px) {
    height: 300px;
    justify-content: flex-start;
  }
`;

const SFooterLogo = styled.div`
  height: 35px;
  display: flex;
  margin: 13px 25px 200px 0;
  align-items: center;

  @media screen and (max-width: 1328px) {
    margin: 0;
  }

  a {
    display: flex;
    align-items: center;
  }
  .logo {
    width: 160px;
    height: 100px;
    fill: #444444;
  }
`;

const SGitContainer = styled.div`
  display: flex;
  margin: 0 10px 0 30px;
  color: #444444;

  .githubBox {
    max-width: 130px;
    text-align: center;
    padding: 10px 30px 0 0;
    white-space: nowrap;
    @media screen and (max-width: 822px) {
      padding: 10px 22px 0 0;
    }
    li {
      display: flex;
      align-items: center;
      font-size: 13px;
      padding: 0 15px 15px;
      color: #7a7a7a;

      a {
        display: flex;
        align-items: center;
      }
    }
    .github {
      padding-right: 7px;
    }
  }
`;

const SDescription = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;

  p {
    color: #7a7a7a;
    font-weight: 600;
    font-size: 13px;
    line-height: 28px;
    @media screen and (max-width: 822px) {
      display: none;
    }
  }

  .copyright {
    color: #7a7a7a;
    font-size: 12px;
    display: flex;
    justify-content: flex-end;

    @media screen and (max-width: 822px) {
      position: relative;
      bottom: -35%;
      right: -16%;
      font-size: 11px;
    }
  }
`;

const Footer = () => {
  return (
    <StyledFooter>
      <SFooterLogo>
        <a href="/">
          <Logo className="logo" />
        </a>
      </SFooterLogo>
      <SGitContainer>
        <ul className="githubBox">
          <h4>BE</h4>
          <li>
            <a href="https://github.com/SsangSoo">
              <img src={github} alt="github" className="github" />
              김성수
            </a>
          </li>
          <li>
            <a href="https://github.com/pjongk148">
              <img src={github} alt="github" className="github" />
              박종혁
            </a>
          </li>
          <li>
            <a href="https://github.com/orioncsy">
              <img src={github} alt="github" className="github" />
              채승윤
            </a>
          </li>
        </ul>
        <ul className="githubBox">
          <h4>FE</h4>
          <li>
            <a href="https://github.com/aemaaeng">
              <img src={github} alt="github" className="github" />
              김혜민
            </a>
          </li>
          <li>
            <a href="https://github.com/yejin32">
              <img src={github} alt="github" className="github" />
              장예진
            </a>
          </li>
          <li>
            <a href="https://github.com/djWjfk">
              <img src={github} alt="github" className="github" />
              최윤지
            </a>
          </li>
          <li>
            <a href="https://github.com/cjsrmachs">
              <img src={github} alt="github" className="github" />
              최창훈
            </a>
          </li>
        </ul>
        <SDescription>
          <p>
            더 이상 독서는 혼자만의 취미가 아니다. <br />
            책을 사랑하는, 책과 가까워지고 싶은, 혹은 비슷한 사람들끼리 취미를
            공유하고 싶어하는 모두가 모여 하나의 마을을 형성하는
            <br /> book village는 사용자 모두가 이웃처럼 서로의 지식을 나누고
            책을 공유합니다.
            <br /> 함께하는 독서 습관을 만들어주는 서비스, book village에 오신
            걸 환영합니다!
          </p>
          <div className="copyright">copyright ⓒ 2023 by book15</div>
        </SDescription>
      </SGitContainer>
    </StyledFooter>
  );
};
export default Footer;

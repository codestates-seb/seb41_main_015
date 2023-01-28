import styled from 'styled-components';
import Carousel from 'react-material-ui-carousel';
import { Paper } from '@mui/material';
import leftArrow from '../image/leftArrow.svg';
import rightArrow from '../image/rightArrow.svg';
import loginModalScreen from '../image/loginModalScreen.png';
import introImg from '../image/bookvillageImg.png';
import shareListImg from '../image/shareList.png';
import reqAddImg from '../image/reqAddImg.png';
import rateList from '../image/rateList.png';
import { useNavigate } from 'react-router-dom';

const StyledCarousel = styled.div`
  .Form1 {
    height: 600px;
    background-color: aliceblue;
    display: flex;
    align-items: center;
    .homeContainer {
      display: flex;
      justify-content: space-around;
      align-items: center;
      width: 1024px;
      margin: 0 auto;
      @media screen and (max-width: 900px) {
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
    img {
      width: 550px;
      border-radius: 5px;
      @media screen and (max-width: 900px) {
        display: none;
      }
    }
    .img1Container {
      padding-bottom: 20px;
    }
    .desSt {
      color: #42728f;
      font-weight: 500;
    }
  }
  .Form2 {
    height: 600px;
    background-color: #fbf7f2;
    display: flex;
    align-items: center;
    .shareContainer {
      display: flex;
      justify-content: space-around;
      align-items: center;
      width: 1024px;
      margin: 0 auto;
      @media screen and (max-width: 785px) {
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
    img {
      width: 320px;
      border-radius: 5px;
      @media screen and (max-width: 785px) {
        display: none;
      }
    }
    .ask {
      color: #d57600;
      margin-bottom: 10px;
    }
    .toShareAdd {
      outline: none;
      padding: 6px 10px;
      color: #d57600;
      border: 2px solid #d57600;
      border-radius: 5px;
      &:hover {
        font-weight: 600;
      }
    }
  }
  .Form3 {
    height: 600px;
    background-color: #e7ece9;
    display: flex;
    align-items: center;
    .reqContainer {
      display: flex;
      justify-content: space-around;
      align-items: center;
      width: 1024px;
      margin: 0 auto;
      @media screen and (max-width: 785px) {
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
    img {
      width: 320px;
      border-radius: 5px;
      @media screen and (max-width: 785px) {
        display: none;
      }
    }
    .ask {
      color: #6f957b;
      margin-bottom: 10px;
    }

    button {
      outline: none;
      padding: 6px 10px;
      color: #6f957b;
      border: 2px solid #6f957b;
      border-radius: 5px;
      &:hover {
        font-weight: 600;
      }
    }
  }
  .Form4 {
    height: 600px;
    background-color: #eeeaee;
    display: flex;
    align-items: center;
    .rateContainer {
      display: flex;
      justify-content: space-around;
      align-items: center;
      width: 1024px;
      margin: 0 auto;
      @media screen and (max-width: 996px) {
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
    img {
      width: 600px;
      border-radius: 3px;
      @media screen and (max-width: 996px) {
        display: none;
      }
    }
    .ask {
      color: #a24da2;
      margin-bottom: 10px;
    }
    p {
      line-height: 1.3rem;
    }
    .toRateList {
      outline: none;
      padding: 6px 10px;
      color: #a24da2;
      border: 2px solid #a24da2;
      border-radius: 5px;
      &:hover {
        font-weight: 600;
      }
    }
  }
  .Form5 {
    height: 600px;
    background-color: #f2f2fb;
    display: flex;
    align-items: center;
    .loginContainer {
      display: flex;
      justify-content: center;
      gap: 100px;
      align-items: center;
      width: 1024px;
      margin: 0 auto;
      @media screen and (max-width: 996px) {
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
    img {
      width: 380px;
      border-radius: 3px;
      @media screen and (max-width: 996px) {
        display: none;
      }
    }
    .ask {
      color: #9066ba;
      margin-bottom: 10px;
      font-weight: 500;
    }
    p {
      line-height: 1.3rem;
    }
  }
`;

const CarouselForm = () => {
  const navigate = useNavigate();
  return (
    <>
      <Carousel
        navButtonsProps={{
          style: {
            padding: '15px',
          },
        }}
        navButtonsWrapperProps={{
          style: {
            padding: '5%',
          },
        }}
        NextIcon={<img src={rightArrow} alt="rightArrow" />}
        PrevIcon={<img src={leftArrow} alt="leftArrow" />}
      >
        <StyledCarousel>
          <Paper variant="outlined" square className="Form1">
            <div className="homeContainer">
              <div className="img1Container">
                <img src={introImg} alt="loginModalScreen" />
              </div>
              <div>
                <h1>
                  나만의 공간,
                  <br />
                  우리 모두의 공간
                </h1>
                <p>다양한 책을 무료나눔하고 읽고 싶은 책은 요청까지 한 번에</p>
                <p className="desSt">
                  책을 가까이 하는 삶을 만드는 Book Village 입니다
                </p>
              </div>
            </div>
          </Paper>
        </StyledCarousel>
        <StyledCarousel>
          <Paper variant="outlined" square className="Form2">
            <div className="shareContainer">
              <div>
                <h1>
                  좋은 책,
                  <br />안 읽는 책은 공유해요
                </h1>
                <p>갖고 있는 도서를 무료나눔할 수 있어요.</p>
                <div className="ask">나눔하고 싶은 책이 있나요?</div>
                <button
                  className="toShareAdd"
                  onClick={() => navigate(`/shareAdd`)}
                >
                  나눔하러 가기 &gt;
                </button>
              </div>
              <div>
                <img src={shareListImg} alt="shareListImg" />
              </div>
            </div>
          </Paper>
        </StyledCarousel>
        <StyledCarousel>
          <Paper variant="outlined" square className="Form3">
            <div className="reqContainer">
              <img src={reqAddImg} alt="reqAddImg" />
              <div>
                <h1>
                  나에게 없는 책은 <br />
                  빌리지 사람들에게서
                </h1>
                <p>읽고 싶은 책이 있다면, 다른 사람들에게 책을 요청하세요</p>
                <div className="ask">요청하고 싶은 책이 있나요?</div>
                <button
                  className="toRateList"
                  onClick={() => navigate(`/reqAdd`)}
                >
                  요청하러 가기 &gt;
                </button>
              </div>
            </div>
          </Paper>
        </StyledCarousel>
        <StyledCarousel>
          <Paper variant="outlined" square className="Form4">
            <div className="rateContainer">
              <div>
                <h1>
                  같은 생각, <br />
                  다른 생각을 공유해요
                </h1>
                <p>
                  특별히 감명깊게 읽은 책이 있다면 평점을 남기고 <br />
                  다른 사람들은 어떻게 생각하는지 구경할 수 있어요
                </p>
                <div className="ask">
                  빌리지 사람들이 평가한 책이 궁금하다면
                </div>
                <button
                  className="toRateList"
                  onClick={() => navigate(`/rateList`)}
                >
                  구경하러 가기 &gt;
                </button>
              </div>
              <div>
                <img src={rateList} alt="rateListImg" />
              </div>
            </div>
          </Paper>
        </StyledCarousel>
        <StyledCarousel>
          <Paper variant="outlined" square className="Form5">
            <div className="loginContainer">
              <div>
                <img src={loginModalScreen} alt="loginModalImg" />
              </div>
              <div>
                <h1>
                  쉽고 간편하게 <br />
                  소셜 로그인으로
                </h1>
                <p>
                  복잡한 회원가입 없이 <br />
                  편리하게 이용할 수 있어요.
                </p>
                <div className="ask">
                  지금 바로 Book Village를 시작해보세요!
                </div>
              </div>
            </div>
          </Paper>
        </StyledCarousel>
      </Carousel>
    </>
  );
};

export default CarouselForm;

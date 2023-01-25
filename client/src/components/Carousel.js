import styled from 'styled-components';
import Carousel from 'react-material-ui-carousel';
import { Paper } from '@mui/material';
import leftArrow from '../image/leftArrow.svg';
import rightArrow from '../image/rightArrow.svg';

const StyledCarousel = styled.div`
  /* padding: 0 50px; */
  .Form {
    height: 500px;
    padding: 10px 45px;
    background-color: aliceblue;
  }
`;

const CarouselForm = (props) => {
  var items = [
    {
      title: '# page1',
      content: 'Carousel 1',
      des: '',
      img: 'https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg',
    },
    {
      title: '# page2',
      content: 'Carousel 2',
      des: '더 이상 독서는 혼자만의 취미가 아니다. 책을 사랑하는, 책과 가까워지고 싶은, 혹은 비슷한 사람들끼리 취미를 공유하고 싶어하는 모두가 모여 하나의 마을을 형성하는 book village는 사용자 모두가 이웃처럼 서로의 지식을 나누고 책을 공유합니다. 함께하는 독서 습관을 만들어주는 서비스, book village에 오신 걸 환영합니다!',
    },
  ];

  return (
    <Carousel
      NextIcon={<img src={rightArrow} alt="rightArrow" />}
      PrevIcon={<img src={leftArrow} alt="leftArrow" />}
    >
      {items.map((item, index) => (
        <StyledCarousel key={index} item={item}>
          <Paper variant="outlined" square className="Form">
            <h2>{item.title}</h2>
            <p>{item.content}</p>
            <p>{item.des}</p>
            <img src={item.img} alt="img" />
          </Paper>
        </StyledCarousel>
      ))}
    </Carousel>
  );
};

export default CarouselForm;

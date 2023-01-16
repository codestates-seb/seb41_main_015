import styled from 'styled-components';
import { ReactComponent as Search } from '../image/SearchIcon.svg';
import { useNavigate } from 'react-router-dom';

const SShareTop = styled.div`
  display: flex;
  flex-direction: row;
  margin-bottom: 50px;
  margin-top: 30px;
  justify-content: space-between;
  @media screen and (max-width: 930px) {
    flex-direction: column;
    align-items: center;
  }
  p {
    color: #212124;
  }
  .fs-23 {
    font-weight: 700;
    font-size: 22px;
    @media screen and (max-width: 930px) {
      text-align: center;
    }
    @media screen and (max-width: 768px) {
      font-size: 18px;
    }
  }
  .fs-16 {
    font-weight: 400;
    font-size: 16px;
    @media screen and (max-width: 930px) {
      text-align: center;
    }
    @media screen and (max-width: 768px) {
      font-size: 14px;
    }
  }
  .ml-5 {
    margin-left: 89px;
    @media screen and (max-width: 930px) {
      /* margin-left: 6%; */
      margin-left: 0px;
    }
  }
  .mb-5 {
    margin-bottom: 5px;
  }
  .search {
    width: 288px;
    height: 41px;
    box-sizing: border-box;
    margin-top: 22px;
    padding-left: 7px;
    font-size: 20px;
    background: #ffffff;
    border: 1px solid #aaaaaa;
    border-radius: 6px;
    @media screen and (max-width: 1023px) {
      width: 200px;
      /* margin-left: 20%; */
    }
  }
  .search-icon {
    transform: translate(-35px, 5px);
  }
  .searchBox {
    @media screen and (max-width: 1023px) {
      margin-right: 20px;
    }
  }
  select {
    margin-right: 10px;
    -moz-appearance: none;
    -webkit-appearance: none;
    appearance: none;

    padding: 7px;
    font-size: 1rem;
    border: 1px solid #aaaaaa;
    border-radius: 6px;
    color: #666666;
    text-align: center;
    @media screen and (max-width: 768px) {
      font-size: 0.8rem;
    }
  }
`;

const SRegister = styled.button`
  width: 106px;
  height: 41px;
  margin-right: 89px;
  border-radius: 6px;
  border: 1.5px solid #bb2649;
  color: #bb2649;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 400;
  font-size: 18px;
  line-height: 24px;
  align-items: center;
  text-align: center;
  @media screen and (max-width: 1023px) {
    width: 80px;
    height: 41px;
    font-size: 15px;
    margin-right: 0;
    /* transform: translate(360%, -100%); */
  }
`;

const ListHigh = ({ page }) => {
  const navigate = useNavigate();
  const route = page === 'share' ? '/shareAdd' : '/reqAdd';

  return (
    <SShareTop>
      <div className="ml-5">
        <p className="fs-23 mb-5">
          {page === 'share'
            ? '현재 빌리지에 올라온 목록입니다!'
            : '빌리지 사람들이 찾고 있는 책이에요!'}
        </p>
        <p className="fs-16">
          {page === 'share'
            ? '찾고 있는 책이 있다면 연락해보세요!'
            : '내가 갖고 있는 책이라면 연락해보세요!'}
        </p>
      </div>
      <div className="searchBox">
        <select id="searchFilter">
          <option value="">--선택--</option>
          <option value="bookTitle">책 제목</option>
          <option value="content">내용</option>
        </select>
        <input className="search" />
        <Search className="search-icon" />
        <SRegister onClick={() => navigate(route)}>책 등록하기</SRegister>
      </div>
    </SShareTop>
  );
};

export default ListHigh;

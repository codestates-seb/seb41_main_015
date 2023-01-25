import styled from 'styled-components';
import { ReactComponent as Search } from '../image/SearchIcon.svg';
import { useNavigate } from 'react-router-dom';

const SShareTop = styled.div`
  display: flex;
  flex-direction: row;
  /* margin-bottom: 50px;
  margin-top: 30px; */
  color: #2c2c2c;
  padding: 18px;
  /* margin: 20px 10%; */
  border-bottom: 1px solid #acacac;
  justify-content: space-between;
  @media screen and (max-width: 1180px) {
    flex-direction: column;
    align-items: center;
  }
  p {
    color: #212124;
  }
  .fs-23 {
    font-weight: 700;
    font-size: 22px;
    @media screen and (max-width: 1180px) {
      text-align: center;
    }
    @media screen and (max-width: 768px) {
      font-size: 18px;
    }
  }
  .fs-16 {
    font-weight: 400;
    font-size: 16px;
    @media screen and (max-width: 1030px) {
      text-align: center;
    }
    @media screen and (max-width: 768px) {
      font-size: 14px;
    }
  }
  .ml-5 {
    margin-left: 0px;
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
    /* margin-top: 22px; */
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
    transform: translate(-35px, 0px);
  }
  .searchAndRegister {
    display: flex;
    flex-direction: row;
    margin-right: 0px;
    align-items: center;
    @media screen and (max-width: 1023px) {
      margin-right: 0px;
    }
    @media screen and (max-width: 543px) {
      flex-shrink: 0;
    }
  }
  .searchBox {
    display: flex;
    align-items: center;
    justify-content: center;
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
    height: 41px;
    color: #666666;
    text-align: center;
    @media screen and (max-width: 768px) {
      font-size: 0.8rem;
    }
  }
  .register {
    width: 100px;
    height: 40px;
    margin-right: 0px;
    /* margin-top: 22px; */
    border-radius: 3px;
    border: 1px solid #bb2649;
    color: #bb2649;
    font-size: 16px;
    /* line-height: 24px; */
    /* align-items: center;
  text-align: center; */
    @media screen and (max-width: 1023px) {
      width: 90px;
      height: 41px;
      margin-right: 0;
    }
  }
`;

const ListHigh = ({
  title,
  route,
  keyword,
  handleKeyword,
  handleSearch,
  handleOption,
}) => {
  const navigate = useNavigate();
  const path = route === 'share' ? '/shareAdd' : '/reqAdd';

  return (
    <SShareTop>
      <div className="ml-5">
        <p className="fs-23 mb-5">{title}</p>
        <p className="fs-16">
          {route === 'share'
            ? '찾고 있는 책이 있다면 연락해보세요!'
            : '내가 갖고 있는 책이라면 연락해보세요!'}
        </p>
      </div>
      <div className="searchAndRegister">
        <div className="searchBox">
          <select id="searchFilter" onChange={handleOption}>
            <option value="">--선택--</option>
            <option value="bookTitle">책 제목</option>
            <option value="author">저자</option>
            <option value="content">내용</option>
            <option value="displayName">작성자</option>
          </select>
          <input
            className="search"
            value={keyword}
            onChange={handleKeyword}
            onKeyDown={handleSearch}
          />
          <Search className="search-icon" />
        </div>
        <div>
          <button className="register" onClick={() => navigate(path)}>
            책 등록하기
          </button>
        </div>
      </div>
    </SShareTop>
  );
};

export default ListHigh;

import { useEffect, useState } from 'react';
import axios from 'axios';
import styled from 'styled-components';
import RateItems from '../components/RateItems';
import { useNavigate } from 'react-router-dom';
import Paging from '../components/Paging';

const StyledRateList = styled.div`
  margin: 0 190px;
  @media screen and (max-width: 1360px) {
    margin: 0px 50px;
  }

  .rateHeader {
    height: 146px;
    color: #2c2c2c;
    padding: 18px;
    border-bottom: 1px solid #acacac;
    display: flex;
    justify-content: space-between;
    align-items: center;
    @media screen and (max-width: 555px) {
      display: flex;
      flex-direction: column;
      font-size: 14px;
      height: 150px;
    }
    .title {
      display: flex;
      flex-direction: column;
      h2 {
        margin-bottom: 0;
        font-size: 22px;
      }
      p {
        @media screen and (max-width: 555px) {
          display: none;
        }
      }
    }
    .rateBtn {
      width: 100px;
      height: 38px;
      padding: 10px;
      border: 1px solid #bb2649;
      border-radius: 3px;
      color: #ffffff;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #cf385b;
      box-shadow: inset 0 1px 0 hsla(0, 0%, 100%, 0.7);
      font-weight: 500;

      :hover {
        color: #ffffff;
        background-color: #bb2649;
        border: 1px solid #bb2649;
      }
      @media screen and (max-width: 1023px) {
        font-size: 16px;
        width: 90px;
        height: 41px;
        padding: 0;
      }
    }
  }
`;

const RateList = (props) => {
  const [bookItems, setBookItems] = useState([]);
  const [page, setPage] = useState(1);
  const [count, setCount] = useState(0);
  const PER_PAGE = 15;

  const navigate = useNavigate();
  const url = 'https://serverbookvillage.kro.kr/';

  useEffect(() => {
    axios
      .get(url + `v1/books?page=0&size=${PER_PAGE}&sort=createdAt%2Cdesc`)
      .then((res) => {
        setBookItems(res.data.data);
        setCount(res.data.pageInfo.totalElements);
      })
      .catch((err) => {
        setCount(0);
        setBookItems([]);
        console.log(err);
      });
  }, []);

  const getDatabyPage = async (page) => {
    try {
      const res = await axios.get(
        url + `v1/books?page=${page - 1}&size=${PER_PAGE}&sort=createdAt%2Cdesc`
      );
      const data = res.data;
      return data;
    } catch (err) {
      console.error(err);
    }
  };

  const handlePageChange = async (page) => {
    setPage(page);
    const pageData = await getDatabyPage(page);
    setBookItems(pageData.data);
  };

  return (
    <StyledRateList>
      <div className="rateHeader">
        <div className="title">
          <h2>빌리지 사람들의 평점 목록입니다!</h2>
          <p>알고 있는 책에 자유롭게 평점을 매겨보세요!</p>
        </div>
        <div>
          <button className="rateBtn" onClick={() => navigate('/rateAdd')}>
            책 등록하기
          </button>
        </div>
      </div>
      <RateItems data={bookItems} />
      <Paging
        count={count}
        page={page}
        perPage={PER_PAGE}
        handlePageChange={handlePageChange}
      />
    </StyledRateList>
  );
};

export default RateList;

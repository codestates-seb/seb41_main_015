import { useEffect, useState } from 'react';
import instanceAxios from '../reissue/InstanceAxios';
import styled from 'styled-components';
import RateItems from '../components/RateItems';
import { useNavigate } from 'react-router-dom';

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
    .title {
      display: flex;
      flex-direction: column;
      h2 {
        margin-bottom: 0;
        font-size: 22px;
      }
    }
    .rateBtn {
      width: 100px;
      height: 38px;
      padding: 10px;
      border: 1px solid #bb2649;
      border-radius: 3px;
      color: #bb2649;
      display: flex;
      align-items: center;
      justify-content: center;
      :hover {
        color: #ffffff;
        background-color: #bb2649;
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
  const navigate = useNavigate();

  useEffect(() => {
    instanceAxios
      .get(`v1/books?page=0&size=5&sort=createdAt%2Cdesc`)
      .then((res) => {
        setBookItems(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  return (
    <StyledRateList>
      <div className="rateHeader">
        <div className="title">
          <h2>빌리지 사람들의 평점 목록입니다!</h2>
          <p>알고 있는 책에 자유롭게 평점을 매겨보세요!</p>
        </div>
        <button className="rateBtn" onClick={() => navigate('/rateAdd')}>
          책 등록하기
        </button>
      </div>
      <RateItems className="rateItems" data={bookItems} />
    </StyledRateList>
  );
};

export default RateList;

import React from 'react';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import styled from 'styled-components';
import { ReactComponent as BookStar } from '../image/bookStar.svg';
import RateModal from '../components/RateModal';
import RateComment from '../components/RateComment';

const SDetailLayout = styled.main`
  padding: 24px;
  min-height: calc(100vh - 60px - 280px);
  .container {
    max-width: 1280px;
    margin: 0 auto;
  }
`;
const STitle = styled.div`
  h2 {
    color: #2c2c2c;
    padding: 18px;
    font-weight: 700;
    font-size: 20px;
    margin: 20px 10%;
    border-bottom: 1px solid #acacac;
  }
`;
const SDetailWrap = styled.div`
  display: flex;
  justify-content: center;
  gap: 35px;
  img {
    margin: 24px 0;
    width: 250px;
  }
  @media screen and (max-width: 1100px) {
    flex-direction: column;
    align-items: center;
  }
`;

const SRightSide = styled.div`
  margin: 24px;
  width: calc(100% - 610px);
  .controlButtons {
    flex-shrink: 0;
    color: #aaaaaa;
    margin-right: 8px;
  }
  .betweenButtons {
    margin: 5px;
  }
  .controlButton {
    &:hover {
      color: #bb2649;
      cursor: pointer;
    }
  }
  .description {
    font-size: 1.05rem;
  }
  @media screen and (max-width: 1100px) {
    width: 100%;
    padding: 0px 24px;
  }
`;

const SBookInfo = styled.div`
  border-radius: 5px;
  padding: 10px 0;

  h2 {
    margin-top: 0;
    margin-bottom: 30px;
  }
  div {
    color: #000000;
    font-weight: 600;
    font-size: 1rem;
  }
  .mb-5 {
    margin-bottom: 5px;
  }
  .mb-10 {
    margin-bottom: 10px;
  }
  .textCor {
    color: #505050;
  }
  .flex {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }
`;

const SContact = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: center;
  div {
    font-weight: 600;
    color: #7c7c7c;
    font-size: 0.9rem;
  }
  .RateModalBtn {
    border: none;
    border-radius: 5px;
    background-color: #f9e000;
    padding: 12px;
    margin: 10px 5px 10px 0px;
    font-weight: 600;
    font-size: 0.9rem;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 5px;
    flex-shrink: 0;
  }
  .RateText {
    margin-right: 15px;
  }
`;
const SStarIcon = styled.div`
  text-align: right;
  display: flex;
  font-size: 30px;
  font-weight: 700;
  color: #6e6d6d;
  align-items: center;
  margin-bottom: 30px;
  .rateTitle {
    width: max-content;
  }
  .rateStar {
    margin: 0 2px;
  }
`;

const RateDetail = () => {
  const { id } = useParams();
  const [data, setData] = useState({});
  const [rateComment, serRateComment] = useState([]);

  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleOpenModal = () => {
    setIsModalOpen(true);
  };
  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  const url = 'https://serverbookvillage.kro.kr';

  useEffect(() => {
    window.scrollTo(0, 0);
    const rateData = async () => {
      try {
        const res = await axios.get(url + `/v1/books/${id}`);
        setData(res.data.data);
        //댓글 최신순 정렬
        const sortRateComments = res.data.data.rates;
        sortRateComments.sort(
          (a, b) => new Date(b.createdAt) - new Date(a.createdAt)
        );
        serRateComment(sortRateComments);
      } catch (error) {
        console.error(error);
      }
    };
    rateData();
  }, []);

  return (
    <SDetailLayout>
      <STitle>
        <h2>{data.bookTitle}</h2>
      </STitle>
      <div className="container">
        <SDetailWrap>
          <div>
            <img
              alt="책 표지"
              src={
                data.thumbnail ||
                'https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg'
              }
            />
          </div>
          <SRightSide>
            <SBookInfo>
              <div className="flex">
                <h2>{data.bookTitle}</h2>
                <SStarIcon>
                  <div className="rateTitle">평균</div>
                  <BookStar className="rateStar" />
                  {data.avgRate}
                </SStarIcon>
              </div>
              <div className="mb-5 textCor">
                저자: <span>{data.author || ''}</span>
              </div>
              <div className="mb-10 textCor">
                출판사: <span>{data.publisher || ''}</span>
              </div>

              <SContact>
                <div className="RateText">아직 평점을 남기지 않으셨나요?</div>
                <button className="RateModalBtn" onClick={handleOpenModal}>
                  평가 등록하러 가기
                </button>
                <RateModal
                  isModalOpen={isModalOpen}
                  handleCloseModal={handleCloseModal}
                  data={data}
                />
              </SContact>
            </SBookInfo>
          </SRightSide>
        </SDetailWrap>
        <RateComment data={rateComment} />
      </div>
    </SDetailLayout>
  );
};

export default RateDetail;

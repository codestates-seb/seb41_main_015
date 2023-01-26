import React from 'react';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import Swal from 'sweetalert2';
import Comment from '../components/Comment';
import styled from 'styled-components';
import { prettyDate } from '../util/dateparse';
import { ReactComponent as Star } from '../image/star.svg';
import RateModal from '../components/RateModal';

const SDetailLayout = styled.main`
  padding: 24px;
  min-height: calc(100vh - 60px - 280px);
  .container {
    max-width: 1280px;
    margin: 0 auto;
  }
`;
const STitle = styled.div`
  p {
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
    margin: 24px 24px 24px 0px;
    width: 250px;
  }
  @media screen and (max-width: 1100px) {
    flex-direction: column;
    align-items: center;
  }
`;

const SRightSide = styled.div`
  margin: 24px;
  /* border: 1px solid green; */
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

const STopWrap = styled.div`
  padding-bottom: 5px;
  border-bottom: 1px solid #aaaaaa;
  /* max-width: 677px; */
  h1 {
    margin: 10px 0px;
  }
  .titleAndButton {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 40px;
  }
`;

const SAuthorAndStatus = styled.div`
  display: flex;
  justify-content: space-between;
  /* align-items: center; */
  margin-bottom: 10px;
  .authorInfo {
    display: flex;
    align-items: center;
    gap: 10px;
    padding-left: 3px;
    .author {
      display: flex;
      align-items: center;
      gap: 5px;
    }
    img {
      margin: 0;
      width: 28px;
      height: 28px;
      border-radius: 70%;
    }
    .views {
      color: #aaaaaa;
      font-size: 0.8rem;
      display: flex;
      align-items: center;
      gap: 3px;
    }
    .createdAt {
      color: #aaaaaa;
      font-size: 0.8rem;
      @media screen and (max-width: 527px) {
        display: none;
      }
    }
  }
  .onlyInShare {
    display: none;
  }
`;

const SBookInfo = styled.div`
  margin-top: 20px;
  margin-bottom: 40px;
  /* border: 1px solid #bb2649; */
  background-color: #fffbeac2;
  border-radius: 5px;
  /* border-bottom: 1px solid #aaaaaa; */
  padding: 24px 12px 5px 24px;
  h2 {
    margin-top: 0;
  }
  div {
    color: #000000;
    /* margin: 5px 0px; */
    font-weight: 600;
    font-size: 1rem;
    span {
      color: #505050;
    }
  }
`;

const SContact = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: center;
  div {
    font-weight: 600;
    /* margin-right: 15px; */
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
`;
const SStarIcon = styled.div`
  text-align: right;
`;

const RateDetail = () => {
  const { id } = useParams();
  // console.log(id);
  const [data, setData] = useState({});

  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleOpenModal = () => {
    setIsModalOpen(true);
  };
  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  const url = 'https://serverbookvillage.kro.kr';

  useEffect(() => {
    const rateData = async () => {
      try {
        const res = await axios.get(url + `/v1/books/${id}`);
        setData(res.data.data);
        console.log('getData', res.data.data);
      } catch (error) {
        console.error(error);
      }
    };
    rateData();
  }, []);

  return (
    <SDetailLayout>
      <STitle>
        <p>{data.bookTitle}</p>
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
            <STopWrap>
              <div className="titleAndButton">
                <h1>{data.title}</h1>
              </div>
              <div className="createdAt">{prettyDate(data.createdAt)}</div>
              <SAuthorAndStatus></SAuthorAndStatus>
            </STopWrap>
            <SStarIcon>
              <Star />
              {data.avgRate}
            </SStarIcon>
            <SBookInfo>
              <h2>{data.bookTitle}</h2>
              <div>
                저자: <span>{data.author}</span>
              </div>
              <div>
                출판사: <span>{data.publisher}</span>
              </div>
              <SContact>
                <div>{data.bookTitle} 리뷰하러 가기 ➡️</div>
                <button
                  className="RateModalBtn"
                  //모달
                  onClick={handleOpenModal}
                >
                  평가 등록하러 가기
                </button>
                <RateModal
                  isModalOpen={isModalOpen}
                  handleCloseModal={handleCloseModal}
                  data={data}
                />
              </SContact>
            </SBookInfo>
            <p className="description">{data.content}</p>
          </SRightSide>
        </SDetailWrap>
      </div>
    </SDetailLayout>
  );
};

export default RateDetail;

import styled from 'styled-components';
import { useEffect, useState } from 'react';
import { ReactComponent as BookStar } from '../image/bookStar.svg';
import bookAd from '../image/bookAd.png';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import eventB from '../image/eventB.png';

const StyledRanking = styled.div`
  .bookAd {
    width: 80%;
    margin: 5% 10%;
  }
  .eventB {
    width: 60%;
    margin: 3% 20%;
  }
`;

const SRateContainer = styled.div`
  padding: 20px 5%;
  .rankingBox {
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
    text-align: center;
    color: #3d3d3d;
    font-weight: 500;
  }
  .rankingItem {
    padding: 10px;
    :hover {
      cursor: pointer;
    }

    .bookIndex {
      width: 23px;
      height: 23px;
      color: #ffffff;
      background-color: #434343;
      position: absolute;
      border-radius: 5px;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .thumbnail {
      width: 200px;
    }
    .bookTitle {
      width: 180px;
      display: flex;
      text-align: center;
      justify-content: center;
      margin: 5px auto;
    }
    .bookDes {
      font-size: 12px;
      color: #595959;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0;
    }
  }
`;

const SContainer = styled.div`
  display: flex;
  justify-content: center;
  margin: 30px;
  @media screen and (max-width: 800px) {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  h3 {
    padding: 10px;
    border-bottom: 1px solid #aaaaaa;
    margin: 10px;
  }
  .rankingBox {
    padding: 5px 44px;
    color: #3d3d3d;
  }
  .bookDes {
    padding-bottom: 13px;
  }

  .bookNumber {
    font-size: 18px;
    font-weight: 800;
    padding-right: 6px;
  }

  .leftConntainer {
    margin-right: 7%;
    width: 500px;
    @media screen and (max-width: 800px) {
      margin: 0 0 50px;
    }
  }
  .rightConntainer {
    width: 500px;
  }
`;

const Ranking = () => {
  const [bookStarRank, setBookStarRank] = useState([]);
  const [bookReqRank, setBookReqRank] = useState([]);
  const [bookBorRank, setBookBorRank] = useState([]);
  const url = 'https://serverbookvillage.kro.kr/';
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get(url + `v1/books?page=0&size=5&sort=avgRate%2Cdesc`)
      .then((res) => {
        setBookStarRank(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
    axios
      .get(url + `v1/requests/rank`)
      .then((res) => {
        setBookReqRank(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
    axios
      .get(url + `v1/borrows/rank`)
      .then((res) => {
        setBookBorRank(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  return (
    <StyledRanking>
      <SRateContainer>
        <h3>별점 랭킹</h3>
        <div className="rankingBox">
          {bookStarRank.map((item, index) => (
            <div
              className="rankingItem"
              item={item}
              key={index}
              onClick={() => navigate(`rateDetail/${item.bookId}`)}
            >
              <div className="bookIndex">{index + 1}</div>
              <img src={item.thumbnail} alt="img" className="thumbnail" />
              <p className="bookTitle">{item.bookTitle}</p>
              <span className="bookDes">
                {item.author}
                <BookStar className="star" />
                {item.avgRate}
              </span>
            </div>
          ))}
        </div>
      </SRateContainer>

      <img src={bookAd} alt="bookAd" className="bookAd" />

      <SContainer>
        <div className="leftConntainer">
          <h3> ⚡ 빌리지 사람들이 많이 나눔하는 책</h3>
          <div className="rankingBox">
            {bookBorRank.map((item, index) => (
              <div className="rankingItem" item={item} key={index}>
                <div className="bookDes">
                  <span className="bookNumber">{index + 1}. </span>
                  <span className="bookTitle">
                    {item.bookTitle} · {item.author}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="rightConntainer">
          <h3> ⚡ 빌리지 사람들이 많이 요청하는 책 </h3>
          <div className="rankingBox">
            {bookReqRank.map((item, index) => (
              <div className="rankingItem" item={item} key={index}>
                <div className="bookDes">
                  <span className="bookNumber">{index + 1}. </span>
                  <span className="bookTitle">
                    {item.bookTitle} · {item.author}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </SContainer>

      <img src={eventB} alt="bookAd" className="eventB" />
    </StyledRanking>
  );
};

export default Ranking;

import styled from 'styled-components';
import { ReactComponent as BookStar } from '../image/bookStar.svg';
import { useNavigate } from 'react-router-dom';

const StyledRateItems = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 30px;
  margin-top: 30px;
  justify-items: center;
  justify-content: center;

  .rateContainer {
    display: flex;
    cursor: pointer;
    flex-direction: column;
    margin: 0 20px 0 10px;
    color: #3d3d3d;
    font-weight: 500;

    img {
      width: 150px;
    }

    .bookInfo {
      display: flex;
      flex-direction: column;
      align-content: center;
      width: 150px;
    }
    p,
    span {
      display: flex;
      text-align: center;
      justify-content: center;
      margin: 4px auto 0;
      span {
        margin: 0;
      }
    }
    .bookStar {
      font-size: 15px;
      color: #595959;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0;
    }
  }
`;
const RateItems = (data) => {
  const navigate = useNavigate();

  return (
    <StyledRateItems>
      {data &&
        data.data.map((item, index) => (
          <div
            className="rateContainer"
            key={index}
            item={item}
            onClick={() => navigate(`/rateDetail/${item.bookId}`)}
          >
            {item.thumbnail === '' ? (
              <img
                src="https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg"
                alt="img"
              />
            ) : (
              <img src={item.thumbnail} alt="img" />
            )}
            <div className="bookInfo">
              <p>{item.bookTitle}</p>
              <span className="bookStar">
                <BookStar />
                {item.avgRate}
              </span>
            </div>
          </div>
        ))}
    </StyledRateItems>
  );
};

export default RateItems;

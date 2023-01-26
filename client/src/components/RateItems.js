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
    flex-direction: column;
    margin: 0 20px 0 10px;

    img {
      width: 150px;
    }

    .bookInfo {
      p,
      span {
        display: flex;
        justify-content: center;
        align-items: center;
        margin: 3px 0;
      }
      span {
        margin: 0;
      }
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
            <img src={item.thumbnail} alt="img" />
            <div className="bookInfo">
              <p>{item.bookTitle}</p>
              <span className="bookStar">
                <BookStar className="bookStar" />
                {item.avgRate}
              </span>
            </div>
          </div>
        ))}
    </StyledRateItems>
  );
};

export default RateItems;

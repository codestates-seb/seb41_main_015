import styled from 'styled-components';
import { useNavigate, Link } from 'react-router-dom';

const SBookContainer = styled.li`
  display: flex;
  flex-direction: column;
  border: 1.5px solid #dddada;
  width: 40%;
  height: 100%;
  padding: 0 18px;
  margin: 1rem;

  @media screen and (max-width: 1023px) {
    display: flex;
    flex-direction: column;
    width: auto;
    /* padding: 0; */
  }
  .coverBox {
    margin-right: 1rem;
    height: 207px;
    float: left;
  }
  .bookCover {
    height: 180px;
    width: 140px;
    &:hover {
      cursor: pointer;
    }
  }
  .informationBox {
    margin-left: 0;
    margin-right: 1rem;
    color: #212124;
  }
  .fs-18 {
    font-size: 18px;
    font-weight: 700;
    color: #212124;
    @media screen and (max-width: 1023px) {
      font-size: 16px;
    }
  }
  .mfs-16 {
    @media screen and (max-width: 1023px) {
      font-size: 15px;
    }
  }
  .fs-12 {
    font-size: 12px;
  }
  .word-break {
    display: -webkit-box;
    word-break: break-word;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
  .item-flex {
    display: flex;
  }
  .mb-15 {
    margin-bottom: 15px;
  }
  .createdAt-r {
    text-align: right;
    margin: auto;
  }
  .mt-20 {
    margin-top: 0px;
    height: 100%;
  }
  .shareTitle {
    margin: 20px 0px;
    font-size: 20px;
    font-weight: 700;
    text-align: start;
    color: #212124;
    &:hover {
      color: #bb2649;
      cursor: pointer;
    }
    @media all and (min-width: 480px) and (max-width: 1023px) {
      margin-left: 3%;
    }
  }
  .f-row {
    flex-direction: column;
  }
`;

const SBookList = styled.ol`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  /* float: left; */
  width: 100%;
  margin: 0 auto;
  padding-inline-start: 0px;
`;

const BookList = ({ data, page }) => {
  const navigate = useNavigate();
  const route = page === 'share' ? '/shareDetail' : '/reqDetail';

  return (
    <SBookList>
      {data.map((book) => (
        <SBookContainer key={book.id}>
          <Link to={`${route}/${book.id}`}>
            <div className="shareTitle">{book.title}</div>
          </Link>
          <div className="f-row">
            <div className="coverBox">
              <img
                className="bookCover"
                src={book.image}
                alt="bookCover"
                onClick={() => navigate(`${route}/${book.id}`)}
              />
            </div>
            <div className="informationBox">
              <p className="fs-18 mb-15">{book.bookTitle}</p>
              <div className="item-flex">
                <p className="mfs-16">{book.writer} 저자 /</p>
                <p className="mfs-16">{book.publisher}</p>
              </div>
              <p className="fs-12 createdAt-r">{book.createdAt}</p>
            </div>
          </div>
          <p className="word-break mt-20 mfs-16">{book.contents}</p>
        </SBookContainer>
      ))}
    </SBookList>
  );
};

export default BookList;

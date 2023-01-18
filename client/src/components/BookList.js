import styled from 'styled-components';
import { useNavigate, Link } from 'react-router-dom';

const SBookContainer = styled.li`
  display: flex;
  flex-grow: 1;
  flex-direction: column;
  border: 1.5px solid #dddada;
  width: 100%;
  height: 100%;
  padding: 0 18px;
  /* margin: 0.8rem; */

  @media screen and (max-width: 1080px) {
    /* display: flex;
    flex-direction: column;
    width: auto; */
    /* padding: 0; */
  }
  .coverBox {
    margin-right: 1rem;
    height: 207px;
    float: left;
  }
  .bookCover {
    height: 11rem;
    width: 8.7rem;
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
    @media screen and (max-width: 1080px) {
      font-size: 16px;
    }
  }
  .mfs-16 {
    @media screen and (max-width: 1080px) {
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
    @media all and (min-width: 480px) and (max-width: 1080px) {
      margin-left: 0px;
    }
  }
  .f-row {
    flex-direction: column;
  }
`;

const SBookList = styled.ol`
  display: grid;
  grid-template-rows: repeat(auto-fill, 1fr);
  /* grid-template-rows: repeat(6, 1fr); */
  grid-template-columns: repeat(2, minmax(446px, 1fr));
  gap: 30px;
  padding: 10px;
  margin: 20px 10%;
  @media screen and (max-width: 1080px) {
    grid-template-columns: repeat(1, minmax(446px, 1fr));
  }
`;

const BookList = ({ data, route }) => {
  const navigate = useNavigate();
  const path = route === 'share' ? '/shareDetail' : '/reqDetail';

  return (
    <SBookList>
      {data.map((article) => {
        // 기본 이미지
        const imgSrc = article.imgUrl
          ? article.imgUrl
          : 'https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg';

        // 아이디
        const id = route === 'share' ? article.borrowId : article.requestId;

        return (
          <SBookContainer key={id}>
            <Link to={`${path}/${id}`}>
              <div className="shareTitle">{article.title}</div>
            </Link>
            <div className="f-row">
              <div className="coverBox">
                <img
                  className="bookCover"
                  src={imgSrc}
                  alt="bookCover"
                  onClick={() => navigate(`${path}/${id}`)}
                />
              </div>
              <div className="informationBox">
                <p className="fs-18 mb-15">{article.bookTitle}</p>
                <div className="item-flex">
                  <p className="mfs-16">{article.author} 저자 /</p>
                  <p className="mfs-16">{article.publisher}</p>
                </div>
                <p className="fs-12 createdAt-r">{article.createdAt}</p>
              </div>
            </div>
            <p className="word-break mt-20 mfs-16">{article.content}</p>
          </SBookContainer>
        );
      })}
    </SBookList>
  );
};

export default BookList;

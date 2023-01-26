import styled from 'styled-components';
import ShareStatus from './ShareStatus';
import { ReactComponent as Eye } from '../image/eye.svg';
import { useNavigate, Link } from 'react-router-dom';
import { elapsed } from '../util/dateparse';

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
    /* margin-right: 1rem; */
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
    @media screen and (max-width: 500px) {
      flex-direction: column;
      margin-bottom: 25px;
      p {
        margin: 2px 0px;
      }
    }
  }
  .mb-15 {
    margin-bottom: 15px;
  }
  .createdAt-r {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    justify-content: center;
    gap: 5px;
    margin: 0px;
  }
  #displayName {
    display: flex;
    align-items: center;
    font-size: 0.9rem;
    margin-bottom: 2px;
    img {
      width: 20px;
      height: 20px;
      margin-right: 5px;
      border-radius: 70%;
    }
  }
  #articleInfo {
    display: flex;
    gap: 10px;
    font-size: 0.8rem;
    color: #aaaaaa;
  }
  #views {
    display: flex;
    align-items: center;
    gap: 3px;
    font-size: 0.8rem;
    color: #aaaaaa;
  }
  .mt-20 {
    margin-top: 0px;
    height: 100%;
  }
  .shareTitle {
    display: flex;
    justify-content: space-between;
    margin: 20px 0px;
    font-size: 20px;
    font-weight: 700;
    text-align: start;
    color: #212124;
    @media all and (min-width: 480px) and (max-width: 1080px) {
      margin-left: 0px;
    }
  }
  .onlyInShare {
    display: none;
  }
  #title {
    &:hover {
      color: #bb2649;
      cursor: pointer;
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
  padding: 0px 10px;
  @media screen and (max-width: 1080px) {
    grid-template-columns: repeat(1, minmax(300px, 1fr));
  }
  @media screen and (max-width: 500px) {
    padding: 0px;
  }
`;

const BookList = ({ data, route }) => {
  const navigate = useNavigate();
  const path = route === 'share' ? '/shareDetail' : '/reqDetail';
  const onlyInShare = route === 'share' ? '' : 'onlyInShare';

  return (
    <SBookList>
      {data.map((article, index) => {
        // 책 표지 기본 이미지
        const cover = article.thumbnail
          ? article.thumbnail
          : 'https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg';
        // 프로필 사진 기본 이미지
        const profile = article.imgUrl
          ? article.imgUrl
          : 'https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/309/59932b0eb046f9fa3e063b8875032edd_crop.jpeg';
        // 아이디
        const id = route === 'share' ? article.borrowId : article.requestId;
        // 상태
        const status = route === 'share' ? article.borrowWhthr : null;

        return (
          <SBookContainer key={index}>
            <div className="shareTitle">
              <Link to={`${path}/${id}`}>
                <span id="title">{article.title}</span>
              </Link>
              <div className={onlyInShare}>
                <ShareStatus status={status} />
              </div>
            </div>
            <div className="f-row">
              <div className="coverBox">
                <img
                  className="bookCover"
                  src={cover}
                  alt="bookCover"
                  onClick={() => navigate(`${path}/${id}`)}
                />
              </div>
              <div className="informationBox">
                <p className="fs-18 mb-15">{article.bookTitle}</p>
                <div className="item-flex">
                  <p className="mfs-16">{article.author}/</p>
                  <p className="mfs-16">{article.publisher}</p>
                </div>
                <div className="createdAt-r">
                  <div id="displayName">
                    <img src={profile} alt="프로필사진" />
                    {article.displayName}
                  </div>
                  <div id="articleInfo">
                    <div id="createdAt">{elapsed(article.createdAt)}</div>
                    <div id="views">
                      <Eye width="14px" height="14px" />
                      {article.view}
                    </div>
                  </div>
                </div>
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

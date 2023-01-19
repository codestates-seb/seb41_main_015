import { Link } from 'react-router-dom';
import styled from 'styled-components';
import { ReactComponent as KakaoFill } from '../image/kakaofill.svg';

const SDetailLayout = styled.main`
  padding: 24px;
  min-height: calc(100vh - 60px - 280px);

  .container {
    max-width: 1280px;
    margin: 0 auto;
  }
`;

const SDetailWrap = styled.div`
  display: flex;
  justify-content: center;
  gap: 35px;

  img {
    margin: 24px 24px 24px 0px;
    width: 350px;
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

  .authorInfo {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
    padding-left: 3px;

    img {
      margin: 0;
      width: 28px;
      border-radius: 70%;
    }

    .createdAt {
      color: #aaaaaa;
      font-size: 0.9rem;
    }
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
    margin: 5px 0px;
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
    margin-right: 15px;
    color: #7c7c7c;
    font-size: 0.9rem;
  }

  button {
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

const DetailForm = ({ data, page }) => {
  // 자기가 쓴 글이 아니면 수정, 삭제 버튼이 안 보여야 함

  // 삭제 버튼 핸들러
  const handleDelete = () => {
    // 서버에 삭제 요청 보내기 (instanceAxios 쓰기)
    console.log('삭제합니다!');
  };

  // 책 표지 기본이미지
  const imgUrl = data.imgUrl
    ? data.imgUrl
    : 'https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg';
  const id = page === 'share' ? data.borrowId : data.requestId;
  return (
    <SDetailLayout>
      <div className="container">
        <SDetailWrap>
          <div>
            <img alt="책 표지" src={imgUrl} />
          </div>
          <SRightSide>
            <STopWrap>
              <div className="titleAndButton">
                <h1>{data.title}</h1>
                {/* 수정, 삭제 버튼은 자기가 쓴 글에서만 보이도록 */}
                <div className="controlButtons">
                  <Link
                    to={
                      page === 'request' ? `/reqEdit/${id}` : `/shareEdit/${id}`
                    }
                  >
                    <span className="controlButton">수정</span>
                  </Link>
                  <span className="betweenButtons">|</span>
                  <span className="controlButton" onClick={handleDelete}>
                    삭제
                  </span>
                </div>
              </div>
              <div className="authorInfo">
                <img
                  alt="profileImage"
                  src="https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/309/59932b0eb046f9fa3e063b8875032edd_crop.jpeg"
                />
                <div>{data.displayName}</div>
                <div className="createdAt">{data.createdAt}</div>
              </div>
            </STopWrap>
            <SBookInfo>
              <h2>{data.bookTitle}</h2>
              <div>
                저자: <span>{data.author}</span>
              </div>
              <div>
                출판사: <span>{data.publisher}</span>
              </div>
              <SContact>
                <div>
                  {page === 'request'
                    ? '이 책을 갖고 계신가요?'
                    : '이 책을 찾고 계셨나요?'}
                </div>
                <button
                  onClick={() => {
                    window.open(data.talkUrl);
                  }}
                >
                  <KakaoFill width="16" height="16" />
                  오픈채팅으로 연락하기
                </button>
              </SContact>
            </SBookInfo>
            <p className="description">{data.content}</p>
          </SRightSide>
        </SDetailWrap>
      </div>
    </SDetailLayout>
  );
};

export default DetailForm;

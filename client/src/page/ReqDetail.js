import styled from 'styled-components';
import { ReactComponent as KakaoFill } from '../image/kakaofill.svg';

const SDetailLayout = styled.main`
  padding: 24px;
`;

const SDetailWrap = styled.div`
  display: flex;
  justify-content: center;
  gap: 35px;

  img {
    margin: 24px;
    width: 350px;
    flex-shrink: 0;
  }
`;

const SRightSide = styled.div`
  margin: 24px;
  min-width: calc(100% - 700px);
  /* border: 1px solid green; */

  .controlButtons {
    flex-shrink: 0;
    color: #aaaaaa;
  }

  .betweenButtons {
    margin: 5px;
  }

  .editButton {
    &:hover {
      color: #bb2649;
      cursor: pointer;
    }
  }
`;

const STopWrap = styled.div`
  border-bottom: 1px solid #aaaaaa;

  h1 {
    margin: 10px 0px;
  }

  .titleAndButton {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 40px;
    max-width: 677px;
  }

  .authorInfo {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;

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
  margin-top: 10px;
  border-radius: 5px;
  border-bottom: 1px solid #aaaaaa;
  padding-bottom: 10px;
  margin-bottom: 10px;

  div {
    color: #000000;
    margin: 5px 0px;
    font-weight: 600;
    font-size: 1.1rem;
    span {
      color: #7c7c7c;
    }
  }
`;

const SContact = styled.div`
  display: flex;
  align-items: center;

  button {
    border: none;
    border-radius: 70%;
    background-color: #f9e000;
    padding: 7px;
    margin: 10px 5px 10px 0px;

    display: flex;
    justify-content: center;
    align-items: center;
  }
`;

const ReqDetail = () => {
  // 제목, 저자, 출판사, 오픈채팅, 본문, 작성자 닉네임
  const dummyData = {
    title: '책 찾습니다',
    bookTitle: '모던 자바스크립트 Deep Dive',
    bookAuthor: '이웅모',
    bookPublisher: '위키북스',
    imgUrl:
      'https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791158392239.jpg',
    content: '갖고 계신 분 연락 부탁드립니다!',
  };

  return (
    <SDetailLayout>
      <SDetailWrap>
        <img alt="책 표지" src={dummyData.imgUrl} />
        <SRightSide>
          <STopWrap>
            <div className="titleAndButton">
              <h1>{dummyData.title}</h1>
              {/* 수정, 삭제 버튼은 자기가 쓴 글에서만 보이도록 */}
              <div className="controlButtons">
                <span className="editButton">수정</span>
                <span className="betweenButtons">|</span>
                <span clasSName="deleteButton">삭제</span>
              </div>
            </div>
            <div className="authorInfo">
              <img
                alt="profileImage"
                src="https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/309/59932b0eb046f9fa3e063b8875032edd_crop.jpeg"
              />
              <div>닉네임</div>
              <div className="createdAt">작성 날짜 회색으로</div>
            </div>
          </STopWrap>
          <SBookInfo>
            <h2>{dummyData.bookTitle}</h2>
            <div>
              저자: <span>{dummyData.bookAuthor}</span>
            </div>
            <div>
              출판사: <span>{dummyData.bookPublisher}</span>
            </div>
          </SBookInfo>
          <SContact>
            <button>
              <KakaoFill width="14" height="14" />
            </button>
            오픈채팅으로 연락하기
          </SContact>
          <div className="description">{dummyData.content}</div>
        </SRightSide>
      </SDetailWrap>
    </SDetailLayout>
  );
};

export default ReqDetail;

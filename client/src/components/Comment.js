import styled from 'styled-components';

const SCommentForm = styled.div`
  margin: 30px 0;
`;
const SInputContainer = styled.div`
  display: flex;
  justify-content: center;
  margin: 30px 0;
  .InputComment {
    width: 50%;
    height: 40px;
    border: 1px solid #aaaaaa;
    border-radius: 5px;
    padding-left: 10px;
  }
  .SunmbitComment {
    margin-left: 30px;
    width: 100px;
    color: #ffffff;
    border: 1px solid #bb2649;
    border-radius: 5px;
    background-color: #bb2649;
  }
`;
const SCommentContainer = styled.div`
  color: #212529;
  display: flex;
  flex-direction: column;
  padding-bottom: 24px;
  margin: 0 20%;
  border-bottom: 1px solid #ececec;
`;

const SUserContainer = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 24px;
  img {
    margin: 0;
    width: 43px;
    border-radius: 70%;
    margin-right: 10px;
  }
  .userName {
    font-size: 16px;
    font-weight: 700;
    align-items: center;
  }
`;
const SUserComment = styled.div`
  .content {
    line-height: 1.6;
    font-size: 15px;
  }
  .btnBox {
    margin-top: 24px;
    float: right;
    color: #aaaaaa;
    font-size: 13px;
  }
  button {
    color: #aaaaaa;
    border: none;
    padding: 0 5px;
    :hover {
      color: #bb2649;
    }
  }
`;

const Comment = () => {
  return (
    <SCommentForm>
      <SInputContainer>
        <input className="InputComment" placeholder="댓글을 남겨보세요" />
        <button className="SunmbitComment" type="sumbit">
          등록
        </button>
      </SInputContainer>
      <SCommentContainer>
        <SUserContainer>
          <img
            alt="profileImage"
            src="https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/309/59932b0eb046f9fa3e063b8875032edd_crop.jpeg"
          />
          <span className="userName">user</span>
        </SUserContainer>
        <SUserComment>
          <div className="content">
            C++만 사용하다 급하게 JavaScript를 사용해야 할 일이 있어 학습 용으로
            어떤 책을 살지 좀 찾아보았는데요. 최신 자바스크립트 문법도 전부
            설명해주는 것도 좋네요. 다른 언어하던 사람이 JavaScript 배운다고
            하면 이 책을 추천해줄 듯 합니다.
          </div>
          <div className="btnBox">
            <button>수정</button>|<button>삭제</button>
          </div>
        </SUserComment>
      </SCommentContainer>
    </SCommentForm>
  );
};

export default Comment;

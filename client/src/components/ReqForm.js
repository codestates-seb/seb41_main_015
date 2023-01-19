import styled from 'styled-components';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BookAddModal from './BookAddModal';

const StyledReqForm = styled.div`
  h2 {
    color: #2c2c2c;
    padding: 18px;
    margin: 20px 10%;
    border-bottom: 1px solid #acacac;
  }
`;
const SInputContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin: 30px 11% 10px 14%;
  font-size: 13px;
  #ImgInput {
    display: none;
  }
  @media screen and (max-width: 930px) {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 0;
  }
`;

const SInputLeft = styled.div`
  margin-top: 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
  .bookImg {
    width: 230px;
    margin-bottom: 30px;

    @media screen and (max-width: 930px) {
      width: 160px;
    }
  }
`;

const SImgBtn = styled.div`
  display: flex;
  text-align: center;
  justify-content: center;
  margin-bottom: 20px;
  .bookImg {
    width: 230px;
    height: 297.156px;
    margin-bottom: 30px;
    @media screen and (max-width: 930px) {
      width: 160px;
      height: 206.712px;
    }
  }
`;

const SInputRight = styled.div`
  width: 65%;
  display: flex;
  flex-direction: column;
  margin-top: 10px;
  input {
    width: 95%;
    height: 40px;
    margin-bottom: 20px;
    border: none;
    border-radius: 2px;
    background-color: #f4f4f4;
    padding-left: 10px;
    :focus {
      outline: none;
      border-bottom: 2px solid #4f4f4f;
      /* background-color: #e8f0ff; */
    }
  }
  .inputContent {
    width: 95%;
    height: 220px;
    border: none;
    /* border-bottom: 1px solid #aaaaaa; */
    margin-bottom: 30px;
    padding: 10px;
  }
`;

const SButtonBox = styled.div`
  display: flex;
  justify-content: center;
  margin-bottom: 70px;
  button {
    width: 15%;
    height: 35px;
    border: 1px solid #bb2649;
    border-radius: 5px;
  }
  .cancelBtn {
    color: #bb2649;
    background-color: #ffffff;
    border: 1px solid #bb2649;
    margin-right: 5%;
    :hover {
    }
  }
  .submitBtn {
    color: #ffffff;
    background-color: #bb2649;
    :hover {
    }
  }
`;

const ReqForm = (props) => {
  const navigate = useNavigate();
  const goBack = () => {
    navigate(-1);
  };

  const { inputs, onBookInfoChange } = props;
  const { bookTitle, author, publisher, talkUrl, title, content, thumbnail } =
    inputs;

  const handleChangeString = (e, type) => {
    onBookInfoChange({ ...inputs, [`${type}`]: e.target.value });
    console.log(e.target.value);
  };

  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleOpenModal = () => {
    setIsModalOpen(true);
  };
  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  return (
    <StyledReqForm>
      <h2>{props.page === 'reqAdd' ? '요청하기' : '수정하기'}</h2>
      <SInputContainer>
        <SInputLeft>
          <SImgBtn>
            <img
              alt="bookImg"
              src={thumbnail}
              className="bookImg"
              onChange={(e) => handleChangeString(e, thumbnail)}
            />
          </SImgBtn>
        </SInputLeft>
        <SInputRight>
          <div>
            <input
              name="bookTitle"
              value={bookTitle || ''}
              onChange={(e) => handleChangeString(e, bookTitle)}
              placeholder="책 제목을 입력해주세요."
              onClick={handleOpenModal}
            />
            {/* 검색 모달  */}
            <BookAddModal
              isModalOpen={isModalOpen}
              onBookInfoChange={onBookInfoChange}
              handleCloseModal={handleCloseModal}
            />
          </div>
          <div>
            <input
              name="author"
              value={author || ''}
              onChange={(e) => handleChangeString(e, author)}
              placeholder="저자를 입력해주세요."
              disabled
            />
          </div>
          <div>
            <input
              name="publisher"
              value={publisher || ''}
              onChange={(e) => handleChangeString(e, publisher)}
              placeholder="출판사를 입력해주세요."
              disabled
            />
          </div>
          <div>
            <input
              name="talkUrl"
              value={talkUrl || ''}
              onChange={(e) => handleChangeString(e, 'talkUrl')}
              placeholder="오픈채팅 대화방 링크를 입력해주세요."
            />
          </div>
          <div>
            <input
              name="title"
              value={title || ''}
              onChange={(e) => handleChangeString(e, 'title')}
              placeholder="게시글 제목을 입력해주세요."
            />
          </div>
          <div>
            <input
              name="content"
              value={content || ''}
              onChange={(e) => handleChangeString(e, 'content')}
              className="inputContent"
              placeholder="게시글 내용을 입력해주세요. (ex. 책 상태, 구매 시기 등)"
            />
          </div>
        </SInputRight>
      </SInputContainer>
      <SButtonBox>
        <button className="cancelBtn" onClick={goBack}>
          취소
        </button>
        <button className="submitBtn" onClick={props.editBtn}>
          {props.page === 'reqAdd' ? '요청' : '수정'}
        </button>
      </SButtonBox>
    </StyledReqForm>
  );
};

export default ReqForm;

import styled from 'styled-components';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BookAddModal from './BookAddModal';

const StyledShareForm = styled.div`
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
  .bookImg {
    width: 230px;
    margin-bottom: 30px;
    @media screen and (max-width: 930px) {
      width: 200px;
    }
  }
`;

const SImgBtn = styled.div`
  display: flex;
  text-align: center;
  justify-content: center;
  margin-bottom: 20px;
  .ImgInputBtn {
    width: 100px;
    height: 30px;
    margin-right: 5px;
    border: 1px solid #d0c9c0;
    border-radius: 4px;
    background-color: #d0c9c0;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 700;
    :hover {
      cursor: pointer;
    }
  }
  .imgDelete {
    width: 100px;
    height: 30px;
    border: 1px solid #d0c9c0;
    border-radius: 4px;
    background-color: #d0c9c0;
    text-align: center;
    font-weight: 700;
    :hover {
      cursor: pointer;
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
    border: 1px solid #aaaaaa;
    border-radius: 4px;
    padding-left: 10px;
  }
  .inputContent {
    width: 95%;
    height: 150px;
    border: 1px solid #aaaaaa;
    border-radius: 4px;
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

const ShareForm = (props) => {
  const navigate = useNavigate();
  const goBack = () => {
    navigate(-1);
  };

  const { inputs, onBookInfoChange } = props;
  const { title, authors, publisher, talkUrl, writeTitle, writeContent } =
    inputs;

  const handleChangeString = (e, type) => {
    onBookInfoChange({ ...inputs, [`${type}`]: e.target.value });
    console.log(e.target.value);
  };

  // 책 표지 업로드
  const [bookImg, setBookImg] = useState(
    'https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg'
  );
  const uploadImg = (e) => {
    setBookImg(URL.createObjectURL(e.target.files[0]));
  };
  const deleteImg = () => {
    URL.revokeObjectURL(bookImg);
    setBookImg(
      'https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg'
    );
  };

  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleOpenModal = () => {
    setIsModalOpen(true);
  };
  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  return (
    <StyledShareForm>
      <h2>{props.page === 'shareAdd' ? '나눔하기' : '수정하기'}</h2>
      <SInputContainer>
        <SInputLeft>
          {bookImg && <img alt="bookImg" src={bookImg} className="bookImg" />}
          <SImgBtn>
            <input
              type="file"
              accept="image/*"
              id="ImgInput"
              onChange={uploadImg}
            />
            <label className="ImgInputBtn" htmlFor="ImgInput">
              책 표지 등록
            </label>
            <button className="imgDelete" onClick={() => deleteImg()}>
              삭제
            </button>
          </SImgBtn>
        </SInputLeft>
        <SInputRight>
          <div>
            <input
              name="title"
              value={title || ''}
              onChange={(e) => handleChangeString(e, title)}
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
              name="authors"
              value={authors || ''}
              onChange={(e) => handleChangeString(e, authors)}
              placeholder="저자를 입력해주세요."
            />
          </div>
          <div>
            <input
              name="publisher"
              value={publisher || ''}
              onChange={(e) => handleChangeString(e, publisher)}
              placeholder="출판사를 입력해주세요."
            />
          </div>
          <div>
            <input
              name="talkUrl"
              // value={talkUrl}
              onChange={(e) => handleChangeString(e, 'talkUrl')}
              placeholder="오픈채팅 대화방 링크를 입력해주세요."
            />
          </div>
          <div>
            <input
              name="writeTitle"
              // value={writeTitle}
              onChange={(e) => handleChangeString(e, 'writeTitle')}
              placeholder="게시글 제목을 입력해주세요."
            />
          </div>
          <div>
            <textarea
              name="writeContent"
              // value={writeContent}
              onChange={(e) => handleChangeString(e, 'writeContent')}
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
          {props.page === 'shareAdd' ? '등록' : '수정'}
        </button>
      </SButtonBox>
    </StyledShareForm>
  );
};

export default ShareForm;

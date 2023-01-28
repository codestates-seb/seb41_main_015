import styled from 'styled-components';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BookAddModal from './BookAddModal';
import Swal from 'sweetalert2';

const StyledReqForm = styled.div`
  .title {
    color: #2c2c2c;
    padding: 18px;
    margin: 0 10% 20px;
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
      height: 205px;
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
    width: 100%;
    height: 40px;
    margin-bottom: 20px;
    border: none;
    border-radius: 2px;
    background-color: #f4f4f4;
    padding-left: 10px;
    :focus {
      outline: none;
      border-bottom: 2px solid #4f4f4f;
    }
  }
  .inputContent {
    width: 100%;
    height: 220px;
    border: none;
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

  const { inputs, onBookInfoChange } = props;
  const { bookTitle, author, publisher, talkUrl, title, content, thumbnail } =
    inputs;

  const handleChangeString = (e, type) => {
    onBookInfoChange({ ...inputs, [`${type}`]: e.target.value });
  };

  const goBack = () => {
    Swal.fire({
      title: '작성을 취소하시겠습니까?',
      text: '작성 중인 내용은 저장되지 않습니다',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#bb2649',
      confirmButtonText: '확인',
      cancelButtonText: '취소',
    }).then((res) => {
      if (res.isConfirmed) {
        navigate(-1);
      }
    });
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
      <div className="title">
        <h2>{props.page === 'reqAdd' ? '요청하기' : '요청글 수정하기'}</h2>
        <div>
          {props.page === 'reqAdd' ? '원하는 도서를 요청해보세요!' : ''}
        </div>
      </div>

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
              autoComplete="off"
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
              autoComplete="off"
            />
          </div>
          <div>
            <input
              name="title"
              value={title || ''}
              onChange={(e) => handleChangeString(e, 'title')}
              placeholder="게시글 제목을 입력해주세요."
              autoComplete="off"
            />
          </div>
          <div>
            <input
              name="content"
              value={content || ''}
              onChange={(e) => handleChangeString(e, 'content')}
              className="inputContent"
              placeholder="게시글 내용을 입력해주세요. (ex. 책 상태, 구매 시기 등)"
              autoComplete="off"
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

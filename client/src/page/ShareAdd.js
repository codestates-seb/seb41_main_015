import styled from 'styled-components';
import { useState } from 'react';
import { useNavigate, createBrowserHistory } from 'react-router-dom';
import axios from 'axios';
import Swal from 'sweetalert2';

const StyledShareAdd = styled.div`
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
  }
  .inputContent {
    width: 95%;
    height: 150px;
    border: 1px solid #aaaaaa;
    border-radius: 4px;
    margin-bottom: 30px;
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

const ShareAdd = () => {
  const navigate = useNavigate();
  const goBack = () => {
    navigate(-1);
  };
  const [inputs, setInputs] = useState({
    bookname: '',
    author: '',
    publisher: '',
    link: '',
    title: '',
    content: '',
  });

  const { bookname, author, publisher, link, title, content } = inputs;

  const handleChangeString = (e, type) => {
    setInputs({ ...inputs, [`${type}`]: e.target.value });
    console.log(e.target.value);
  };

  const handleClickSubmit = () => {
    axios
      .post(`url`, {
        bookname,
        author,
        publisher,
        link,
        title,
        content,
      })
      .then((res) => {
        navigate('/shareDetail');
      })
      .catch((err) => {
        Swal.fire(
          '나눔글 작성 실패',
          '글 작성이 완료되지 않았습니다.',
          'warning'
        );
        console.log(err);
      });
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

  return (
    <StyledShareAdd>
      <h2>나눔하기</h2>
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
            <label className="ImgInputBtn" for="ImgInput">
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
              name="bookname"
              // value={bookname}
              onChange={(e) => handleChangeString(e, bookname)}
              placeholder="책 제목을 입력해주세요."
            />
          </div>
          <div>
            <input
              name="author"
              // value={author}
              onChange={(e) => handleChangeString(e, author)}
              placeholder="저자를 입력해주세요."
            />
          </div>
          <div>
            <input
              name="publisher"
              // value={publisher}
              onChange={(e) => handleChangeString(e, publisher)}
              placeholder="출판사를 입력해주세요."
            />
          </div>
          <div>
            <input
              name="link"
              // value={link}
              onChange={(e) => handleChangeString(e, link)}
              placeholder="오픈채팅 대화방 링크를 입력해주세요."
            />
          </div>
          <div>
            <input
              name="title"
              // value={title}
              onChange={(e) => handleChangeString(e, title)}
              placeholder="게시글 제목을 입력해주세요."
            />
          </div>
          <div>
            <textarea
              name="content"
              // value={content}
              onChange={(e) => handleChangeString(e, content)}
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
        <button className="submitBtn" onClick={handleClickSubmit}>
          등록
        </button>
      </SButtonBox>
    </StyledShareAdd>
  );
};
export default ShareAdd;

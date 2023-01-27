import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import BookAddModal from '../components/BookAddModal';
import instanceAxios from '../reissue/InstanceAxios';
import { RateStar } from '../components/RateStar';
import Swal from 'sweetalert2';

const StyledForm = styled.div`
  margin: 0px 190px;

  .title {
    color: #2c2c2c;
    padding: 18px;
    border-bottom: 1px solid #acacac;
  }

  @media screen and (max-width: 1200px) {
    margin: 0px 50px;
  }
`;

const SInputContainer = styled.div`
  display: flex;
  justify-content: space-around;
  margin: 30px 0px;
  font-size: 13px;
  @media screen and (max-width: 1200px) {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 30px;
    margin: 0;
  }
`;

const SImageContainer = styled.div`
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

const SInputs = styled.div`
  width: 65%;
  display: flex;
  flex-direction: column;
  margin-top: 10px;
  input {
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
  @media screen and (max-width: 1200px) {
    width: 80%;
  }
  .content {
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

const RateAdd = () => {
  const navigate = useNavigate();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  // 필요한 정보
  // 파라미터: isbn, bookTitle, author, publisher, thumbnail
  // body: rating, title, content
  const defaultImg =
    'https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg';
  const [inputs, setInputs] = useState({
    bookTitle: '',
    author: '',
    publisher: '',
    thumbnail: defaultImg,
    isbn: '',
    content: '',
  });

  const { bookTitle, author, publisher, thumbnail, isbn, content } = inputs;
  const [rating, setRating] = useState(0);
  const handleBookInfoChange = (bookInfo) => {
    setInputs(bookInfo);
  };

  // 문자열 변경 감지 함수
  const handleChangeString = (e, type) => {
    handleBookInfoChange({ ...inputs, [`${type}`]: e.target.value });
  };

  const accessToken = sessionStorage.getItem('accessToken');

  const handleSubmit = () => {
    if (accessToken) {
      // input 유효성 검사
      if (rating === 0) {
        Swal.fire(
          '등록 불가',
          '평점은 최소 1점 이상 등록 가능합니다',
          'warning'
        );
        return;
      }
      if (!bookTitle || bookTitle.length === 0) {
        Swal.fire('등록 불가', '책 정보를 입력해주세요', 'warning');
        return;
      }
      if (!content || content.length === 0) {
        Swal.fire('등록 불가', '코멘트를 입력해주세요', 'warning');
        return;
      }
      // 서버 요청
      instanceAxios
        .post(
          `v1/rates?isbn=${isbn}&bookTitle=${bookTitle}&author=${author}&publisher=${publisher}`,
          {
            rating,
            content,
            thumbnail,
          }
        )
        .then(() => {
          Swal.fire(
            '평점 등록 완료',
            '평점이 정상적으로 등록되었습니다.',
            'success'
          );
          navigate('/rateList');
        })
        .catch((err) => console.error(err));
    } else {
      Swal.fire({
        title: '로그인이 필요한 서비스입니다.',
        text: '로그인 후 이용해주세요.',
        icon: 'warning',
      });
    }
  };

  const handleCancel = () => {
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

  return (
    <StyledForm>
      <div className="title">
        <h2>평점 매기기</h2>
        <div>내가 읽은 책에 대한 평가를 남겨보세요!</div>
      </div>
      <SInputContainer>
        <SImageContainer>
          <img alt="bookImg" src={thumbnail} className="bookImg" />
          {/* 별점 컴포넌트 */}
          <RateStar rating={rating} setRating={setRating} />
        </SImageContainer>
        <SInputs>
          <input
            placeholder="책 제목을 입력해주세요"
            onClick={() => setIsModalOpen(true)}
            value={bookTitle || ''}
            onChange={(e) => handleChangeString(e, bookTitle)}
            autoComplete="off"
          />
          <BookAddModal
            isModalOpen={isModalOpen}
            onBookInfoChange={handleBookInfoChange}
            handleCloseModal={handleCloseModal}
            isRate={true}
          />
          <input
            placeholder="책 저자를 입력해주세요"
            value={author}
            onChange={(e) => handleChangeString(e, 'author')}
            disabled
            autoComplete="off"
          />
          <input
            placeholder="출판사를 입력해주세요"
            value={publisher}
            onChange={(e) => handleChangeString(e, 'publisher')}
            disabled
            autoComplete="off"
          />
          <input
            placeholder="책에 대한 코멘트를 입력해주세요"
            onChange={(e) => handleChangeString(e, 'content')}
            className="content"
            autoComplete="off"
          />
        </SInputs>
      </SInputContainer>
      <SButtonBox>
        <button className="cancelBtn" onClick={handleCancel}>
          취소
        </button>
        <button className="submitBtn" onClick={handleSubmit}>
          등록
        </button>
      </SButtonBox>
    </StyledForm>
  );
};

export default RateAdd;

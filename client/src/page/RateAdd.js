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

  // ????????? ?????? ?????? ??????
  const handleChangeString = (e, type) => {
    handleBookInfoChange({ ...inputs, [`${type}`]: e.target.value });
  };

  const accessToken = sessionStorage.getItem('accessToken');

  const handleSubmit = () => {
    if (accessToken) {
      // input ????????? ??????
      if (rating === 0) {
        Swal.fire(
          '?????? ??????',
          '????????? ?????? 1??? ?????? ?????? ???????????????',
          'warning'
        );
        return;
      }
      if (!bookTitle || bookTitle.length === 0) {
        Swal.fire('?????? ??????', '??? ????????? ??????????????????', 'warning');
        return;
      }
      if (!content || content.length === 0) {
        Swal.fire('?????? ??????', '???????????? ??????????????????', 'warning');
        return;
      }
      // ?????? ??????
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
            '?????? ?????? ??????',
            '????????? ??????????????? ?????????????????????.',
            'success'
          );
          navigate('/rateList');
        })
        .catch((err) => console.error(err));
    } else {
      Swal.fire({
        title: '???????????? ????????? ??????????????????.',
        text: '????????? ??? ??????????????????.',
        icon: 'warning',
      });
    }
  };

  const handleCancel = () => {
    Swal.fire({
      title: '????????? ?????????????????????????',
      text: '?????? ?????? ????????? ???????????? ????????????',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#bb2649',
      confirmButtonText: '??????',
      cancelButtonText: '??????',
    }).then((res) => {
      if (res.isConfirmed) {
        navigate(-1);
      }
    });
  };

  return (
    <StyledForm>
      <div className="title">
        <h2>?????? ?????????</h2>
        <div>?????? ?????? ?????? ?????? ????????? ???????????????!</div>
      </div>
      <SInputContainer>
        <SImageContainer>
          <img alt="bookImg" src={thumbnail} className="bookImg" />
          <RateStar rating={rating} setRating={setRating} />
        </SImageContainer>
        <SInputs>
          <input
            placeholder="??? ????????? ??????????????????"
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
            placeholder="??? ????????? ??????????????????"
            value={author}
            onChange={(e) => handleChangeString(e, 'author')}
            disabled
            autoComplete="off"
          />
          <input
            placeholder="???????????? ??????????????????"
            value={publisher}
            onChange={(e) => handleChangeString(e, 'publisher')}
            disabled
            autoComplete="off"
          />
          <input
            placeholder="?????? ?????? ???????????? ??????????????????"
            onChange={(e) => handleChangeString(e, 'content')}
            className="content"
            autoComplete="off"
          />
        </SInputs>
      </SInputContainer>
      <SButtonBox>
        <button className="cancelBtn" onClick={handleCancel}>
          ??????
        </button>
        <button className="submitBtn" onClick={handleSubmit}>
          ??????
        </button>
      </SButtonBox>
    </StyledForm>
  );
};

export default RateAdd;

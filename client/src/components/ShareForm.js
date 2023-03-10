import styled from 'styled-components';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BookAddModal from './BookAddModal';
import axios from 'axios';
import Swal from 'sweetalert2';

const StyledShareForm = styled.div`
  .title {
    color: #2c2c2c;
    padding: 18px;
    margin: 0 10%;
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
  @media screen and (max-width: 1070px) {
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
  .thumbnail {
    width: 230px;
    height: 297.156px;
    margin-bottom: 30px;

    @media screen and (max-width: 930px) {
      width: 160px;
      height: 205px;
    }
  }
  .imgAddDes {
    color: #626262;
    font-size: 11px;
    @media screen and (max-width: 1070px) {
      margin-bottom: 15px;
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

const ShareForm = (props) => {
  const navigate = useNavigate();
  const { inputs, onBookInfoChange } = props;
  const { bookTitle, author, publisher, talkUrl, title, content, thumbnail } =
    inputs;

  const handleChangeString = (e, type) => {
    onBookInfoChange({ ...inputs, [`${type}`]: e.target.value });
  };

  const handleChangeThmbnail = (image) => {
    onBookInfoChange({ ...inputs, thumbnail: image });
  };

  const uploadImg = (e) => {
    if (e.target.files) {
      const uploadImg = e.target.files[0];
      const accessToken = sessionStorage.getItem('accessToken');
      const formData = new FormData();
      formData.append('image', uploadImg);
      axios
        .post(
          'https://serverbookvillage.kro.kr/v1/s3/images/upload',
          formData,
          {
            headers: {
              'Content-Type': 'multipart/form-data',
              Athorization: `Bearer ${accessToken}`,
            },
          }
        )
        .then((res) => {
          handleChangeThmbnail(res.data.data);
        })
        .catch((err) => console.log(err));
    }

    if (e.target.files && e.target.files[0]) {
      const maxSize = 3 * 1024 * 1024;
      const fileSize = e.target.files[0].size;

      if (fileSize > maxSize) {
        Swal.fire(
          '??? ?????? ?????? ??????',
          '?????? ????????? ???????????? 3MB ????????? ?????? ???????????????.',
          'warning'
        );
      }
    }
  };

  const goBack = () => {
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

  const deleteImg = () => {
    URL.revokeObjectURL(thumbnail);
    handleChangeThmbnail(
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
      <div className="title">
        <h2>{props.page === 'shareAdd' ? '????????????' : '????????? ????????????'}</h2>
        <div>
          {props.page === 'shareAdd' ? '?????? ?????? ????????? ???????????????!' : ''}
        </div>
      </div>
      <SInputContainer>
        <SInputLeft>
          {thumbnail && (
            <img alt="thumbnail" src={thumbnail} className="thumbnail" />
          )}
          <SImgBtn>
            <input
              type="file"
              accept="image/*"
              id="ImgInput"
              disabled={bookTitle === undefined || bookTitle === ''}
              onChange={uploadImg}
            />
            <label className="ImgInputBtn" htmlFor="ImgInput">
              ??? ?????? ??????
            </label>
            <button className="imgDelete" onClick={() => deleteImg()}>
              ??????
            </button>
          </SImgBtn>
          <div className="imgAddDes">
            {props.page === 'shareAdd'
              ? '??? ????????? ????????? ??? ?????? ????????? ?????????????????? ??? ????????????!'
              : ''}
          </div>
        </SInputLeft>
        <SInputRight>
          <div>
            <input
              name="bookTitle"
              value={bookTitle || ''}
              onChange={(e) => handleChangeString(e, bookTitle)}
              placeholder="??? ????????? ??????????????????."
              onClick={handleOpenModal}
              autoComplete="off"
            />
            {/* ?????? ??????  */}
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
              placeholder="????????? ??????????????????."
              disabled
            />
          </div>
          <div>
            <input
              name="publisher"
              value={publisher || ''}
              onChange={(e) => handleChangeString(e, publisher)}
              placeholder="???????????? ??????????????????."
              disabled
            />
          </div>
          <div>
            <input
              name="talkUrl"
              value={talkUrl || ''}
              onChange={(e) => handleChangeString(e, 'talkUrl')}
              placeholder="???????????? ????????? ????????? ??????????????????."
              autoComplete="off"
            />
          </div>
          <div>
            <input
              name="title"
              value={title || ''}
              onChange={(e) => handleChangeString(e, 'title')}
              placeholder="????????? ????????? ??????????????????."
              autoComplete="off"
            />
          </div>
          <div>
            <input
              name="content"
              value={content || ''}
              onChange={(e) => handleChangeString(e, 'content')}
              className="inputContent"
              placeholder="????????? ????????? ??????????????????. (ex. ??? ??????, ?????? ?????? ???)"
              autoComplete="off"
            />
          </div>
        </SInputRight>
      </SInputContainer>
      <SButtonBox>
        <button className="cancelBtn" onClick={goBack}>
          ??????
        </button>
        <button className="submitBtn" onClick={props.editBtn}>
          {props.page === 'shareAdd' ? '??????' : '??????'}
        </button>
      </SButtonBox>
    </StyledShareForm>
  );
};

export default ShareForm;

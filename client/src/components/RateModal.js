import styled from 'styled-components';
import Swal from 'sweetalert2';
import { useState } from 'react';
import instanceAxios from '../reissue/InstanceAxios';
import { RateStar } from '../components/RateStar';

const SModalBackground = styled.div`
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 999;
`;

const SRateModal = styled.div`
  width: 400px;
  height: 320px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border-radius: 5px;
  border: 1px solid #aaaaaa;
  background-color: #ffffff;
  .close {
    padding-top: 3px;
    position: absolute;
    right: 13px;
    font-size: 2rem;
    color: #aaaaaa;
    cursor: pointer;
  }
`;
const SRateInfo = styled.div`
  display: flex;
  flex-direction: column;
  margin: 0;
  text-align: center;
  .rateExplain {
    margin-top: 40px;
    margin-bottom: 10px;
  }
  .star {
    flex-direction: row;
    margin-bottom: 20px;
  }
  .rateReviewBtn {
    margin-top: 20px;
    width: 300px;
    height: 35px;
    border: 1px solid #bb2649;
    border-radius: 2.5px;
    background-color: #bb2649;
    color: #ffffff;
  }
  .rateReviewTextBox {
    border-radius: 2.5px;
    padding: 10px;
    resize: none;
    height: 100px;
    width: 300px;
  }
`;
const SLimitNumber = styled.div`
  font-size: 10px !important;
  text-align: left;
  margin-left: 50px;
  color: #bb2649 !important;
`;

const RateModal = ({ isModalOpen, handleCloseModal, data }) => {
  const [content, setContent] = useState('');
  const [rating, setRating] = useState(0);

  const handleChangeRateContent = (e) => {
    setContent(e.target.value);
  };

  const handleClickRateSubmit = () => {
    //로그인 회원만 이용가능한 서비스
    const sessionAccessToken = sessionStorage.getItem('accessToken');
    if (sessionAccessToken) {
      if (content.length === 0) {
        Swal.fire('평점 등록 실패', '1글자 이상 작성하셔야 합니다.', 'warning');
      } else {
        instanceAxios
          .post(
            `/v1/rates?isbn=${data.isbn}&bookTitle=${data.bookTitle}&author=${data.author}&publisher=${data.publisher}`,
            {
              rating,
              content,
            }
          )
          .then((res) => {
            handleCloseModal();
            window.location.reload();
          })
          .catch((err) => {
            console.error(err);
            Swal.fire(
              '이미 평점이 등록되었습니다.',
              '평점은 한 번만 등록할 수 있습니다.',
              'warning'
            );
            handleCloseModal();
          });
      }
    } else {
      Swal.fire(
        '로그인이 필요한 서비스입니다',
        '로그인 후 이용해주세요.',
        'warning'
      );
    }
  };

  return (
    <>
      {isModalOpen ? (
        <SModalBackground onClick={handleCloseModal}>
          <SRateModal onClick={(e) => e.stopPropagation()}>
            <div className="close" onClick={handleCloseModal}>
              &times;
            </div>
            {/* 내용 넣기 */}
            <SRateInfo>
              <div className="rateExplain">
                이 책에 대한 평점과 리뷰를 남겨보세요
              </div>
              <div className="star">
                <RateStar rating={rating} setRating={setRating} />
              </div>
              <div>
                <textarea
                  className="rateReviewTextBox"
                  placeholder="리뷰를 입력해주세요"
                  onChange={handleChangeRateContent}
                ></textarea>
                {content.length < 1 ? (
                  <SLimitNumber>1글자 이상 입력하십시오</SLimitNumber>
                ) : null}
                <button
                  className="rateReviewBtn"
                  onClick={handleClickRateSubmit}
                >
                  리뷰 남기기
                </button>
              </div>
            </SRateInfo>
          </SRateModal>
        </SModalBackground>
      ) : null}
    </>
  );
};

export default RateModal;

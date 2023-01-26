import styled from 'styled-components';
import axios from 'axios';
import { useState, useEffect } from 'react';
import { RateStarSmall } from '../components/RateStar.js';
import { ReactComponent as BookStar } from '../image/bookStar.svg';
import instanceAxios from '../reissue/InstanceAxios';
import Swal from 'sweetalert2';

const SCommentWrap = styled.div`
  margin: 0px 10%;
  color: #434343;
  h2 {
    margin-bottom: 0px;
    border-bottom: 1px solid #acacac;
    padding-bottom: 15px;
    font-size: 1.3rem;
  }
  @media screen and (max-width: 1100px) {
    margin: 0px 24px;
  }
  input {
    border: 1px solid #acacac;
    border-radius: 3px;
    padding: 7px 5px;
  }
`;

const SCommentContainer = styled.div`
  display: flex;
  /* justify-content: center; */
  align-items: center;
  gap: 12px;
  margin: 20px 0px;
  padding: 0px 10px 20px 10px;
  border-bottom: 1px solid #acacac;

  img {
    width: 45px;
    height: 45px;
    border-radius: 70%;
  }

  .texts {
    display: flex;
    flex-direction: column;
    width: 100%;
  }

  .commentTop {
    display: flex;
    justify-content: space-between;
  }

  .commentInfo {
    display: flex;
    gap: 10px;
    margin-bottom: 7px;
  }

  #author {
    font-weight: 600;
  }

  #rates {
    display: flex;
    align-items: center;
    font-weight: 600;
    span {
      padding-bottom: 2px;
    }
  }

  #content {
    font-size: 0.9rem;
  }
`;

const SControlButtons = styled.div`
  color: #aaaaaa;
  font-size: 0.8rem;
  .button {
    margin: 0px 3px;
    &:hover {
      cursor: pointer;
      color: #bb2649;
    }
  }
`;

const RateComment = ({ data }) => {
  const [rating, setRating] = useState(0);
  const [editId, setEditId] = useState('');
  const handleEditMode = (rateId, rating, content) => {
    setEditId(rateId);
    setRating(rating);
    setEditInput(content);
  };

  const [editInput, setEditInput] = useState('');
  const handleChangeInput = (e) => {
    setEditInput(e.target.value);
  };

  const handleSubmitEdit = () => {
    console.log('editId: ', editId);
    console.log(rating, editInput);
    // 서버에 요청 보내기 (id는 나중에 props로 내려받기)
    instanceAxios
      .patch(`v1/rates/${editId}`, {
        rating: rating,
        content: editInput,
      })
      .then(() => {
        Swal.fire({
          title: '평점이 정상적으로 수정되었습니다.',
          icon: 'success',
          confirmButtonText: '확인',
        }).then((res) => {
          if (res.isConfirmed) {
            window.location.reload();
          }
        });
      })
      .catch((err) => {
        console.log(err);
        Swal.fire('수정 실패', '평점 수정에 실패했습니다.', 'warning');
      });
  };

  const handleCancelEdit = () => {
    setEditId('');
  };

  const handleDeleteRate = (rateId) => {
    console.log(rateId);
    // 삭제하기 전에 한 번 물어보기
    Swal.fire({
      title: '평점을 삭제하시겠습니까?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#bb2649',
      confirmButtonText: '확인',
      cancelButtonText: '취소',
    }).then((res) => {
      if (res.isConfirmed) {
        instanceAxios.delete(`v1/rates/${rateId}`).then(() => {
          Swal.fire('평점이 삭제되었습니다');
          window.location.reload();
        });
      }
    });
  };

  // // props로 상속받으면 됨. 아래 useEffect와 useState는 나중에 삭제하기!
  // // bookId는 props로 내려받아야 함
  // const [data, setData] = useState([]);
  // const url = 'https://serverbookvillage.kro.kr';
  // // book을 조회할 때 내려오는 rates를 렌더링!
  // useEffect(() => {
  //   axios.get(url + `/v1/books/2`).then((res) => {
  //     setData(res.data.data.rates);
  //   });
  // }, []);

  // displayName, content, createdAt, imgUrl, rating

  return (
    <SCommentWrap>
      <h2>리뷰 {data.length}</h2>
      {data.map((rate) => {
        const currentUser = sessionStorage.getItem('displayName');
        const isSameUser = rate.displayName === currentUser ? true : false;

        return (
          <SCommentContainer key={rate.rateId}>
            <img src={rate.imgUrl} alt="profile" />
            <div className="texts">
              <div className="commentTop">
                <div className="commentInfo">
                  <div id="author">{rate.displayName}</div>
                  {editId === rate.rateId ? (
                    <RateStarSmall rating={rating} setRating={setRating} />
                  ) : (
                    <div id="rates">
                      <BookStar />
                      <span>{rate.rating}</span>
                    </div>
                  )}
                </div>
                {isSameUser ? (
                  <SControlButtons>
                    {editId === rate.rateId ? (
                      <span className="button" onClick={handleSubmitEdit}>
                        확인
                      </span>
                    ) : (
                      <span
                        className="button"
                        onClick={() =>
                          handleEditMode(rate.rateId, rate.rating, rate.content)
                        }
                      >
                        수정
                      </span>
                    )}
                    <span>|</span>
                    {editId === rate.rateId ? (
                      <span className="button" onClick={handleCancelEdit}>
                        취소
                      </span>
                    ) : (
                      <span
                        className="button"
                        onClick={() => handleDeleteRate(rate.rateId)}
                      >
                        삭제
                      </span>
                    )}
                  </SControlButtons>
                ) : null}
              </div>
              {editId === rate.rateId ? (
                <input
                  defaultValue={rate.content}
                  onChange={handleChangeInput}
                ></input>
              ) : (
                <div id="content">{rate.content}</div>
              )}
            </div>
          </SCommentContainer>
        );
      })}
    </SCommentWrap>
  );
};

export default RateComment;

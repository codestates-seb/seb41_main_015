import styled from 'styled-components';
import { useState } from 'react';
import { RateStarSmall } from '../components/RateStar.js';
import { ReactComponent as BookStar } from '../image/bookStar.svg';
import instanceAxios from '../reissue/InstanceAxios';
import Swal from 'sweetalert2';
import { prettyDate } from '../util/dateparse';

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

  #createdAt {
    margin-left: 15px;
    font-size: 0.7rem;
    font-weight: 400;
    color: #acacac;
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
    // ????????? ?????? ????????? (id??? ????????? props??? ????????????)
    instanceAxios
      .patch(`v1/rates/${editId}`, {
        rating: rating,
        content: editInput,
      })
      .then(() => {
        Swal.fire({
          title: '????????? ??????????????? ?????????????????????.',
          icon: 'success',
          confirmButtonText: '??????',
        }).then((res) => {
          if (res.isConfirmed) {
            window.location.reload();
          }
        });
      })
      .catch((err) => {
        console.log(err);
        Swal.fire('?????? ??????', '?????? ????????? ??????????????????.', 'warning');
      });
  };

  const handleCancelEdit = () => {
    setEditId('');
  };

  const handleDeleteRate = (rateId) => {
    // ???????????? ?????? ??? ??? ????????????
    Swal.fire({
      title: '????????? ?????????????????????????',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#bb2649',
      confirmButtonText: '??????',
      cancelButtonText: '??????',
    }).then((res) => {
      if (res.isConfirmed) {
        instanceAxios.delete(`v1/rates/${rateId}`).then(() => {
          Swal.fire('????????? ?????????????????????');
          window.location.reload();
        });
      }
    });
  };
  return (
    <>
      {data && (
        <SCommentWrap>
          <h2>?????? {data.length}</h2>
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
                          <div id="createdAt">{prettyDate(rate.createdAt)}</div>
                        </div>
                      )}
                    </div>
                    {isSameUser ? (
                      <SControlButtons>
                        {editId === rate.rateId ? (
                          <span className="button" onClick={handleSubmitEdit}>
                            ??????
                          </span>
                        ) : (
                          <span
                            className="button"
                            onClick={() =>
                              handleEditMode(
                                rate.rateId,
                                rate.rating,
                                rate.content
                              )
                            }
                          >
                            ??????
                          </span>
                        )}
                        <span>|</span>
                        {editId === rate.rateId ? (
                          <span className="button" onClick={handleCancelEdit}>
                            ??????
                          </span>
                        ) : (
                          <span
                            className="button"
                            onClick={() => handleDeleteRate(rate.rateId)}
                          >
                            ??????
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
      )}
    </>
  );
};

export default RateComment;

import styled from 'styled-components';
import instanceAxios from '../reissue/InstanceAxios';
import axios from 'axios';
import Swal from 'sweetalert2';
import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useRef } from 'react';

const SCommentForm = styled.div`
  margin: 30px 0;
`;
const SInputContainer = styled.div`
  display: flex;
  justify-content: center;
  margin: 30px 0;
  .InputComment {
    width: 50%;
    height: 40px;
    border: 1px solid #aaaaaa;
    border-radius: 5px;
    padding-left: 10px;
  }
  .SunmbitComment {
    margin-left: 30px;
    width: 100px;
    color: #ffffff;
    border: 1px solid #bb2649;
    border-radius: 5px;
    background-color: #bb2649;
  }
`;
const SCommentContainer = styled.div`
  color: #212529;
  display: flex;
  flex-direction: column;
  padding-bottom: 24px;
  margin: 0 20%;
`;

const SUserContainer = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 24px;
  img {
    margin: 0;
    width: 43px;
    border-radius: 70%;
    margin-right: 10px;
  }
  .userName {
    font-size: 16px;
    font-weight: 700;
    align-items: center;
  }
`;
const SUserComment = styled.div`
  .content {
    width: 100%;
    /* height: 85px; */
    border: none;
    padding: 10px;
    overflow: hidden;
    resize: none;
    line-height: 1.6;
    font-size: 15px;
    background-color: #ffffff;
  }
  .tModify {
    width: 100%;
    /* height: 85px; */
    padding: 10px;
    overflow: hidden;
    resize: none;
    line-height: 1.6;
    font-size: 15px;
    border: 1px solid #aaaaaa;
    outline-color: #aaaaaa;
  }
  .btnBox {
    margin: 10px;
    float: right;
    color: #aaaaaa;
    font-size: 13px;
  }
  button {
    color: #aaaaaa;
    border: none;
    padding: 0 5px;
    :hover {
      color: #bb2649;
    }
  }
`;
const SCommentWrap = styled.div`
  border-bottom: 1px solid #ececec;
  margin: 10px 0;
`;

const Comment = ({ data, borrowComment, reqComment, page, id }) => {
  const [content, setContent] = useState('');
  const [contentForm, setContentForm] = useState(false);
  // const [commentData, setCommentData] = useState('');
  const textarea = useRef();

  const endpoint = page === 'share' ? 'borrows' : 'requests';
  const commentMap = page === 'share' ? borrowComment : reqComment;

  // 해당하는 유저에게만 댓글을 수정하고 삭제하는 권한주기
  const currentUser = sessionStorage.getItem('displayName');

  //수정 폼 글자수 제한 함수
  const handleChangeContent = (e) => {
    setContent(e.target.value);
    const text_length = e.target.value.length;
    if (text_length > 120) {
      Swal.fire(
        '글자 수 초과',
        '댓글은 120자 이상 작성 할 수 없습니다',
        'warning'
      );
    }
  };

  //textarea 높이 자동조절
  const handleChangeTextareaHeight = () => {
    textarea.current.style.height = 'auto';
    textarea.current.style.height = textarea.current.scrollHeight + 'px';
  };

  //수정 폼 판별하기
  const handleClickModifyComment = () => {
    setContentForm(true);
  };

  //댓글 등록
  const commentSubmit = () => {
    //로그인 회원만 이용가능한 서비스
    const sessionAccessToken = sessionStorage.getItem('accessToken');
    if (sessionAccessToken) {
      if (content) {
        instanceAxios
          .post(`/v1/${endpoint}/comments/${id}`, {
            content,
          })
          .then((res) => {
            console.log('postRes', res.data.data);
            window.location.reload();
            Swal.fire(
              '댓글 등록',
              '정상적으로 댓글이 등록되었습니다',
              'success'
            );
          })
          .catch((err) => {
            Swal.fire(
              '댓글 등록 실패',
              '댓글등록이 이루어지지 않았습니다.',
              'warning'
            );
            console.error(err);
          });
      } else {
        Swal.fire(
          '내용을 입력하십시오',
          '최소 1글자 이상 작성해주세요',
          'warning'
        );
      }
    } else {
      Swal.fire(
        '로그인이 필요한 서비스입니다',
        '로그인 후 이용해주세요.',
        'warning'
      );
    }
  };

  //댓글 수정
  const handleClickPatchComment = (commentId) => {
    if (content) {
      instanceAxios
        .patch(`/v1/${endpoint}/comments/${commentId}`, {
          content,
        })
        .then(() => {
          window.location.reload();
        })
        .catch(() => {
          Swal.fire(
            '댓글수정 실패',
            '댓글수정이 이루어지지 않았습니다.',
            'warning'
          );
        });
    } else {
      Swal.fire(
        '내용을 입력하십시오',
        '최소 1글자 이상 작성해주세요',
        'warning'
      );
    }
  };
  //댓글 삭제
  const handleClickDeleteComment = (commentId) => {
    instanceAxios
      .delete(`/v1/${endpoint}/comments/${commentId}`)
      .then(() => {
        window.location.reload();
        Swal.fire(
          '댓글 삭제 성공',
          '정상적으로 댓글이 삭제되었습니다',
          'success'
        );
      })
      .catch(() => {
        Swal.fire(
          '댓글 삭제 실패',
          '댓글삭제가 정상적으로 이루어지지 않았습니다.',
          'warning'
        );
      });
  };

  const handleClickCancelModify = () => {
    window.location.reload();
  };

  return (
    <SCommentForm>
      <SInputContainer>
        <input
          type="text"
          className="InputComment"
          placeholder="댓글을 남겨보세요"
          onChange={handleChangeContent}
        />
        <button
          className="SunmbitComment"
          type="sumbit"
          onClick={commentSubmit}
        >
          등록
        </button>
      </SInputContainer>
      <SCommentContainer>
        {commentMap.map((comment) => {
          console.log('1', comment.borrowCommentId);
          const commentId =
            page === 'share'
              ? comment.borrowCommentId
              : comment.requestCommentId;
          const isSameUser = comment.displayName === currentUser ? true : false;
          return (
            <SCommentWrap key={commentId}>
              <SUserContainer>
                <img
                  alt="profileImage"
                  // src부분을 {comment.imgUrl}로 변경하기
                  src="https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/309/59932b0eb046f9fa3e063b8875032edd_crop.jpeg"
                />

                <span className="userName">{comment.displayName}</span>
              </SUserContainer>
              <SUserComment>
                {contentForm ? (
                  //수정할 때
                  <div>
                    <textarea
                      className="tModify"
                      defaultValue={comment.content}
                      maxLength="120"
                      required
                      ref={textarea}
                      onChange={(e) => {
                        handleChangeContent(e);
                        handleChangeTextareaHeight();
                      }}
                    ></textarea>
                  </div>
                ) : (
                  //수정 안할때
                  <div>
                    <textarea
                      className="content"
                      defaultValue={comment.content}
                      maxLength="120"
                      disabled
                    ></textarea>
                  </div>
                )}
                {/* 내가 쓴 댓글만 수정가능 */}
                {isSameUser && (
                  <div className="btnBox">
                    {contentForm ? (
                      <div>
                        <button
                          onClick={() => handleClickPatchComment(commentId)}
                        >
                          확인
                        </button>
                        <button onClick={handleClickCancelModify}>취소</button>
                      </div>
                    ) : (
                      <div>
                        <button onClick={handleClickModifyComment}>수정</button>
                        <button
                          onClick={() => handleClickDeleteComment(commentId)}
                        >
                          삭제
                        </button>
                      </div>
                    )}
                  </div>
                )}
              </SUserComment>
            </SCommentWrap>
          );
        })}
      </SCommentContainer>
    </SCommentForm>
  );
};

export default Comment;

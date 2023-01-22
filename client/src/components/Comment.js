import styled from 'styled-components';
import instanceAxios from '../reissue/InstanceAxios';
import axios from 'axios';
import Swal from 'sweetalert2';
import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { current } from '@reduxjs/toolkit';

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
  border-bottom: 1px solid #ececec;
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
    height: 85px;
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
    height: 85px;
    padding: 10px;
    overflow: hidden;
    resize: none;
    line-height: 1.6;
    font-size: 15px;
    border: 1px solid #aaaaaa;
    outline-color: #aaaaaa;
  }
  .btnBox {
    margin-top: 10px;
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

const Comment = ({ data, page }) => {
  const { id } = useParams();
  const [content, setContent] = useState('');
  const [contentForm, setContentForm] = useState(false);

  const endpoint = page === 'share' ? 'borrows' : 'requests';

  // 해당하는 유저에게만 댓글을 수정하고 삭제하는 권한주기
  const currentUser = sessionStorage.getItem('displayName');
  const isSameUser = data.displayName === currentUser ? true : false;

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

  //수정 폼 판별하기
  const handleClickModifyComment = () => {
    setContentForm(true);
  };

  //댓글 등록
  const commentSubmit = () => {
    instanceAxios
      .post(`/v1/${endpoint}/comments/${id}`, {
        content,
      })
      .then((res) => {
        console.log('postRes', res.data.data);
        Swal.fire('댓글 등록', '정상적으로 댓글이 등록되었습니다', 'success');
      })
      .catch((err) => {
        Swal.fire(
          '댓글 등록 실패',
          '댓글등록이 이루어지지 않았습니다.',
          'warning'
        );
        console.error(err);
      });
  };
  //댓글 수정
  const handleClickPatchComment = () => {
    instanceAxios
      .patch(`/v1/${endpoint}/comments/${id}`, {
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
  };
  //댓글 삭제
  const handleClickDeleteComment = () => {
    instanceAxios
      .delete(`/v1/${endpoint}/comments/${id}`)
      .then(() => {
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
        {/* {data.borrowComments.map((comment)=>(<div>댓글 map함수</div>))} */}
        <SUserContainer>
          <img
            alt="profileImage"
            // src부분을 {comment.imgUrl}로 변경하기
            src="https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/309/59932b0eb046f9fa3e063b8875032edd_crop.jpeg"
          />
          {/* user => {comment.displayName} */}
          <span className="userName">user</span>
        </SUserContainer>
        <SUserComment>
          {contentForm ? (
            //수정할 때
            <div>
              {/* borrowComments에 값이 들어오면 defaultValue값을 {comment.content}로 수정하기 */}
              <textarea
                className="tModify"
                defaultValue={'가나다라마바사'}
                maxLength="120"
                required
                onChange={handleChangeContent}
              ></textarea>
            </div>
          ) : (
            //수정 안할때
            <div>
              <textarea
                className="content"
                defaultValue={'가나다라마바사'}
                maxLength="120"
                disabled
                onChange={handleChangeContent}
              ></textarea>
            </div>
          )}
          {/* 내가 쓴 댓글만 수정가능 */}
          {isSameUser && (
            <div className="btnBox">
              {contentForm ? (
                <div>
                  <button onClick={handleClickPatchComment}>확인</button>
                  <button onClick={handleClickCancelModify}>취소</button>
                </div>
              ) : (
                <div>
                  <button onClick={handleClickModifyComment}>수정</button>
                  <button onClick={handleClickDeleteComment}>삭제</button>
                </div>
              )}
            </div>
          )}
        </SUserComment>
      </SCommentContainer>
    </SCommentForm>
  );
};

export default Comment;

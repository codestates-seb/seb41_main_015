import styled from 'styled-components';
import Swal from 'sweetalert2';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import instanceAxios from '../reissue/InstanceAxios';
import { setExisting } from '../redux/slice/userSlice';

const SModalBackground = styled.div`
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 999;
`;

const SNicknameModal = styled.div`
  width: 400px;
  height: 250px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border-radius: 5px;
  border: 1px solid #aaaaaa;
  background-color: #ffffff;
`;

const SModalWrap = styled.div`
  display: flex;
  justify-content: center;
  flex-wrap: wrap;

  .modalContent {
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;
    align-items: center;
    height: 250px;

    h3 {
      font-size: 1.2rem;
      margin-bottom: 8px;
      text-align: center;
    }

    .caution {
      font-size: 0.8rem;
      margin-bottom: 15px;
    }

    #impossible {
      font-weight: 700;
      color: #bb2649;
    }
  }

  input {
    width: 264px;
    height: 35px;
    font-size: 0.9rem;
    padding-left: 5px;
  }

  .inputContainer {
    display: flex;
    justify-content: center;
  }

  .redBorder {
    border: 1.5px solid #bb2649;
    border-radius: 3px;
  }

  .errorMessage {
    color: #bb2649;
    font-size: 0.75rem;
    margin-top: 3px;
    padding: 3px;
    height: 25px;
  }

  .buttonContainer {
    display: flex;
    justify-content: center;
  }

  button {
    background-color: #bb2649;
    border: none;
    color: white;
    padding: 10px;
    border-radius: 5px;
    width: 130px;
    margin: 15px 0px;
    font-weight: 600;
  }
`;

const NicknameModal = ({ isModalOpen, handleCloseModal }) => {
  const dispatch = useDispatch();
  const [nickname, setNickname] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const validationCheck = (value) => {
    if (value.match(/^[a-zA-Z0-9가-힣+_.-]+$/)) {
      if (value.length === 0) {
        setErrorMessage('닉네임은 필수입니다.');
        return false;
      }
      if (value.length === 1) {
        setErrorMessage('닉네임은 2글자 이상이어야 합니다.');
        return false;
      }
      if (value.length > 20) {
        setErrorMessage('닉네임은 20글자를 넘을 수 없습니다.');
        return false;
      }
      return true;
    } else {
      setErrorMessage('특수문자, 띄어쓰기는 포함할 수 없습니다.');
      return false;
    }
  };

  const handleInfoPost = () => {
    // 유효성 검사를 충족할 때에만 데이터 보내기
    if (validationCheck(nickname)) {
      instanceAxios
        .patch('v1/members', {
          displayName: nickname,
        })
        .then(() => {
          handleCloseModal();
          dispatch(setExisting({ displayName: nickname }));
          Swal.fire(
            '닉네임 설정이 완료되었습니다',
            '북빌리지에 오신 것을 환영합니다!',
            'success'
          );
        })
        .catch((err) => {
          // 중복 닉네임이라 오류 뜰 경우 다루기
          if (err.response.status === 409) {
            setErrorMessage('이미 사용 중인 닉네임입니다.');
          } else {
            Swal.fire(
              '닉네임 등록 실패',
              '닉네임 설정에 실패했습니다',
              'warning'
            );
            console.error(err);
          }
        });
      return;
    }
    return;
  };

  const resetErrorMessage = () => {
    setErrorMessage('');
  };

  const redBorder = errorMessage.length === 0 ? '' : 'redBorder';

  return (
    <>
      {isModalOpen ? (
        <SModalBackground>
          <SNicknameModal onClick={(e) => e.stopPropagation()}>
            <SModalWrap>
              <div className="modalContent">
                <div>
                  <h3>닉네임을 설정해주세요.</h3>
                  <div className="caution">
                    닉네임은 변경이 <span id="impossible">불가능</span>하니
                    신중하게 설정해주세요!
                  </div>
                  <div className="inputContainer">
                    <input
                      type="text"
                      placeholder="2~20자 한글, 영어, 숫자, 특수문자(-._)"
                      value={nickname}
                      onChange={(e) => {
                        setNickname(e.target.value);
                      }}
                      onFocus={resetErrorMessage}
                      className={redBorder}
                    />
                  </div>
                  <div className="errorMessage">
                    {errorMessage ? errorMessage : ''}
                  </div>
                  <div className="buttonContainer">
                    <button onClick={handleInfoPost}>시작하기</button>
                  </div>
                </div>
              </div>
            </SModalWrap>
          </SNicknameModal>
        </SModalBackground>
      ) : null}
    </>
  );
};

export default NicknameModal;

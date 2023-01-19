import styled from 'styled-components';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
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

  /* .close {
    padding-top: 3px;
    position: absolute;
    right: 13px;
    font-size: 2rem;
    color: #aaaaaa;
    cursor: pointer;
  } */

  /* .bottomText {
    font-size: 0.8rem;
  } */
`;

const SModalWrap = styled.div`
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  /* align-items: center; */
  /* height: 300px; */

  .modalContent {
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;
    align-items: center;
    height: 250px;
    /* padding-top: 2rem; */

    h3 {
      font-size: 1.2rem;
      margin-bottom: 25px;
      text-align: center;
    }
  }

  input {
    width: 250px;
    height: 35px;
    font-size: 0.9rem;
    padding-left: 5px;
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

  const handleInfoPost = () => {
    // 유효성 검사 추가하기
    if (nickname.length === 0) {
      setErrorMessage('닉네임은 필수입니다.');
      return;
    }
    // 여기에 서버에 정보 전송 로직만 넣어주면 됨
    dispatch(setExisting());
    handleCloseModal();
  };

  const handleErrorMessage = () => {
    setErrorMessage('');
  };

  const redBorder = errorMessage.length === 0 ? '' : 'redBorder';

  return (
    <>
      {isModalOpen ? (
        <SModalBackground>
          <SNicknameModal onClick={(e) => e.stopPropagation()}>
            {/* <div className="close" onClick={handleCloseModal}>
              &times;
            </div> */}
            <SModalWrap>
              <div className="modalContent">
                <div>
                  <h3>닉네임을 설정해주세요.</h3>
                  <input
                    type="text"
                    placeholder="닉네임 생성 규칙"
                    value={nickname}
                    onChange={(e) => {
                      setNickname(e.target.value);
                    }}
                    onFocus={handleErrorMessage}
                    className={redBorder}
                  />
                  <div className="errorMessage">
                    {errorMessage ? errorMessage : ''}
                  </div>
                  <div className="buttonContainer">
                    <button onClick={handleInfoPost}>제출</button>
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

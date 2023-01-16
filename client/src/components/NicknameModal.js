import styled from 'styled-components';

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
  width: 448px;
  height: 430px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border-radius: 5px;
  border: 1px solid #aaaaaa;
  background-color: #ffffff;

  .modalContent {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding-top: 2rem;

    h3 {
      margin-bottom: 30px;
    }
  }

  .close {
    padding-top: 3px;
    position: absolute;
    right: 13px;
    font-size: 2rem;
    color: #aaaaaa;
    cursor: pointer;
  }

  .bottomText {
    font-size: 0.8rem;
  }

  button {
    background-color: white;
    border: 1px solid #bb2649;
    margin-top: 30px;
  }
`;

const NicknameModal = ({ isModalOpen, handleCloseModal }) => {
  const handlePost = () => {
    console.log('버튼 클릭!');
  };

  return (
    <>
      {isModalOpen ? (
        <SModalBackground onClick={handleCloseModal}>
          <SNicknameModal onClick={(e) => e.stopPropagation()}>
            <div className="close" onClick={handleCloseModal}>
              &times;
            </div>
            <div className="modalContent">
              <h3>닉네임을 입력하세요.</h3>
              <input type="text" placeholder="닉네임 생성 규칙" />
              <button>제출</button>
            </div>
          </SNicknameModal>
        </SModalBackground>
      ) : null}
    </>
  );
};

export default NicknameModal;

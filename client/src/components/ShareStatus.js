import styled from 'styled-components';

const SContainer = styled.div`
  .box {
    width: 80px;
    height: 33px;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 5px;
    font-size: 0.9rem;
  }

  .available {
    /* 토글 스위치랑 통일성 있게 같은 색으로 설정하기 */
    background-color: #2b7bf6;
    color: #ffffff;
    font-weight: 700;
  }

  .done {
    background-color: #cfcfcf;
    color: #474747;
    font-weight: 700;
  }
`;

const ShareStatus = () => {
  // props로 나눔 상태 받아오기
  // true이면 거래가능
  // false이면 거래완료

  const exampleStatus = false;
  const status = exampleStatus === true ? 'box available' : 'box done';
  const statusText = exampleStatus === true ? '거래가능' : '거래완료';

  return (
    <SContainer>
      <div className={status}>
        <div>{statusText}</div>
      </div>
    </SContainer>
  );
};

export default ShareStatus;

import styled from 'styled-components';

const SContainer = styled.div`
  .box {
    width: 70px;
    height: 33px;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 15px;
    font-size: 0.8rem;
    color: #000000;
    line-height: 33px;
  }

  .available {
    /* 토글 스위치랑 통일성 있게 같은 색으로 설정하기 */
    background-color: #c8f9b6;
    font-weight: 700;
  }

  .done {
    background-color: #dfdfdf;
    /* color: #474747; */
    font-weight: 700;
  }

  .onlyInShare {
    display: none;
  }
`;

const ShareStatus = ({ status }) => {
  // props로 나눔 상태 받아오기
  // true이면 거래가능
  // false이면 거래완료

  const shareStatus = status === true ? 'box available' : 'box done';
  const statusText = status === true ? '나눔가능' : '나눔완료';

  return (
    <SContainer>
      <div className={shareStatus}>
        <div>{statusText}</div>
      </div>
    </SContainer>
  );
};

export default ShareStatus;

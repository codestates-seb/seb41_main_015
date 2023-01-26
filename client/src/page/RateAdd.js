import styled from 'styled-components';

const StyledForm = styled.div`
  margin: 0px 190px;

  .title {
    color: #2c2c2c;
    padding: 18px;
    border-bottom: 1px solid #acacac;
  }

  @media screen and (max-width: 1360px) {
    margin: 0px 50px;
  }
`;

const SInputContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin: 30px 0px;
  font-size: 13px;
  @media screen and (max-width: 930px) {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 0;
  }
`;

const SImageContainer = styled.div`
  margin-top: 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
  .bookImg {
    width: 230px;
    margin-bottom: 30px;

    @media screen and (max-width: 930px) {
      width: 160px;
    }
  }
`;

const SInputs = styled.div`
  /* border: 1px solid green; */
  width: 65%;
  display: flex;
  flex-direction: column;
  margin-top: 10px;
  input {
    height: 40px;
    margin-bottom: 20px;
    border: none;
    border-radius: 2px;
    background-color: #f4f4f4;
    padding-left: 10px;
    :focus {
      outline: none;
      border-bottom: 2px solid #4f4f4f;
    }
  }
  .content {
    height: 220px;
    border: none;
    margin-bottom: 30px;
    padding: 10px;
  }
`;

const SButtonBox = styled.div`
  display: flex;
  justify-content: center;
  margin-bottom: 70px;
  button {
    width: 15%;
    height: 35px;
    border: 1px solid #bb2649;
    border-radius: 5px;
  }
  .cancelBtn {
    color: #bb2649;
    background-color: #ffffff;
    border: 1px solid #bb2649;
    margin-right: 5%;
    :hover {
    }
  }
  .submitBtn {
    color: #ffffff;
    background-color: #bb2649;
    :hover {
    }
  }
`;

const RateAdd = () => {
  return (
    <StyledForm>
      <div className="title">
        <h2>평점 매기기</h2>
        <div>내가 읽은 책에 대한 평가를 남겨보세요!</div>
      </div>
      <SInputContainer>
        <SImageContainer>
          <img
            alt="bookImg"
            src="https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg"
            className="bookImg"
          />
          <div>책 사진 등록 버튼</div>
          <div>그리고 별 다섯개..</div>
        </SImageContainer>
        <SInputs>
          <input placeholder="책 제목을 입력해주세요" />
          <input placeholder="책 저자를 입력해주세요" />
          <input placeholder="출판사를 입력해주세요" />
          <input placeholder="제목을 입력해주세요" />
          <input placeholder="코멘트를 입력해주세요" className="content" />
        </SInputs>
      </SInputContainer>
      <SButtonBox>
        <button className="cancelBtn">취소</button>
        <button className="submitBtn">등록</button>
      </SButtonBox>
    </StyledForm>
  );
};

export default RateAdd;

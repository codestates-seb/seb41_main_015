import styled from 'styled-components';

const SInputList = styled.div`
  display: block;
  text-align: center;
  p {
    margin-bottom: 31px;
  }
  .inputSize {
    width: 472px;
    height: 36px;
  }
  .mr-30 {
    margin-right: 30px;
  }
  .mr-18 {
    margin-right: 18px;
  }
  .mr-6 {
    margin-right: 6px;
  }
`;

const SHr = styled.hr`
  margin-bottom: 50px;
`;

const SWithdraw = styled.div`
  font-family: 'Inter';
  font-style: normal;
  font-weight: 700;
  font-size: 15px;
  line-height: 18px;
  color: #bb2649;
  cursor: pointer;
  margin-left: 1100px;
`;

const SCancelBtn = styled.button`
  height: 43px;
  width: 141px;
  left: 576px;
  top: 860px;
  border-radius: 5px;
  border: 1px solid #bb2649;
  color: #bb2649;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 700;
  font-size: 16px;
`;

const SSaveBtn = styled.button`
  height: 43px;
  width: 141px;
  left: 576px;
  top: 860px;
  border-radius: 5px;
  border: 1px solid #bb2649;
  background-color: #bb2649;
  color: white;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 700;
  font-size: 16px;
  margin-left: 40px;
`;

const SEditBtn = styled.div`
  text-align: center;
  margin-top: 60px;
`;

const STitle = styled.h1`
  margin-left: 54px;
`;

const SDefaultProfile = styled.div`
  margin-left: 200px;
  img {
    height: 100px;
    width: 100px;
    border-radius: 50%;
  }
`;

const MyPageEdit = () => {
  return (
    <div>
      <STitle>회원정보 수정</STitle>
      <SHr />
      <SDefaultProfile>
        <img
          src="https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/309/59932b0eb046f9fa3e063b8875032edd_crop.jpeg"
          alt="profile"
        />
      </SDefaultProfile>
      <SInputList>
        <p>
          <label htmlFor="name" className="mr-30">
            이름:
          </label>
          <input
            id="name"
            className="inputSize"
            placeholder="이름을 입력하십시오"
          ></input>
        </p>
        <p>
          <label htmlFor="nickName" className="mr-18">
            닉네임:
          </label>
          <input
            id="nickName"
            className="inputSize"
            placeholder="닉네임을 입력하십시오"
          ></input>
        </p>
        <p>
          <label htmlFor="email" className="mr-18">
            이메일:
          </label>
          <input
            id="email"
            className="inputSize"
            placeholder="이메일을 입력하십시오"
          ></input>
        </p>
        <p>
          <label htmlFor="address" className="mr-30">
            주소:
          </label>
          <input
            id="address"
            className="inputSize"
            placeholder="주소를 입력하십시오"
          ></input>
        </p>
        <p>
          <label htmlFor="phonNumber" className="mr-6">
            전화번호:
          </label>
          <input
            id="phonNumber"
            className="inputSize"
            placeholder="전화번호를 입력하십시오"
          ></input>
        </p>
        <p>
          <label htmlFor="profile" className="mr-18">
            프로필:
          </label>
          <input
            id="profile"
            className="inputSize"
            placeholder="프로필을 url형태로 입력하십시오"
          ></input>
        </p>
      </SInputList>
      <SWithdraw>회원탈퇴</SWithdraw>
      <SEditBtn>
        <SCancelBtn>취소</SCancelBtn>
        <SSaveBtn>저장</SSaveBtn>
      </SEditBtn>
    </div>
  );
};
export default MyPageEdit;

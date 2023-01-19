import styled from 'styled-components';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import instanceAxios from '../reissue/InstanceAxios';

const SWrapEdit = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;
const SFlexRow = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  width: 100%;

  @media screen and (max-width: 1080px) {
    padding-left: 10px;
  }
`;
const SLabelList = styled.div`
  display: flex;
  flex-direction: column;
  text-align: center;
  margin: 0 50px;
  label {
    margin-bottom: 1rem;
    padding-top: 10px;
    height: 36px;
    width: 70px;
  }
`;

const SInputList = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin: 0px;
  padding-right: 10%;
  @media screen and (max-width: 1080px) {
    padding-left: 10px;
    justify-content: left;
  }
  input {
    margin-bottom: 1rem;
  }
  input:disabled {
    background: #f2f2f2;
  }
  .inputSize {
    width: 472px;
    height: 36px;
    @media screen and (max-width: 1080px) {
      width: 20rem;
    }
  }
`;

const SWithdraw = styled.div`
  font-family: 'Inter';
  font-style: normal;
  font-weight: 700;
  font-size: 15px;
  line-height: 18px;
  color: #bb2649;
  cursor: pointer;
  display: flex;
  justify-content: flex-end;
  padding-right: 10%;
`;

const SCancelBtn = styled.button`
  height: 43px;
  width: 141px;
  /* left: 576px; */
  top: 860px;
  border-radius: 5px;
  border: 1px solid #bb2649;
  color: #bb2649;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 700;
  font-size: 16px;
  @media screen and (max-width: 1080px) {
    height: 40px;
    width: 100px;
    margin-left: 50px;
  }
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
  @media screen and (max-width: 1080px) {
    height: 40px;
    width: 100px;
    margin-left: 20px;
  }
`;

const SEditBtn = styled.div`
  text-align: center;
  margin-top: 20px;
`;

const STitle = styled.div`
  h2 {
    color: #2c2c2c;
    padding: 18px;
    margin: 20px 10%;
    border-bottom: 1px solid #acacac;
  }
`;

const SDefaultProfile = styled.div`
  margin: 2rem 0;
  @media screen and (max-width: 1080px) {
    display: flex;
    margin: 2rem 0;
    justify-content: center;
  }
  img {
    height: 100px;
    width: 100px;
    /* border-radius: 50%; */
    @media screen and (max-width: 1080px) {
      height: 80px;
      width: 80px;
      margin-left: 110%;
    }
  }
`;

const MyPageEdit = () => {
  //컴포넌트에서 바뀌는 값 관리
  const [name, setName] = useState('');
  const [displayName, setDisplayName] = useState('');
  const [email, setEmail] = useState('');
  const [address, setAddress] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [profile, setProfile] = useState('');

  const navigate = useNavigate();

  //input 입력값 상태 저장
  const handleChangeName = (e) => {
    setName(e.target.value);
  };
  const handleChangeDisplayName = (e) => {
    setDisplayName(e.target.value);
  };
  const handleChangeEmail = (e) => {
    setEmail(e.target.value);
  };
  const handleChangeAddress = (e) => {
    setAddress(e.target.value);
  };
  const handleChangePhoneNumber = (e) => {
    setPhoneNumber(e.target.value);
  };
  const handleChangeProfile = (e) => {
    setProfile(e.target.value);
  };

  //저장 버튼 클릭 시, 서버로 patch 요청
  const handleClickSave = () => {
    instanceAxios
      .patch('/v1/members', {
        name,
        displayName,
        address,
        phoneNumber,
      })
      .then(() => {
        navigate('/mypage');
        Swal.fire(
          '프로필 수정 완료.',
          '프로필 수정이 정상적으로 이루어졌습니다.',
          'success'
        );
      })
      .catch((err) => {
        Swal.fire(
          '프로필 수정 실패',
          '수정이 정상적으로 등록되지 않았습니다.',
          'warning'
        );
        console.error(err);
      });
  };

  // 서버 연결 후 주석 풀기!
  useEffect(() => {
    const editData = async () => {
      try {
        const res = await instanceAxios.get('/v1/members');
        // console.log(res.data.data);
        setName(res.data.data.name);
        setEmail(res.data.data.email);
        setDisplayName(res.data.data.displayName);
        setAddress(res.data.data.address);
        setPhoneNumber(res.data.data.phoneNumber);
      } catch (error) {
        console.error(error);
        navigate('/');
        Swal.fire(
          '죄송합니다',
          '회원님의 정보를 가져오는데 실패했습니다.',
          'warning'
        );
      }
    };
    editData();
  }, []);

  //회원탈퇴(이벤트 연결할 것!)
  const handleClickQuit = () => {
    instanceAxios.patch('/v1/members/quit').then(() => {
      Swal.fire({
        title: '회원탈퇴를 진행하시겠습니까?',
        text: '회원탈퇴 후 재로그인이 어렵습니다 신중하게 생각해주세요.',
        icon: 'warning',
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
        confirmButtonText: '승인', // confirm 버튼 텍스트 지정
        cancelButtonText: '취소', // cancel 버튼 텍스트 지정
        reverseButtons: true, // 버튼 순서 거꾸로
      }).then((result) => {
        if (result.isConfirmed) {
          // 만약 모달창에서 confirm 버튼을 눌렀다면
          sessionStorage.clear();
          navigate('/');
          window.location.reload();
          console.log('회원탈퇴!');
          Swal.fire(
            '정상적으로 회원탈퇴가 처리되었습니다.',
            '이용해주셔서 감사합니다',
            'success'
          );
        }
      });
    });
  };

  return (
    <div>
      <STitle>
        <h2>회원정보 수정</h2>
      </STitle>
      <SWrapEdit>
        <SDefaultProfile>
          <img
            src="https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/309/59932b0eb046f9fa3e063b8875032edd_crop.jpeg"
            alt="profile"
          />
        </SDefaultProfile>
        <SFlexRow>
          <SLabelList>
            <label htmlFor="name" className="mr-30 inputSize">
              이름
            </label>
            <label htmlFor="nickName" className="mr-18 inputSize">
              닉네임
            </label>

            <label htmlFor="email" className="mr-18 inputSize">
              이메일
            </label>
            <label htmlFor="address" className="mr-30 inputSize">
              주소
            </label>
            <label htmlFor="phonNumber" className="mr-6 inputSize">
              전화번호
            </label>
            <label htmlFor="profile" className="mr-18 inputSize">
              프로필
            </label>
          </SLabelList>
          <SInputList>
            <input
              id="name"
              type="text"
              className="inputSize"
              placeholder="이름을 입력하십시오"
              value={name || ''}
              onChange={handleChangeName}
            ></input>
            <input
              id="nickName"
              type="text"
              className="inputSize"
              placeholder="닉네임을 입력하십시오"
              disabled
              value={displayName || ''}
              onChange={handleChangeDisplayName}
            ></input>

            <input
              id="email"
              type="text"
              className="inputSize"
              placeholder="이메일을 입력하십시오"
              disabled
              value={email || ''}
              onChange={handleChangeEmail}
            ></input>

            <input
              id="address"
              type="text"
              className="inputSize"
              placeholder="주소를 입력하십시오"
              value={address || ''}
              onChange={handleChangeAddress}
            ></input>

            <input
              id="phonNumber"
              type="text"
              className="inputSize"
              placeholder="전화번호를 입력하십시오"
              value={phoneNumber || ''}
              onChange={handleChangePhoneNumber}
            ></input>

            <input
              id="profile"
              type="text"
              className="inputSize"
              placeholder="프로필을 url형태로 입력하십시오"
              value={profile || ''}
              onChange={handleChangeProfile}
            ></input>

            <SWithdraw>회원탈퇴</SWithdraw>
          </SInputList>
        </SFlexRow>
      </SWrapEdit>
      <SEditBtn>
        <SCancelBtn onClick={() => navigate('/mypage')}>취소</SCancelBtn>
        <SSaveBtn onClick={handleClickSave}>저장</SSaveBtn>
      </SEditBtn>
    </div>
  );
};
export default MyPageEdit;

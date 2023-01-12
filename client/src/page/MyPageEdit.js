import styled from 'styled-components';
import axios from 'axios';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

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
  margin-left: 48%;
  img {
    height: 100px;
    width: 100px;
    /* border-radius: 50%; */
  }
`;

const MyPageEdit = () => {
  //컴포넌트에서 바뀌는 값 관리
  const [name, setName] = useState('');
  const [nickname, setNickname] = useState('');
  const [email, setEmail] = useState('');
  const [address, setAddress] = useState('');
  const [phonNumber, setPhonNumber] = useState('');
  const [profile, setProfile] = useState('');

  //현재 접속한 페이지의 id값을 가져옴
  const { id } = useParams();
  //navigate
  const navigate = useNavigate();

  //input 입력값 상태 저장
  const handleChangeName = (e) => {
    setName(e.target.value);
    console.log(e.target.value);
  };
  const handleChangeNickname = (e) => {
    setNickname(e.target.value);
    console.log(e.target.value);
  };
  const handleChangeEmail = (e) => {
    setEmail(e.target.value);
    console.log(e.target.value);
  };
  const handleChangeAddress = (e) => {
    setAddress(e.target.value);
    console.log(e.target.value);
  };
  const handleChangePhonNumber = (e) => {
    setPhonNumber(e.target.value);
    console.log(e.target.value);
  };
  const handleChangeProfile = (e) => {
    setProfile(e.target.value);
    console.log(e.target.value);
  };

  //base url
  const url = 'https://serverbookvillage.kro.kr';
  //저장 버튼 클릭 시, 서버로 patch 요청
  const handleClickSave = () => {
    axios
      .patch(
        url + `/v1/members`,
        {
          name,
          nickname,
          email,
          address,
          phonNumber,
          profile,
        }
        //토큰 부분
        // {
        //   headers: {
        //     'Content-Type': 'application/json;charset=UTF-8',
        //     Accept: 'application/json',
        //     Authorization:
        //       'Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInVzZXJuYW1lIjoidGpkdG40NDg0QGdtYWlsLmNvbSIsInN1YiI6InRqZHRuNDQ4NEBnbWFpbC5jb20iLCJpYXQiOjE2NzM1MTIzNjcsImV4cCI6MTY3MzUxNDE2N30.XcstPpk7smmT-_qAkeoo0jHQObjbrb9xEFt0XO5kxtQ ',
        //   },
        // }
      )
      .then(() => {
        navigate('/mypage');
        alert('프로필 수정이 완료되었습니다!');
        console.log('수정완료');
      })
      .catch((err) => {
        alert('수정이 정상적으로 이루어지지 않았습니다. 다시 시도해주세요!');
        console.error(err);
      });
  };

  // 서버 연결 후 주석 풀기!
  useEffect(() => {
    axios
      .get(url + `/v1/members`, {
        //토큰 부분
        // headers: {
        //   'Content-Type': 'application/json;charset=UTF-8',
        //   Accept: 'application/json',
        //   Authorization:
        //     'Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInVzZXJuYW1lIjoidGpkdG40NDg0QGdtYWlsLmNvbSIsInN1YiI6InRqZHRuNDQ4NEBnbWFpbC5jb20iLCJpYXQiOjE2NzM1MTIzNjcsImV4cCI6MTY3MzUxNDE2N30.XcstPpk7smmT-_qAkeoo0jHQObjbrb9xEFt0XO5kxtQ',
        // },
      })
      .then((res) => {
        setName(res.data.name);
        setNickname(res.data.nickname);
        setEmail(res.data.email);
        setAddress(res.data.address);
        setPhonNumber(res.data.phonNumber);
        setPhonNumber(res.data.profile);
      })
      .catch((err) => {
        console.error(err);
      });
  }, []);

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
            // value={name}
            onChange={handleChangeName}
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
            // value={nickname}
            onChange={handleChangeNickname}
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
            // value={email}
            onChange={handleChangeEmail}
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
            // value={address}
            onChange={handleChangeAddress}
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
            // value={phonNumber}
            onChange={handleChangePhonNumber}
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
            // value={profile}
            onChange={handleChangeProfile}
          ></input>
        </p>
      </SInputList>
      <SWithdraw>회원탈퇴</SWithdraw>
      <SEditBtn>
        <SCancelBtn>취소</SCancelBtn>
        <SSaveBtn onClick={handleClickSave}>저장</SSaveBtn>
      </SEditBtn>
    </div>
  );
};
export default MyPageEdit;

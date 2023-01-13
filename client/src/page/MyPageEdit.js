import styled from 'styled-components';
import axios from 'axios';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';

const SWrapEdit = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;
const SInputList = styled.div`
  margin: 0 5%;
  input:disabled {
    background: #f2f2f2;
  }
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
  display: flex;
  justify-content: flex-end;
  padding-right: 10%;
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
  margin-bottom: 30%;
  margin-top: 20px;
`;

const STitle = styled.h1`
  margin-left: 5%;
`;

const SDefaultProfile = styled.div`
  img {
    height: 100px;
    width: 100px;
    /* border-radius: 50%; */
  }
`;

const MyPageEdit = () => {
  //컴포넌트에서 바뀌는 값 관리
  const [memberId, setMemberId] = useState('');
  const [name, setName] = useState('');
  const [displayName, setDisplayName] = useState('');
  const [email, setEmail] = useState('');
  const [address, setAddress] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [profile, setProfile] = useState('');

  //현재 접속한 페이지의 id값을 가져옴
  // const { id } = useParams();

  //경로 이동
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

  //base url
  const url = 'https://serverbookvillage.kro.kr';

  //전역상태값 가져오기
  const user = useSelector((state) => state.user);

  //저장 버튼 클릭 시, 서버로 patch 요청
  const handleClickSave = () => {
    axios
      .patch(
        `${url}/v1/members`,
        {
          memberId,
          name,
          displayName,
          address,
          phoneNumber,
        },
        // 토큰 부분
        {
          headers: {
            'Content-Type': 'application/json;charset=UTF-8',
            Accept: 'application/json',
            Authorization: `Bearer ${user.accessToken}`,
          },
        }
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
    const editData = async () => {
      try {
        const res = await axios.get(url + '/v1/members', {
          // 토큰 부분
          headers: {
            'Content-Type': 'application/json;charset=UTF-8',
            Accept: 'application/json',
            Authorization: `Bearer ${user.accessToken}`,
          },
        });
        console.log(res.data.data);
        setMemberId(res.data.data.memberId);
        setName(res.data.data.name);
        setEmail(res.data.data.email);
        setDisplayName(res.data.data.displayName);
        setAddress(res.data.data.address);
        setPhoneNumber(res.data.data.phoneNumber);
      } catch (error) {
        console.error(error);
        alert('정보를 불러오는데 실패했습니다');
      }
    };
    editData();
    console.log('렌더링 중!');
  }, []);
  //   axios
  //     .get(url + '/v1/members', {
  //       // 토큰 부분
  //       headers: {
  //         'Content-Type': 'application/json;charset=UTF-8',
  //         Accept: 'application/json',
  //         Authorization: `Bearer ${user.accessToken}`,
  //       },
  //     })
  //     .then((res) => {
  //       setName(res.data.name);
  //       setDisplayName(res.data.displayName);
  //       setEmail(res.data.email);
  //       setAddress(res.data.address);
  //       setPhoneNumber(res.data.phoneNumber);
  //     })
  //     .catch((err) => {
  //       console.error(err);
  //       alert('회원정보를 불러오는데 실패했습니다');
  //     });
  // }, []);

  return (
    <div>
      <STitle>회원정보 수정</STitle>
      <SHr />
      <SWrapEdit>
        <SDefaultProfile>
          <img
            src="https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/309/59932b0eb046f9fa3e063b8875032edd_crop.jpeg"
            alt="profile"
          />
        </SDefaultProfile>
        <SInputList>
          <p>
            <label htmlFor="name" className="mr-30">
              이름
            </label>
            <input
              id="name"
              type="text"
              className="inputSize"
              placeholder="이름을 입력하십시오"
              value={name || ''}
              onChange={handleChangeName}
            ></input>
          </p>
          <p>
            <label htmlFor="nickName" className="mr-18">
              닉네임
            </label>
            <input
              id="nickName"
              type="text"
              className="inputSize"
              placeholder="닉네임을 입력하십시오"
              value={displayName || ''}
              onChange={handleChangeDisplayName}
            ></input>
          </p>
          <p>
            <label htmlFor="email" className="mr-18">
              이메일
            </label>
            <input
              id="email"
              type="text"
              className="inputSize"
              placeholder="이메일을 입력하십시오"
              disabled
              value={email || ''}
              onChange={handleChangeEmail}
            ></input>
          </p>
          <p>
            <label htmlFor="address" className="mr-30">
              주소
            </label>
            <input
              id="address"
              type="text"
              className="inputSize"
              placeholder="주소를 입력하십시오"
              value={address || ''}
              onChange={handleChangeAddress}
            ></input>
          </p>
          <p>
            <label htmlFor="phonNumber" className="mr-6">
              전화번호
            </label>
            <input
              id="phonNumber"
              type="text"
              className="inputSize"
              placeholder="전화번호를 입력하십시오"
              value={phoneNumber || ''}
              onChange={handleChangePhoneNumber}
            ></input>
          </p>
          <p>
            <label htmlFor="profile" className="mr-18">
              프로필
            </label>
            <input
              id="profile"
              type="file"
              className="inputSize"
              placeholder="프로필을 url형태로 입력하십시오"
              value={profile || ''}
              onChange={handleChangeProfile}
            ></input>
          </p>
          <SWithdraw>회원탈퇴</SWithdraw>
        </SInputList>
      </SWrapEdit>
      <SEditBtn>
        <SCancelBtn onClick={() => navigate('/mypage')}>취소</SCancelBtn>
        <SSaveBtn onClick={handleClickSave}>저장</SSaveBtn>
      </SEditBtn>
    </div>
  );
};
export default MyPageEdit;

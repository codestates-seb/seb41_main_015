import { useState } from 'react';
import Swal from 'sweetalert2';
import styled from 'styled-components';

const SLabel = styled.label`
  position: relative;
  display: inline-block;
  width: 90px;
  height: 33px;

  input {
    opacity: 0;
    width: 0;
    height: 0;
  }

  input:checked + .statusSwitch {
    /* 배경색 논의해보기 */
    /* 테마색 */
    /* background-color: #bb2649; */
    background-color: #c8f9b6;
  }

  input:checked + .statusSwitch:before {
    -webkit-transition: translateX(58px);
    -ms-transform: translateX(58px);
    transform: translateX(58px);
    transition: 0.4s;
  }

  .statusSwitch {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    border-radius: 20px;
    background-color: #dfdfdf;
    -webkit-transition: 0.4s;
    transition: 0.4s;
  }

  .statusSwitch:before {
    position: absolute;
    content: '';
    height: 24px;
    width: 24px;
    left: 4px;
    bottom: 5px;
    background-color: #ffffff;
    border-radius: 20px;
    -webkit-transition: 0.4s;
    transition: 0.4s;
  }

  // 안에 글자
  .texts {
    position: absolute;
    left: 0;
    width: 100%;
    height: 100%;
    font-size: 0.8rem;
    color: #000000;
    transition: all 0.4s ease-in-out;
    cursor: pointer;
  }

  .texts::after {
    content: attr(data-off);
    position: absolute;
    /* color: #474747; */
    font-weight: 700;
    opacity: 1;
    transition: all 0.4s;
    top: 7px;
    right: 12px;
  }

  .texts::before {
    content: attr(data-on);
    position: absolute;
    color: #474747;
    top: 7px;
    left: 13px;
    font-weight: 700;
    opacity: 0;
    transition: all 0.4s;
  }

  input:checked ~ .texts::after {
    opacity: 0;
  }

  input:checked ~ .texts::before {
    opacity: 1;
  }
`;

const ToggleSwitch = () => {
  // 초기값은 서버에서 받아온 나눔 상태 값 (boolean)
  // props로 내려줘야 할 듯!!
  const [isAvailable, setIsAvailable] = useState(true);

  const handleToggleClick = () => {
    setIsAvailable(!isAvailable);
  };

  const handleStatusChange = () => {
    Swal.fire({
      title: '나눔 상태를 변경하시겠습니까?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#bb2649',
      confirmButtonText: '확인',
      cancelButtonText: '취소',
    }).then((res) => {
      if (res.isConfirmed) {
        handleToggleClick();
        console.log('나눔 상태:', !isAvailable);
        console.log('서버에 정보를 전송합니다...');
        console.log('---------------------');
      }
    });
  };

  return (
    <SLabel>
      <input
        type="checkbox"
        checked={isAvailable}
        onChange={handleStatusChange}
      />
      <span className="statusSwitch"></span>
      <span className="texts" data-on="나눔가능" data-off="나눔완료"></span>
    </SLabel>
  );
};

export default ToggleSwitch;

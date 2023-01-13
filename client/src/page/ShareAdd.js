import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import Swal from 'sweetalert2';
import ShareForm from '../components/ShareForm';

const StyledShareAdd = styled.div`
  width: 100%;
`;

const ShareAdd = () => {
  const handleClickSubmit = () => {
    Swal.fire('나눔글 작성 실패', '글 작성이 완료되지 않았습니다.', 'warning');
  };
  return (
    <StyledShareAdd>
      <ShareForm title="나눔하기" edit="등록" editBtn={handleClickSubmit} />
    </StyledShareAdd>
  );
};

export default ShareAdd;

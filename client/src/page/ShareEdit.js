import { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import Swal from 'sweetalert2';
import ShareForm from '../components/ShareForm';

const StyledShareEdit = styled.div`
  width: 100%;
`;

const ShareEdit = () => {
  const handleClickEdit = () => {
    Swal.fire('나눔글 수정 실패', '글 수정이 완료되지 않았습니다.', 'warning');
  };

  return (
    <StyledShareEdit>
      <ShareForm title="수정하기" edit="수정" editBtn={handleClickEdit} />
    </StyledShareEdit>
  );
};

export default ShareEdit;

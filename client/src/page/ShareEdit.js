import axios from 'axios';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import Swal from 'sweetalert2';
import ShareForm from '../components/ShareForm';

const StyledShareEdit = styled.div`
  width: 100%;
`;

const ShareEdit = () => {
  const navigate = useNavigate();
  const url = 'https://serverbookvillage.kro.kr';
  const accessToken = sessionStorage.getItem('accessToken');

  const [inputs, setInputs] = useState({
    bookname: '',
    author: '',
    publisher: '',
    link: '',
    title: '',
    content: '',
  });

  const { bookname, author, publisher, link, title, content } = inputs;

  const handleClickEdit = () => {
    axios
      .patch(
        `${url}/v1/borrow/`,
        {
          bookname,
          author,
          publisher,
          link,
          title,
          content,
        },
        {
          header: {
            'Content-Type': 'application/json;charset=UTF-8',
            Accept: 'application/json',
            Authorization: `Bearer  ${accessToken}`,
          },
        }
      )
      .then((res) => {
        navigate('/shareDetail');
      })
      .catch(() => {});
    Swal.fire('나눔글 수정 실패', '글 수정이 완료되지 않았습니다.', 'warning');
  };

  return (
    <StyledShareEdit>
      <ShareForm
        page="shareEdit"
        editBtn={handleClickEdit}
        inputs={inputs}
        setInputs={setInputs}
      />
    </StyledShareEdit>
  );
};

export default ShareEdit;

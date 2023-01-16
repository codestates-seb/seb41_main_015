import axios from 'axios';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import Swal from 'sweetalert2';
import ShareForm from '../components/ShareForm';

const StyledShareAdd = styled.div`
  width: 100%;
`;

const ShareAdd = () => {
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

  const handleClickSubmit = () => {
    axios
      .post(
        `${url}/v1/borrow`,
        {
          bookname,
          author,
          publisher,
          link,
          title,
          content,
        },
        {
          headers: {
            'Content-Type': 'application/json;charset=UTF-8',
            Accept: 'application/json',
            Authorization: `Bearer  ${accessToken}`,
          },
        }
      )
      .then((res) => {
        if (accessToken === null) {
          Swal.fire(
            '권한이 없습니다.',
            '로그인 상태에서만 글 작성이 가능합니다.',
            'warning'
          );
        } else {
          navigate('/shareDetail');
        }
      })
      .catch((err) => {
        Swal.fire(
          '나눔글 작성 실패',
          '글 작성이 완료되지 않았습니다.',
          'warning'
        );
      });
  };
  return (
    <StyledShareAdd>
      <ShareForm
        page="shareAdd"
        editBtn={handleClickSubmit}
        inputs={inputs}
        setInputs={setInputs}
      />
    </StyledShareAdd>
  );
};

export default ShareAdd;

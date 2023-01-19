// import axios from 'axios';
import { useState } from 'react';
// import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import Swal from 'sweetalert2';
import ShareForm from '../components/ShareForm';
import instanceAxios from '../reissue/InstanceAxios';

const StyledShareAdd = styled.div`
  width: 100%;
`;

const ShareAdd = () => {
  // const navigate = useNavigate();

  const [inputs, setInputs] = useState({
    bookTitle: '',
    author: '',
    publisher: '',
    talkUrl: '',
    title: '',
    content: '',
  });

  const { bookTitle, author, publisher, talkUrl, title, content } = inputs;

  const handleClickSubmit = () => {
    console.log(inputs);
    instanceAxios
      .post('/v1/borrows', {
        bookTitle,
        author,
        publisher,
        talkUrl,
        title,
        content,
      })
      .then((res) => {
        console.log(res);
        Swal.fire(
          '나눔 글 등록 완료.',
          '나눔 글이 정상적으로 작성되었습니다.',
          'success'
        );
        // if (accessToken === null) {
        //   Swal.fire(
        //     '권한이 없습니다.',
        //     '로그인 상태에서만 글 작성이 가능합니다.',
        //     'warning'
        //   );
        // } else {
        //   navigate('/shareDetail');
        // }
      })
      .catch((err) => {
        Swal.fire(
          '나눔글 작성 실패',
          '글 작성이 완료되지 않았습니다.',
          'warning'
        );
      });
  };

  const handleBookInfoChange = (bookInfo) => {
    setInputs(bookInfo);
  };

  return (
    <StyledShareAdd>
      <ShareForm
        page="shareAdd"
        editBtn={handleClickSubmit}
        inputs={inputs}
        onBookInfoChange={handleBookInfoChange}
      />
    </StyledShareAdd>
  );
};

export default ShareAdd;

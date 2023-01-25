import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import ShareForm from '../components/ShareForm';
import instanceAxios from '../reissue/InstanceAxios';

const ShareAdd = () => {
  const navigate = useNavigate();

  const [inputs, setInputs] = useState({
    bookTitle: '',
    author: '',
    publisher: '',
    talkUrl: '',
    title: '',
    content: '',
    thumbnail:
      'https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg',
  });

  const { bookTitle, author, publisher, talkUrl, title, content, thumbnail } =
    inputs;

  const handleClickSubmit = () => {
    instanceAxios
      .post('/v1/borrows', {
        bookTitle,
        author,
        publisher,
        talkUrl,
        title,
        content,
        thumbnail,
      })
      .then((res) => {
        Swal.fire(
          '나눔 글 등록 완료.',
          '나눔 글이 정상적으로 작성되었습니다.',
          'success'
        );
        navigate('/shareList');
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
    <>
      <ShareForm
        page="shareAdd"
        editBtn={handleClickSubmit}
        inputs={inputs}
        onBookInfoChange={handleBookInfoChange}
      />
    </>
  );
};

export default ShareAdd;
import ReqForm from '../components/ReqForm';
import { useState } from 'react';
import Swal from 'sweetalert2';
import instanceAxios from '../reissue/InstanceAxios';
import { useNavigate } from 'react-router-dom';

const ReqAdd = () => {
  const navigate = useNavigate();

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
      .post('/v1/requests', {
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
          '요청 글 등록 완료.',
          '요청 글이 정상적으로 작성되었습니다.',
          'success'
        );
        navigate('/reqList');
      })
      .catch((err) => {
        Swal.fire(
          '요청글 작성 실패',
          '글 작성이 완료되지 않았습니다.',
          'warning'
        );
      });
  };

  const handleBookInfoChange = (bookInfo) => {
    setInputs(bookInfo);
  };

  return (
    <ReqForm
      page="reqAdd"
      editBtn={handleClickSubmit}
      inputs={inputs}
      onBookInfoChange={handleBookInfoChange}
    ></ReqForm>
  );
};
export default ReqAdd;

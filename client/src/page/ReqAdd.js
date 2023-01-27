import ReqForm from '../components/ReqForm';
import { useState } from 'react';
import Swal from 'sweetalert2';
import instanceAxios from '../reissue/InstanceAxios';
import { useNavigate } from 'react-router-dom';

const ReqAdd = () => {
  const navigate = useNavigate();
  const defaultImg =
    'https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg';

  const [inputs, setInputs] = useState({
    bookTitle: '',
    author: '',
    publisher: '',
    talkUrl: '',
    title: '',
    content: '',
    thumbnail: defaultImg,
  });

  const { bookTitle, author, publisher, talkUrl, title, content, thumbnail } =
    inputs;

  const handleClickSubmit = () => {
    const accessToken = sessionStorage.getItem('accessToken');
    if (!accessToken) {
      Swal.fire(
        '로그인이 필요한 서비스입니다',
        '로그인 후 이용해주세요.',
        'warning'
      );
      return;
    }

    console.log(inputs);
    instanceAxios
      .post('/v1/requests', {
        bookTitle,
        author,
        publisher,
        talkUrl,
        title,
        content,
        thumbnail,
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

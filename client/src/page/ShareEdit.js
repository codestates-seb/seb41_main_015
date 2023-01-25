import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Swal from 'sweetalert2';
import ShareForm from '../components/ShareForm';
import instanceAxios from '../reissue/InstanceAxios';

const ShareEdit = (onBookInfoChange) => {
  const navigate = useNavigate();
  const { id } = useParams();

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

  const handleClickEdit = () => {
    instanceAxios
      .patch(`v1/borrows/${id}`, {
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
          '나눔글 수정 완료',
          '글 수정이 정상적으로 수정되었습니다.',
          'success'
        );
        navigate('/shareList');
      })
      .catch((err) => {
        Swal.fire(
          '나눔글 수정 실패',
          '글 수정이 완료되지 않았습니다.',
          'warning'
        );
      });
  };

  useEffect(() => {
    const shareAddData = async () => {
      try {
        const result = await instanceAxios.get(`v1/borrows/${id}`);
        setInputs(result.data.data);
      } catch (err) {
        console.log(err);
      }
    };
    shareAddData();
  }, []);

  const handleBookInfoChange = (bookInfo) => {
    setInputs(bookInfo);
  };

  return (
    <>
      <ShareForm
        page="shareEdit"
        editBtn={handleClickEdit}
        inputs={inputs}
        onBookInfoChange={handleBookInfoChange}
      />
    </>
  );
};

export default ShareEdit;

import ReqForm from '../components/ReqForm';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import instanceAxios from '../reissue/InstanceAxios';
import Swal from 'sweetalert2';

const ReqEdit = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const defaultImg =
    ' https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg';

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
    instanceAxios
      .patch(`/v1/requests/${id}`, {
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
          '요청글 수정 완료',
          '글이 정상적으로 수정되었습니다.',
          'success'
        );
        navigate('/ReqList');
      })
      .catch((err) => {
        Swal.fire('요청글 수정 실패', '글이 수정되지 않았습니다.', 'warning');
      });
  };

  useEffect(() => {
    const reqAddData = async () => {
      try {
        const result = await instanceAxios.get(`/v1/requests/${id}`);
        setInputs(result.data.data);
      } catch (err) {
        console.log(err);
      }
    };
    reqAddData();
  }, []);

  const handleBookInfoChange = (bookInfo) => {
    setInputs(bookInfo);
  };

  return (
    <ReqForm
      page="ReqEdit"
      editBtn={handleClickSubmit}
      inputs={inputs}
      onBookInfoChange={handleBookInfoChange}
    />
  );
};
export default ReqEdit;

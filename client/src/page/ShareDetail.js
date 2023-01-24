import { useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
import DetailForm from '../components/DetailForm';
import Swal from 'sweetalert2';
import Comment from '../components/Comment';

const ShareDetail = () => {
  const { id } = useParams();
  const [data, setData] = useState({});
  const [borrowCommentId, setBorrowCommentId] = useState([]);
  const url = 'https://serverbookvillage.kro.kr/';

  useEffect(() => {
    const shareData = async () => {
      try {
        window.scrollTo(0, 0);
        const res = await axios.get(url + `v1/borrows/${id}`);
        setData(res.data.data);
        setBorrowCommentId(res.data.data.borrowComments);
        console.log('getShareDetail', res.data.data);
      } catch (error) {
        Swal.fire('데이터 로딩 실패', '데이터 로딩에 실패했습니다.', 'warning');
        console.error(error);
      }
    };
    shareData();
  }, []);

  return (
    <>
      <DetailForm data={data} page="share" id={id} />
      <Comment
        data={data}
        borrowCommentId={borrowCommentId}
        page="share"
        id={id}
      />
    </>
  );
};
export default ShareDetail;

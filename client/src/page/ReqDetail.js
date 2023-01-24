import { useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
import DetailForm from '../components/DetailForm';
import Swal from 'sweetalert2';
import Comment from '../components/Comment';

const ReqDetail = () => {
  const { id } = useParams();
  const [data, setData] = useState({});
  const [reqComment, setReqComment] = useState([]);
  const url = 'https://serverbookvillage.kro.kr/';

  useEffect(() => {
    window.scrollTo(0, 0);
    axios
      .get(url + `v1/requests/${id}`)
      .then((res) => {
        setData(res.data.data);
        setReqComment(res.data.data.requestComments);
      })
      .catch((err) => {
        Swal.fire('데이터 로딩 실패', '데이터 로딩에 실패했습니다.', 'warning');
        console.error(err);
      });
  }, []);

  return (
    <>
      <DetailForm data={data} page="request" id={id} />
      <Comment data={data} reqComment={reqComment} page="request" id={id} />
    </>
  );
};

export default ReqDetail;

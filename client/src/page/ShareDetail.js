import { useParams } from 'react-router-dom';
import { useState, useEffect } from 'react';
import axios from 'axios';
import DetailForm from '../components/DetailForm';
import Swal from 'sweetalert2';

const ShareDetail = () => {
  const { id } = useParams();
  const [data, setData] = useState({});
  const url = 'https://serverbookvillage.kro.kr/';

  useEffect(() => {
    window.scrollTo(0, 0);
    axios
      .get(url + `v1/borrows/${id}`)
      .then((res) => {
        setData(res.data.data);
      })
      .catch((err) => {
        Swal.fire('데이터 로딩 실패', '데이터 로딩에 실패했습니다.', 'warning');
        console.error(err);
      });
  }, []);

  return (
    <>
      <DetailForm data={data} page="share" />
    </>
  );
};
export default ShareDetail;

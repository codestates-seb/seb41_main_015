import axios from 'axios';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import shareListData from '../data/shareListData.json';
import ListHigh from '../components/ListHigh';
import BookList from '../components/BookList';

const ShareList = () => {
  const [title, setTitle] = useState('');
  const [cover, setCover] = useState('');
  const [bookTitle, setBookTitle] = useState('');
  const [writer, setWriter] = useState('');
  const [publisher, setPublisher] = useState('');
  const [createdAt, setCreatedAt] = useState('');

  const navigate = useNavigate();
  const { id } = useParams();

  //!서버연결 후 주석 풀기 (비동기 처리)
  // useEffect(() => {
  //   const listData = async () => {
  //     try {
  //       const res = await axios.get('http://localhost8080/sharelist');
  //       setCover(res.data.cover);
  //       setTitle(res.data.title);
  //       setWriter(res.data.writer);
  //       setPublisher(res.data.publisher);
  //       setCreatedAt(res.data.createdAt);
  //     } catch (error) {
  //       console.error(error);
  //       alert('정보를 불러오는데 실패했습니다');
  //     }
  //   };
  //   listData();
  // }, []);

  // 비동기 처리x
  // useEffect(() => {
  //   axios
  //     .get('http://localhost8080/sharelist')
  //     .then((res) => {
  //       setCover(res.data.cover);
  //       setTitle(res.data.title);
  //       setWriter(res.data.writer);
  //       setPublisher(res.data.publisher);
  //       setCreatedAt(res.data.createdAt);
  //     })
  //     .catch((err) => {
  //       console.error(err);
  //       alert('책 정보를 불러오는데 실패했습니다.');
  //     });
  // }, []);

  return (
    <>
      <ListHigh page="share" />
      <BookList data={shareListData.books} page="share" />
    </>
  );
};
export default ShareList;

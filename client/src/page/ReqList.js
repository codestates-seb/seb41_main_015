import { useState, useEffect } from 'react';
import ListHigh from '../components/ListHigh';
import BookList from '../components/BookList';
import reqListData from '../data/reqListData.json';
import axios from 'axios';

const ReqList = () => {
  // const url = 'https://serverbookvillage.kro.kr/';
  // const [data, setData] = useState([]);

  // useEffect(() => {
  //   // get 요청으로 요청 목록 데이터 받아오기
  //   axios
  //     .get(url + '엔드포인트')
  //     .then((res) => setData(res.data))
  //     .catch((err) => console.log(err));
  // }, []);

  return (
    <>
      <ListHigh page="request" />
      <BookList data={reqListData.books} page="request" />
    </>
  );
};

export default ReqList;

import ListHigh from '../components/ListHigh';
import BookList from '../components/BookList';
import reqListData from '../data/reqListData.json';

const ReqList = () => {
  // get 요청으로 요청 목록 데이터 받아오기

  return (
    <>
      <ListHigh page="request" />
      <BookList data={reqListData.books} page="request" />
    </>
  );
};

export default ReqList;

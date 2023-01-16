import { useState, useEffect } from 'react';
import ListHigh from '../components/ListHigh';
import BookList from '../components/BookList';
import reqListData from '../data/reqListData.json';
import axios from 'axios';

const ReqList = () => {
  const [title, setTitle] = useState('빌리지 사람들이 찾고 있는 책이에요!');
  const [keyword, setKeyword] = useState('');
  const [type, setType] = useState('');
  // const [data, setData] = useState([]);

  // const url = 'https://serverbookvillage.kro.kr/';

  // useEffect(() => {
  //   // get 요청으로 요청 목록 데이터 받아오기
  //   axios
  //     .get(url + '엔드포인트')
  //     .then((res) => setData(res.data))
  //     .catch((err) => alert('데이터를 불러오는데 실패했습니다.'));
  // }, []);

  const handleInput = (e) => {
    setKeyword(e.target.value);
  };

  const handleSearch = (e) => {
    if (e.nativeEvent.isComposing === true) return;
    if (e.key === 'Enter') {
      if (type.length === 0) {
        // Swal.fire('검색어 종류를 설정해주세요');
        alert('검색어 종류를 설정해주세요');
        return;
      }
      if (keyword.length === 0) {
        // Swal.fire('검색어를 입력해주세요.');
        alert('검색어를 입력해주세요.');
        return;
      }
      console.log('엔터 입력!', `키워드: ${keyword}, 검색어 종류: ${type}`);
      setTitle(`'${keyword}'에 대한 검색 결과입니다.`);
      // 검색 정보를 서버로 보내고
      // data를 그 정보로 업데이트하면 상태가 변경되므로 알아서 리렌더링 발생
    }
  };

  // 드롭다운 이벤트
  const handleOption = (e) => {
    setType(e.target.value);
  };

  return (
    <>
      <ListHigh
        title={title}
        page="request"
        handleInput={handleInput}
        handleSearch={handleSearch}
        handleOption={handleOption}
      />
      {/* reqListData.books는 더미데이터, 나중에 data로 바꾸기 */}
      <BookList data={reqListData.books} page="request" />
    </>
  );
};

export default ReqList;

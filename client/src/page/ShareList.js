import axios from 'axios';
import { useEffect, useState } from 'react';
import shareListData from '../data/shareListData.json';
import ListHigh from '../components/ListHigh';
import BookList from '../components/BookList';
import Paging from '../components/Paging';

const ShareList = () => {
  const [title, setTitle] = useState('현재 빌리지에 올라온 목록입니다!');
  const [keyword, setKeyword] = useState('');
  const [type, setType] = useState('');

  // 한 페이지에 들어갈 데이터 (페이지가 바뀔 때마다 get으로 받아옴)
  const [items, setItems] = useState([]);
  const [page, setPage] = useState(1);
  const handlePageChange = (page) => {
    setPage(page);
  };

  // 총 데이터
  const [data, setData] = useState(shareListData.books);

  // const url = 'https://serverbookvillage.kro.kr/';

  // useEffect(() => {
  //   // get 요청으로 요청 목록 데이터 받아오기
  //   axios
  //     .get(url + 'v1/borrow')
  //     .then((res) => setData(res.data))
  //     .catch((err) => alert('데이터를 불러오는데 실패했습니다.'));
  // }, []);

  const handleKeyword = (e) => {
    // 앞뒤 공백 지우기
    setKeyword(e.target.value.replace(/^\s+|\s+$/gm, ''));
  };

  const handleSearch = (e) => {
    if (e.nativeEvent.isComposing === true) return;

    if (e.key === 'Enter') {
      // type이 없고 검색어도 없을 땐 아무것도 하지 않기
      if (!type && !keyword) return;

      // 둘 중 하나라도 입력한 경우에는 경고창 띄우기
      if (type && !keyword) {
        alert('검색어를 입력해주세요.');
        return;
      }

      if (!type && keyword) {
        alert('검색어 종류를 선택해주세요.');
        return;
      }

      console.log('엔터 입력!', `키워드: ${keyword}, 검색어 종류: ${type}`);
      setTitle(`'${keyword}'에 대한 검색 결과입니다.`);
      setKeyword('');

      // axios
      //   .get(url + `v1/borrow/search?field=${type}&keyword=${keyword}`)
      //   .then((res) => setData(res.data))
      //   .catch((err) => alert('데이터를 불러오는데 실패했습니다.'))
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
        page="share"
        keyword={keyword}
        handleKeyword={handleKeyword}
        handleSearch={handleSearch}
        handleOption={handleOption}
      />
      <BookList data={data} page="share" />
      {/* 데이터의 총 길이가 필요함..! */}
      <Paging
        page={page}
        count={data.length}
        handlePageChange={handlePageChange}
      />
    </>
  );
};

export default ShareList;

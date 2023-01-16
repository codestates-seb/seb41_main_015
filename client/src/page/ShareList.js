import axios from 'axios';
import { useEffect, useState } from 'react';
import Swal from 'sweetalert2';
import shareListData from '../data/shareListData.json';
import ListHigh from '../components/ListHigh';
import BookList from '../components/BookList';

const ShareList = () => {
  const [title, setTitle] = useState('현재 빌리지에 올라온 목록입니다!');
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

  const handleKeyword = (e) => {
    setKeyword(e.target.value);
  };

  const handleSearch = (e) => {
    if (e.nativeEvent.isComposing === true) return;

    if (e.key === 'Enter') {
      // type 미선택 혹은 keyword가 비었을 때 알림창 띄우기
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
      // input도 비워버리기
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
        handleKeyword={handleKeyword}
        handleSearch={handleSearch}
        handleOption={handleOption}
      />
      {/* shareListData.books는 더미데이터, 나중에 data로 바꾸기 */}
      <BookList data={shareListData.books} page="share" />
    </>
  );
};
export default ShareList;

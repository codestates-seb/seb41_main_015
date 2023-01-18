import axios from 'axios';
import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import Swal from 'sweetalert2';
import shareListData from '../data/shareListData.json';
import ListHigh from '../components/ListHigh';
import BookList from '../components/BookList';
import Paging from '../components/Paging';

const CommonList = (props) => {
  const { headTitle, endpoint, route } = props;
  const { pathname } = useLocation();
  const id = pathname === '/shareList' ? 'borrowId' : 'requestId';

  const url = 'https://serverbookvillage.kro.kr/';

  const [title, setTitle] = useState('');
  const [keyword, setKeyword] = useState('');
  const [type, setType] = useState('');
  const [isSearchMode, setIsSearchMode] = useState(false);

  // 한 페이지에 들어갈 데이터 (페이지가 바뀔 때마다 get으로 받아옴)
  const [items, setItems] = useState([]);
  const [page, setPage] = useState(1);

  // 총 데이터 개수
  const [count, setCount] = useState(0);
  const PER_PAGE = 4;

  useEffect(() => {
    // 상태 초기화
    setTitle(headTitle);
    setPage(1);
    setIsSearchMode(false);
    setItems([]);

    // 첫 페이지 데이터 불러오기
    axios
      .get(url + `v1/${endpoint}?page=0&size=${PER_PAGE}&sort=${id}%2Cdesc`)
      .then((res) => {
        setItems(res.data.data);
        setCount(res.data.pageInfo.totalElements);
      })
      .catch((err) => {
        setItems([]);
        setCount(0);
        console.log(err);
        Swal.fire('데이터를 불러오는데 실패했습니다');
      });
  }, [pathname]);

  const getDatabyPage = async (page) => {
    try {
      const res = await axios.get(
        url +
          `v1/${endpoint}?page=${page - 1}&size=${PER_PAGE}&sort=${id}%2Cdesc`
      );
      const data = res.data; // 디버깅 필요
      return data;
    } catch (err) {
      console.error(err);
    }
  };

  const getSearchDatabyPage = async (keyword, type, page) => {
    // try {
    //   const res = await axios.get(
    //     url +
    //       `v1/${endpoint}/search?field=${type}&keyword=${keyword}&page=${
    //         page - 1
    //       }&size=12`
    //   );
    //   const data = res.data; // 디버깅 필요
    //   return data;
    // } catch (err) {
    //   console.error(err);
    // }
  };

  const handlePageChange = async (page) => {
    console.log(page);
    setPage(page);

    if (!isSearchMode) {
      const pageData = await getDatabyPage(page);
      console.log(pageData);
      setItems(pageData.data);
      console.log('검색 중이 아닙니다.');
    } else {
      // const pageData = await getSearchDatabyPage(keyword, type, page);
      // setItems(pageData);
      console.log('검색 중입니다.');
    }

    window.scrollTo(0, 0);
  };

  const handleKeyword = (e) => {
    setKeyword(e.target.value.replace(/^\s+|\s+$/gm, ''));
  };

  // 드롭다운 이벤트
  const handleOption = (e) => {
    setType(e.target.value);
    // console.log(e.target.value);
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

      setTitle(`'${keyword}'에 대한 검색 결과입니다.`);

      axios
        .get(
          url +
            `v1/${endpoint}/search?field=${type}&keyword=${keyword}&page=0&size=${PER_PAGE}&sort=createdAt%2Cdesc`
        )
        .then((res) => {
          // console.log(res.data);
          setIsSearchMode(true);
          setItems(res.data.data);
          setCount(res.data.pageInfo.totalElements);
          setPage(1);
        })
        .catch((err) => {
          Swal.fire(
            '데이터 로딩 실패',
            '데이터 로딩에 실패했습니다.',
            'warning'
          );
          console.log(err);
        });

      setKeyword('');
    }
  };

  return (
    <>
      <ListHigh
        title={title}
        route={route}
        keyword={keyword}
        handleKeyword={handleKeyword}
        handleSearch={handleSearch}
        handleOption={handleOption}
      />
      <BookList data={items} route={route} />
      <Paging
        page={page}
        count={count}
        perPage={PER_PAGE}
        handlePageChange={handlePageChange}
      />
    </>
  );
};

export default CommonList;

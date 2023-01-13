import styled from 'styled-components';
import axios from 'axios';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ReactComponent as Search } from '../image/SearchIcon.svg';
import shareListData from '../data/shareListData.json';
import ListHigh from '../components/ListHigh';

const SShareTop = styled.div`
  display: flex;
  flex-direction: row;
  margin-bottom: 50px;
  margin-top: 30px;
  justify-content: space-between;
  p {
    color: #212124;
  }
  .fs-23 {
    font-weight: 700;
    font-size: 22px;
    @media screen and (max-width: 768px) {
      font-size: 18px;
    }
  }
  .fs-16 {
    font-weight: 400;
    font-size: 16px;
    @media screen and (max-width: 768px) {
      font-size: 14px;
    }
  }
  .ml-5 {
    margin-left: 89px;
    @media screen and (max-width: 768px) {
      margin-left: 6%;
    }
  }
  .mb-5 {
    margin-bottom: 5px;
  }
  .search {
    width: 288px;
    height: 41px;
    box-sizing: border-box;
    margin-top: 22px;
    font-size: 20px;
    background: #ffffff;
    border: 1px solid #aaaaaa;
    border-radius: 6px;
    @media screen and (max-width: 1023px) {
      width: 200px;
      margin-left: 20%;
    }
  }
  .search-icon {
    transform: translate(-35px, 5px);
  }
  .searchBox {
    @media screen and (max-width: 1023px) {
    }
  }
`;

const SRegister = styled.button`
  width: 106px;
  height: 41px;
  margin-right: 89px;
  border-radius: 6px;
  border: 1.5px solid #bb2649;
  color: #bb2649;
  font-family: 'Inter';
  font-style: normal;
  font-weight: 400;
  font-size: 18px;
  line-height: 24px;
  align-items: center;
  text-align: center;
  @media screen and (max-width: 1023px) {
    width: 80px;
    height: 41px;
    font-size: 15px;
    transform: translate(360%, -100%);
  }
`;

const SBookContainer = styled.li`
  display: flex;
  flex-direction: column;
  border: 1.5px solid #dddada;
  width: 40%;
  height: 100%;
  padding: 0 18px;
  margin: 1rem;

  @media all and (min-width: 480px) and (max-width: 1023px) {
    display: flex;
    flex-direction: column;
    width: auto;
    padding: 0;
  }
  .coverBox {
    margin-right: 1rem;
    height: 207px;
    float: left;
  }
  .bookCover {
    height: 180px;
    width: 140px;
  }
  .informationBox {
    margin-left: 0;
    margin-right: 1rem;
    color: #212124;
  }
  .fs-18 {
    font-size: 18px;
    font-weight: 700;
    color: #212124;
    @media screen and (max-width: 1023px) {
      font-size: 16px;
    }
  }
  .mfs-16 {
    @media screen and (max-width: 1023px) {
      font-size: 15px;
    }
  }
  .fs-12 {
    font-size: 12px;
  }
  .word-break {
    display: -webkit-box;
    word-break: break-word;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
  .item-flex {
    display: flex;
  }
  .mb-15 {
    margin-bottom: 15px;
  }
  .createdAt-r {
    text-align: right;
    margin: auto;
  }
  .mt-20 {
    margin-top: 0px;
    height: 100%;
  }
  .shareTitle {
    margin: 20px 0px;
    font-size: 20px;
    font-weight: 700;
    text-align: start;
    color: #212124;
    @media all and (min-width: 480px) and (max-width: 1023px) {
      margin-left: 3%;
    }
  }
  .f-row {
    flex-direction: column;
  }
`;

const SBookList = styled.ol`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  float: left;
  width: 100%;
  margin: 0 auto;
  padding-inline-start: 0px;
`;

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
      {/* <ListHigh /> */}
      <SShareTop>
        <div className="ml-5">
          <p className="fs-23 mb-5">현재 빌리지에 올라온 목록입니다!</p>
          <p className="fs-16">찾고있는 책이 있다면 연락해보세요!</p>
        </div>
        <div className="searchBox">
          <input className="search" />
          <Search className="search-icon" />
          <SRegister onClick={() => navigate('/shareAdd')}>
            책 등록하기
          </SRegister>
        </div>
      </SShareTop>
      <SBookList>
        {shareListData.books.map((book) => (
          <SBookContainer key={book.id}>
            <div className="shareTitle">{book.title}</div>
            <div className="f-row">
              <div className="coverBox">
                <img
                  className="bookCover"
                  src={book.image}
                  alt="bookCover"
                  onClick={() => {
                    navigate(`/shareDetail/${id}`);
                  }}
                />
              </div>
              <div className="informationBox">
                <p className="fs-18 mb-15">{book.bookTitle}</p>
                <div className="item-flex">
                  <p className="mfs-16">{book.writer} 저자 /</p>
                  <p className="mfs-16">{book.publisher}</p>
                </div>
                <p className="fs-12 createdAt-r">{book.createdAt}</p>
              </div>
            </div>
            <p className="word-break mt-20 mfs-16">{book.contents}</p>
          </SBookContainer>
        ))}
      </SBookList>
    </>
  );
};
export default ShareList;

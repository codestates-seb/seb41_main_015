import styled from 'styled-components';
import axios from 'axios';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ReactComponent as Search } from '../image/SearchIcon.svg';
import shareListData from '../data/shareListData.json';

const SShareTop = styled.div`
  display: flex;
  margin-bottom: 50px;
  p {
    color: #545454;
  }
  .fs-23 {
    font-weight: 700;
    font-size: 1.43rem;
  }
  .fs-16 {
    font-weight: 400;
    font-size: 1rem;
  }
  .ml-5 {
    margin-left: 5.6rem;
  }
  .mb-5 {
    margin-bottom: 5px;
  }
  .search {
    width: 288px;
    height: 41px;
    box-sizing: border-box;
    position: absolute;
    left: 65%;
    right: 14%;
    top: 13%;
    bottom: 82%;

    background: #ffffff;
    border: 1px solid #aaaaaa;
    border-radius: 6px;
  }
  .search-icon {
    position: absolute;
    left: 81%;
    right: 14%;
    top: 14%;
    bottom: 83%;
  }
`;

const SRegister = styled.button`
  width: 106px;
  height: 41px;
  position: absolute;
  left: 85%;
  right: 14%;
  top: 13%;
  bottom: 83%;
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
`;

const SBookContainer = styled.li`
  display: flex;
  flex-direction: row;
  border: 1.5px solid #dddada;
  width: 40%;
  height: 100%;
  padding: 0 18px;
  margin: 1rem;
  .coverBox {
    margin: 1rem;
    height: 207px;
  }
  .bookCover {
    height: 207px;
    width: 140px;
  }
  .informationBox {
    margin-left: 0;
    margin-right: 1rem;
  }
  .fs-18 {
    font-size: 18px;
    font-weight: 700;
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
    margin-top: 20px;
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
  const [cover, setCover] = useState('');
  const [title, setTitle] = useState('');
  const [writer, setWriter] = useState('');
  const [publisher, setPublisher] = useState('');
  const [createdAt, setCreatedAt] = useState('');

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
      <SShareTop>
        <div>
          <p className="fs-23 ml-5 mb-5">현재 빌리지에 올라온 목록입니다!</p>
          <p className="fs-16 ml-5">찾고있는 책이 있다면 연락해보세요!</p>
        </div>
        <form>
          <input className="search" />
          <Search className="search-icon" />
          <SRegister>책 등록하기</SRegister>
        </form>
      </SShareTop>
      <SBookList>
        {shareListData.books.map((book) => (
          <SBookContainer key={book.id}>
            <div className="coverBox">
              <img className="bookCover" src={book.image} alt="bookCover" />
            </div>
            <div className="informationBox">
              <p className="fs-18 mb-15">{book.title}</p>
              <div className="item-flex">
                <p className="fs-16">{book.writer} 저자 /</p>
                <p className="fs-16">{book.publisher}</p>
              </div>
              <p className="fs-12 createdAt-r">{book.createdAt}</p>
              <p className="word-break mt-20">{book.contents}</p>
            </div>
          </SBookContainer>
        ))}
      </SBookList>
    </>
  );
};
export default ShareList;

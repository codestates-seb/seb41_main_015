import styled from 'styled-components';
import { useState, useEffect } from 'react';
import BookSearch from '../util/Api';

const SModalBackground = styled.div`
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.6);
`;

const SAddModal = styled.div`
  width: 448px;
  height: 490px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border-radius: 5px;
  border: 1px solid #aaaaaa;
  background-color: #ffffff;
  overflow: auto;
  ::-webkit-scrollbar {
    display: none;
  }

  .modalContent {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding-top: 1rem;

    h2 {
      margin-bottom: 30px;
      color: #bb2649;
      margin: 13px;
    }
  }

  .close {
    padding-top: 3px;
    position: absolute;
    right: 13px;
    font-size: 2rem;
    color: #aaaaaa;
    cursor: pointer;
  }
`;

const SModalBox = styled.div`
  box-sizing: border-box;
  padding: 5px;
  margin: 10px auto;
  .inputBox {
    display: flex;
    align-items: flex-start;
  }
  .bookSearch {
    width: 340px;
    padding: 8px;
    font-size: 16px;
  }
  .search {
    margin-left: 10px;
    padding: 10px;
    border: 1px solid #bb2649;
    border-radius: 3px;
    color: #bb2649;
    :hover {
      color: #ffffff;
      background-color: #bb2649;
    }
  }
`;

const SRenderBox = styled.div`
  width: 400px;
  border: 1px solid #aaaaaa;
  border-radius: 5px;
  padding: 10px;
  margin-bottom: 5px;
  color: #303030;
  :hover {
    background-color: #f0f5ff;
  }
  .bookImg {
    display: none;
  }
  .bookname {
    font-size: 16.5px;
    font-weight: 600;
    margin-bottom: 4px;
  }
  .authors {
    font-size: 13px;
    margin-right: 7px;
  }
  .publisher {
    margin-left: 7px;
  }
`;

const BookAddModal = ({
  isModalOpen,
  handleCloseModal,
  onBookInfoChange,
  inputs,
  isRate = false,
}) => {
  const [bookList, setBookList] = useState([]);
  const [text, setText] = useState('');
  const [query, setQuery] = useState('');

  useEffect(() => {
    if (query.length > 0) {
      bookSearchHttpHandler(query, true);
    }
  }, [query]);

  const handleEnter = (e) => {
    if (e.keyCode === 13) {
      setQuery(text);
    }
  };
  const handleClickSearch = (e) => {
    setQuery(text);
  };

  const hadleTextUpdate = (e) => {
    setText(e.target.value);
  };

  const handleChoose = (chooseBook) => {
    const { title, authors, publisher, thumbnail, isbn } = chooseBook;

    if (isRate === true) {
      onBookInfoChange({
        ...inputs,
        bookTitle: title,
        author: authors[0] === undefined ? '저자없음' : authors[0],
        publisher: publisher,
        thumbnail: thumbnail,
        isbn: isbn,
      });
    } else {
      onBookInfoChange({
        ...inputs,
        bookTitle: title,
        author: authors[0] === undefined ? '저자없음' : authors[0],
        publisher: publisher,
        thumbnail: thumbnail,
      });
    }

    handleCloseModal();
  };

  const bookSearchHttpHandler = async (query, reset) => {
    const params = {
      query: query,
      sort: 'accuracy',
      page: 1,
      size: 10,
    };

    const { data } = await BookSearch(params);
    if (reset) {
      setBookList(data.documents);
    } else {
      setBookList(bookList.concat(data.documents));
    }
  };

  return (
    <>
      {isModalOpen ? (
        <SModalBackground onClick={handleCloseModal}>
          <SAddModal onClick={(e) => e.stopPropagation()}>
            <div className="close" onClick={handleCloseModal}>
              &times;
            </div>
            <div className="modalContent">
              <h2>책 제목을 입력해주세요</h2>
              <SModalBox>
                <div className="inputBox">
                  <input
                    className="bookSearch"
                    type="search"
                    placeholder="검색어를 입력해 주세요."
                    name="query"
                    onKeyDown={handleEnter}
                    onChange={hadleTextUpdate}
                    value={text}
                    autoComplete="off"
                  />
                  <button className="search" onClick={handleClickSearch}>
                    검색
                  </button>
                </div>
                <>
                  {bookList.map((book, idx) => (
                    <SRenderBox key={idx} onClick={() => handleChoose(book)}>
                      <div className="bookname">{book.title}</div>
                      <span className="authors">{book.authors}</span>|
                      <span className="publisher">{book.publisher}</span>
                      <img
                        alt="bookImg"
                        className="bookImg"
                        src={book.thumbnail}
                      />
                    </SRenderBox>
                  ))}
                </>
              </SModalBox>
            </div>
          </SAddModal>
        </SModalBackground>
      ) : null}
    </>
  );
};

export default BookAddModal;

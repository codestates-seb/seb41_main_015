import Pagination from 'react-js-pagination';
import styled from 'styled-components';

const SContainer = styled.div`
  margin: 20px 0px;
  ul {
    color: #686868;
    display: flex;
    gap: 10px;
    justify-content: center;
    & > li.active {
      color: black;
      font-weight: 600;
      border-bottom: 2px solid #bb2649;
    }
  }
  li {
    /* width: 30px; */
    /* border: 1px solid #c8c8c8; */
    padding: 4px 13px;
    border-radius: 2px;
    &:hover {
      color: #bb2649;
      /* background-color: #dcdcdc; */
      cursor: pointer;
    }
  }
`;

const Paging = ({ page, count, handlePageChange }) => {
  return (
    <SContainer>
      <Pagination
        activePage={page}
        itemsCountPerPage={6}
        totalItemsCount={count}
        pageRangeDisplayed={5}
        prevPageText={'<'}
        nextPageText={'>'}
        onChange={handlePageChange}
      />
    </SContainer>
  );
};

export default Paging;

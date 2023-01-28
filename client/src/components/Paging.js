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
    padding: 4px 13px;
    border-radius: 2px;
    &:hover {
      color: #bb2649;
      cursor: pointer;
    }
  }
`;

const Paging = ({ page, count, perPage, handlePageChange }) => {
  return (
    <SContainer>
      <Pagination
        activePage={page}
        itemsCountPerPage={perPage}
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

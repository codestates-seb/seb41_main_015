import styled from 'styled-components';
import dummy from "../data/data.json";
import axios from 'axios';


const SBoxA = styled.div`
  overflow:scroll;
height:300px
`;
const SBoxB = styled.div`
overflow:scroll;
height:120px

`;
const SBoxC = styled.div`
overflow:scroll;
height:120px
`;
const SProfile = styled.td`
  width:30%;
`;

const SInformation = styled.button`
  width: 90px;
  height: 33px;
  font-size: 16px;
  font-weight: 600;
  color: #bb2649;
  border: 1px solid #bb2649;
  border-radius: 20px;
  :hover {
    color: #ffffff;
    background-color: #bb2649;
  }
`;
const SText = styled.h4`
  width: 90px;
  height: 33px;
  font-size: 16px;
  font-weight: 600;
  color: #bb2649;
  right:30px;
  text-align:center;
`;
const MyPage = () => {
  const books = dummy.books;
  const comments = dummy.comment;
  const community = dummy.community;
  console.log(comments);
  return <>
    <table style={{ width: "80%", height: "100%"}}>
    
      <tbody>
        <tr>
          <td><SText>마이 페이지</SText> </td>
          <td></td>
          <td style={{textAlign:"center"}}><SInformation>정보수정</SInformation></td>
        </tr>
       <tr>
       <td colSpan={3}> <hr></hr></td>
       </tr>
        <tr>
          <td  colSpan={1} style={{textAlign:"center"}}>
            <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQve-F1mablIoZC6aIu2aVyBwDsaCAUu2863A&usqp=CAU" style={{borderRadius:"50%"}} alt="..."></img>
          </td>
          <td >
            이메일 : seb41mainporject@naver.com
            <br></br>
            닉네임 : Groot
          </td>
          <td></td>
        </tr>
  
        <tr>
          <td>
            <h5>나눔 중</h5>
            <SBoxA>
              {
                books.map((book) => (
                  <div style={{border:"1px solid black"}}>
                    <tr>제목 : {book.name}</tr>
                    <tr>순서 : {book.id}</tr>
                    <tr>작성자 : {book.author}</tr>
                  </div>
                ))
              }
            </SBoxA>
          </td>
          <td>
            <h5>커뮤니티 활동</h5>

            <SBoxB>
              {
                community.map((communities) => (
                  <div class="border-bottom border-dark">
                    <tr>제목 : {communities.name} </tr>
                    <tr>북클럽이름 : {communities.bookclub} </tr>
                    <tr>책 제목 : {communities.bookname} </tr>
                  </div>
                ))
              }
            </SBoxB>

            <h5>댓글</h5>
            <SBoxC>
              {
                comments.map((comment) => (
                  <>
                    <tr className='bg-secondary bg-opacity-50'><strong>{comment.id} : {comment.comment}</strong></tr>
                  </>
                ))
              }
            </SBoxC>
          </td>
        </tr>
       

       
      </tbody>

    </table>
  </>;
};
export default MyPage;

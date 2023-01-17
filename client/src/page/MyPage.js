import axios from 'axios';
import { useEffect, useState } from 'react';
import styled from 'styled-components';
import dummy from "../data/data.json";
import {useNavigate} from 'react-router-dom';

const STable = styled.table`
margin-left:auto;
margin-right:auto;

`;

const SOverflow = styled.div`
overflow-y:auto;
height:700px;
`;

const SBoxA = styled.div`
overflow: scroll;
::-webkit-scrollbar{width: 4px;}
::-webkit-scrollbar-thumb{background-color: #bb2649; border-radius: 10px;}
height:300px;
padding: 10px;
`;

const SBoxB = styled.div`
overflow:scroll;
::-webkit-scrollbar{width: 4px;}
::-webkit-scrollbar-thumb{background-color: #bb2649; border-radius: 10px;}
height:300px;
padding: 10px;

`;



const SProfile = styled.td`
font-size: 14px;
font-weight: 600;
transform: translateX(50px);

`;

const SDivide = styled.div`
border : 1px solid black;

`;

const SInformation = styled.button`
width: 90px;
  height: 33px;
  font-size: 16px;
  font-weight: 600;
  color: #bb2649;
  border: 1px solid #bb2649;
  border-radius: 10px;
  :hover {
    color: #ffffff;
    background-color: #bb2649;
  }
  margin-right:54px;
`;


const SInformationtd = styled.td`
text-align:right;
width:50%;
`;
const SMyPageText = styled.h3`
  width: 200px;
  height: 10px;
  font-weight: 750;
  font-size: 30px;
  text-align:right;
`;
const SText = styled.td`
  font-size: 13px;
`;
const SCommunity = styled.div`
border-bottom:1px solid black;
`;
const SComment = styled.strong`
background : lightgray;
`;

const SHR = styled.hr`
margin-bottom: 50px;
`;

const SImage = styled.img`
border-radius:50%;
width:150px;
height:150px;
margin-left:50px;

`;

const SDivideImage = styled.img`
width:70%;
display:flex;
`;

const STitle = styled.h1`
  margin-left: 54px;
  height: 30px;
  font-size: 30px;
  font-weight: 730;
`;

const STitle2 = styled.h2`
  margin-left: 10px;
  font-size: 20px;
 
`;

const STextStrong = styled.strong`
  font-size: 18px;
`;



const MyPage = () => {
  const books = dummy.books;
  const comments = dummy.comment;
  const community = dummy.community;
  const navigate = useNavigate();
  const Nickname = "Groot"; 
  // const [books,setBooks] = useState();
  // const [comments,setComments] = useState();
  // const [community,setcommunity] = useState();
  // useEffect(()=>{
  //   axios.get("http://localhost~")
  //   .then(() => {
  //     ;
  //   })
  //   .catch((err) => {
  //     alert('데이터가 불러오기 실패');
  //     console.error(err);
  //   });

  //   axios.get("http://localhost~")
  //   .then(() => {
  //    ;
  //   })
  //   .catch((err) => {
  //     alert('데이터가 불러오기 실패');
  //     console.error(err);
  //   });

  //   axios.get("http://localhost~")
  //   .then(() => {
  //     ;
  //   })
  //   .catch((err) => {
  //     alert('데이터가 불러오기 실패');
  //     console.error(err);
  //   });
  // })

  return <>
    <SOverflow>
      <STable>
        <tbody>
          <tr>
            <td><STitle >마이 페이지</STitle> </td>
            <SInformationtd><SInformation onClick={() => navigate('/mypageEdit')}>정보수정</SInformation></SInformationtd>
          </tr>
          <tr>
            <td colSpan={2}>
              <SHR></SHR>
            </td>
          </tr>
    
          <tr>
            <td>
              <tr>
                <td><SImage src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQve-F1mablIoZC6aIu2aVyBwDsaCAUu2863A&usqp=CAU" alt="..."></SImage></td>
                <SProfile>
                  이메일
                  <br></br>
                  seb41mainporject@naver.com
                  <br></br>
                  <br></br>
                  닉네임
                  <br></br>
                  {Nickname}
                </SProfile>
              </tr>
            </td>
            <td>
            </td>
          </tr>
          <tr>
            <td>
              <STitle2>{Nickname}의 나눔 세상</STitle2>
              <SBoxA>
                {
                  books.map((book) => (
                    <>
                      <SDivide>
                        <table>
                          <tr>
                            <td><SDivideImage src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9sYgypcA8MhjHyNJD7-u9L0EjZj7YomKVTA&usqp=CAU" alt="..."></SDivideImage></td>
                            <td>
                              <tr>
                                <td><STextStrong>{book.name}</STextStrong></td>
                              </tr>
                              <tr>
                                <td><strong>책 제목</strong> : {book.name}</td>
                              </tr>
                              <tr>
                                <SText>저자 : {book.author}, 출판사 : {book.author}</SText>
                              </tr>
                              <tr>
                                <SText>작성일자 : {book.id}</SText>
                              </tr>
                            </td>
                          </tr>
                        </table>

                      </SDivide>
                      <br></br>
                    </>
                  ))
                }
              </SBoxA>
            </td>
            <td>
              <STitle2>우리{Nickname}의 커뮤 활동 </STitle2>
              <SBoxB>
                {
                  community.map((communities) => (
                    <SCommunity>
                      <tr>제목 : {communities.name} </tr>
                      <tr>북클럽이름 : {communities.bookclub} </tr>
                      <tr>책 제목 : {communities.bookname} </tr>
                      <br></br>
                    </SCommunity>
                  ))
                }
              </SBoxB>
             
            </td>
          </tr>
        </tbody>
      </STable>
    </SOverflow>
  </>;
};
export default MyPage;

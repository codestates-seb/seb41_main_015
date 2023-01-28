import { useEffect, useState } from 'react';
import styled from 'styled-components';
// import axios from 'axios';
import Paging from '../components/Paging';

import { prettyDate } from '../util/dateparse';
import { useNavigate } from 'react-router-dom';
import instanceAxios from '../reissue/InstanceAxios';

const STable = styled.table`
  /* margin-left: auto;
  margin-right: auto; */
  display: flex;
  justify-content: center;
  flex-direction: row;
`;


const SBoxA = styled.div`
  // overflow: scroll;
  // ::-webkit-scrollbar {
  //   width: 4px;
  // }
  // ::-webkit-scrollbar-thumb {
  //   background-color: #bb2649;
  //   border-radius: 2%;
  // }
  height: 500px;
  padding: 10px;
  width: 800px;
  border-radius: 50%;


`;

// const SBoxB = styled.div`
//   overflow: scroll;
//   ::-webkit-scrollbar {
//     width: 4px;
//   }
//   ::-webkit-scrollbar-thumb {
//     background-color: #bb2649;
//     border-radius: 10px;
//   }
//   height: 300px;
//   padding: 10px;
//   -moz-border-radius: 3px;
//   -webkit-border-radius: 3px;
//   border-radius: 3px;
// `;

const SProfile = styled.td`
  font-size: 14px;
  font-weight: 600;
  transform: translateX(50px);
`;

const SDivide = styled.div`
  border: 1px solid #acacac;
  height: 150px;
  border-radius: 2.5px;
  display: flex;
    padding-left: 10px;
    &:hover {
    background-color: #e3e3e369;
    transition: 0.2s;
   cursor: pointer;
    }
 
`;

const Str = styled.div`
    font-size: 13px;
    font-weight: 395; 
    ;
`;

const STr2 = styled.strong`
margin-left: 10px;
font-size: 18px;
color: #bb2649;
`;

const SInformation = styled.button`
  width: 90px;
  height: 33px;
  font-size: 16px;
  font-weight: 600;
  float: right;
  color: #bb2649;
  border: 1px solid #bb2649;
  border-radius: 10px;
  :hover {
    color: #ffffff;
    background-color: #bb2649;
  }
  margin-left: 10%;
  
  transform: translateX(5.2em);
  /* transform: translateY(0.1in);
  */
`;

const SInformationtd = styled.td`
  text-align: right;
  width: 50%;
`;
// const SMyPageText = styled.h3`
//   width: 200px;
//   height: 10px;
//   font-weight: 750;
//   font-size: 30px;
//   text-align: right;
//`;
const SText = styled.td`
  font-size: 13px;
`;
// const SCommunity = styled.div`
//   border-bottom: 1px solid black;
// `;
// const SComment = styled.strong`
//   background: lightgray;
// `;

// const SHR = styled.hr`
//   margin-bottom: 50px;
// `;

const SImage = styled.img`
  border-radius: 50%;
  width: 150px;
  height: 150px;
  margin-left: 50px;
`;

const SDivideImage = styled.img`
  width: 70%;
  display: flex;
  display : flex;
 align-items: center;
 justify-content: center;
 align-content: center;
  
`;

const STitle = styled.div`
  h2 {
    color: #2c2c2c;
    padding: 18px;
    margin: 20px 10%;
    border-bottom: 1px solid #acacac;
  }
`;

const STitle2 = styled.h2`
  margin-left: 10px;
  font-size: 20px;
  
  color: #acacac;
    &:hover {
      cursor: default;
    }
  
`;

const STextStrong = styled.strong`
  font-size: 18px;

`;

const SShareList= styled.tr`
 display : flex;
 align-items: center;
 justify-content: center;
 align-content: center;
  
`;

const MyPage = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [imgUrl, setImgUrl] = useState('');
  const [data, setData] = useState([]);

  const [page, setPage] = useState(1);
  const [count, setCount] = useState(0); // 총 데이터 개수
  const PER_PAGE = 3;
  // const url = 'https://serverbookvillage.kro.kr/';

  useEffect(() => {
    // 회원 정보 받아오는 코드
    instanceAxios.get('/v1/members').then((res) => {
      console.log(res);
      setEmail(res.data.data.email);
      setNickname(res.data.data.displayName);
      setImgUrl(res.data.data.imgUrl);
    });

    // 내가 나눔한 목록 받아오는 코드 (페이지네이션 필요)
    instanceAxios
      .get(`/v1/borrows/mine?page=0&size=${PER_PAGE}&sort=createdAt%2Cdesc`)
      .then((res) => {
        console.log('나눔 목록: ', res);
        setData(res.data.data);
        setCount(res.data.pageInfo.totalElements);
      });
  }, []);

  //pagenation 페이지네이션 컴포넌트에서 각 숫자를 눌렀을 때 작동하는 함수
  const handlePageChange = async (page) => {
    setPage(page);
    const res = await instanceAxios.get(
      `/v1/borrows/mine?page=${page - 1}&size=${PER_PAGE}&sort=createdAt%2Cdesc`
    );
    setData(res.data.data);
  };

  return (
    <>
      
        <STitle>
          <h2>마이 페이지</h2>
        </STitle>
        <div>
          
        </div>
        <STable>
          <tbody>
            <tr>
              <SInformationtd></SInformationtd>
            </tr>
            <tr>
              <td colSpan={2}></td>
            </tr>

            <tr>
              <td>
                <tr>
                  <td>
                    {imgUrl.length !== 0 ? (
                      <SImage src={imgUrl} alt="img from share"></SImage>
                    ) : (
                      <SImage
                        src="	https://img.icons8.com/windows/32/null/user-male-circle.png"
                        alt="when it hasn't been uploaded"
                      ></SImage>
                    )}
                  </td>
                  <SProfile>
                    이메일
                    <br></br>
                    <Str>
                    {email}
                    </Str>
                    <br></br>
                    <br></br>
                    닉네임
                    <br></br>
                    <Str>
                    {nickname}
                    </Str>
                  </SProfile>
                </tr>
              </td>
              <SInformation onClick={() => navigate('/mypageEdit')}>
            정보수정
          </SInformation>
              <td></td>
            </tr>
            <SShareList>
              <td>
                <STitle2><STr2>{nickname}</STr2> 의 나눔목록</STitle2>
                <SBoxA>
                  {data.length !== 0 ? (
                    data.map((book) => (
                <>
                        <SDivide onClick={() => navigate(`/shareDetail/${book.borrowId}`)}>
                          <table>
                            <tr>
                              <td>
                                <SDivideImage
                                  src={book.thumbnail}
                                  alt="book img"
                                ></SDivideImage>
                              </td>
                              <td>
                                <tr>
                                  <td>
                                    <STextStrong>{book.title}</STextStrong>
                                  </td>
                                </tr>
                                <tr>
                                  <td>
                                    <strong>책 제목</strong> : {book.bookTitle}
                                  </td>
                                </tr>
                                <tr>
                                  <SText>저자 : {book.author}</SText>
                                  <SText>출판사 : {book.author}</SText>
                                </tr>
                                <tr>
                                  <SText>
                                    작성일자 : {prettyDate(book.createdAt)}
                                  </SText>
                                </tr>
                              </td>
                            </tr>
                          </table>
                        </SDivide>

                        <br></br>
                      </>
                    ))
                  ) : (
                    <SDivide>
                      <table>
                        <tr>데이터가없습니다.</tr>
                      </table>
                    </SDivide>
                  )}
                </SBoxA>
                <Paging
                  page={page}
                  count={count}
                  perPage={PER_PAGE}
                  handlePageChange={handlePageChange}
                />
              </td>
              {/* <td>
                <STitle2>{nickname} 의 커뮤니티 </STitle2>
                <SBoxB>
                  <SDivide>
                    <table>
                      <tr>데이터가없습니다.</tr>
                    </table>
                  </SDivide>
                </SBoxB>
                <Pagingtwo
                  page={page}
                  count={count}
                  perPage={PER_PAGE}
                  handlePageChange={handlePageChange}
                />
              </td> */}
            </SShareList>
          </tbody>
        </STable>
      
    </>
  );
};
export default MyPage;
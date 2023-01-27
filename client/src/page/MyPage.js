import { useEffect, useState } from 'react';
import styled from 'styled-components';
import axios from 'axios';

import { useNavigate } from 'react-router-dom';
import instanceAxios from '../reissue/InstanceAxios';
import Paging from '../components/Paging';

const STable = styled.table`
  /* margin-left: auto;
  margin-right: auto; */
  display: flex;
  justify-content: center;
  flex-direction: row;
`;

const SOverflow = styled.div`
  overflow-y: auto;
  height: 700px;
`;

const SBoxA = styled.div`
  overflow: scroll;
  ::-webkit-scrollbar {
    width: 4px;
  }
  ::-webkit-scrollbar-thumb {
    background-color: #bb2649;
    border-radius: 10px;
  }
  height: 300px;
  padding: 10px;
`;

const SBoxB = styled.div`
  overflow: scroll;
  ::-webkit-scrollbar {
    width: 4px;
  }
  ::-webkit-scrollbar-thumb {
    background-color: #bb2649;
    border-radius: 10px;
  }
  height: 300px;
  padding: 10px;
`;

const SProfile = styled.td`
  font-size: 14px;
  font-weight: 600;
  transform: translateX(50px);
`;

const SDivide = styled.div`
  border: 1px solid black;
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
  margin-right: 10%;
`;

const SInformationtd = styled.td`
  text-align: right;
  width: 50%;
`;
const SMyPageText = styled.h3`
  width: 200px;
  height: 10px;
  font-weight: 750;
  font-size: 30px;
  text-align: right;
`;
const SText = styled.td`
  font-size: 13px;
`;
const SCommunity = styled.div`
  border-bottom: 1px solid black;
`;
const SComment = styled.strong`
  background: lightgray;
`;

const SHR = styled.hr`
  margin-bottom: 50px;
`;

const SImage = styled.img`
  border-radius: 50%;
  width: 150px;
  height: 150px;
  margin-left: 50px;
`;

const SDivideImage = styled.img`
  width: 70%;
  display: flex;
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
`;

const STextStrong = styled.strong`
  font-size: 18px;
`;

const MyPage = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [imgUrl, setImgUrl] = useState('');
  const [data, setData] = useState([]);

  const url = 'https://serverbookvillage.kro.kr/';
 
  const [page, setPage] = useState(1);

  // 총 데이터 개수
  const [count, setCount] = useState(0);
  const PER_PAGE = 4;

  useEffect(() => {
    setPage(1);
    setData([]);

     axios
     .get(url + `/v1/borrows/mine?page=0&size=${PER_PAGE}&sort=createdAt%2Cdesc`)
     .then((res) => {
       setData(res.data.data);
       setCount(res.data.pageInfo.totalElements);
       setPage(1);
     });

    instanceAxios.get('/v1/members').then((res) => {
      console.log(res);
      setEmail(res.data.data.email);
      setNickname(res.data.data.displayName);
      setImgUrl(res.data.data.imgUrl);
    });

    instanceAxios
      .get('/v1/borrows/mine?page=0&size=10&sort=createdAt%2Cdesc')
      .then((res) => {
        console.log(res);
        setData(res.data.data);
      });
  }, []);



  // const getDatabyPage = async (page);
  //     const res = await axios.get(
  //       url +
  //         `v1/borrows/mine?page=${
  //           page - 1
  //         }&size=${PER_PAGE}&sort=createdAt%2Cdesc`
  //     );
  //     const data = res.data;
  //     return data;

  //   };
  // const handlePageChange = async (page) => {
  //   setPage(page);
  //     const pageData = await getDatabyPage(page);
  //     setData(pageData.data);
  // };
  



  return (
    <>
      <SOverflow>
        <STitle>
          <h2>마이 페이지</h2>
        </STitle>
        <div>
          <SInformation onClick={() => navigate('/mypageEdit')}>
            정보수정
          </SInformation>
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
                    <SImage
                      src={imgUrl}
                      alt="..."
                    ></SImage>
                    ) : (
                      <SImage
                      src="	https://img.icons8.com/windows/32/null/user-male-circle.png"
                      alt="..."
                    ></SImage>
                    )}
                  </td>
                  <SProfile>
                    이메일
                    <br></br>
                    {email}
                    <br></br>
                    <br></br>
                    닉네임
                    <br></br>
                    {nickname}
                  </SProfile>
                </tr>
              </td>
              <td></td>
            </tr>
            <tr>
              <td>
                <STitle2>{nickname}의 나눔 세상</STitle2>
                <SBoxA>
                  {data.length !== 0 ? (
                    data.map((book) => (
                      <>
                        <SDivide>
                          <table>
                            <tr>
                              <td>
                                <SDivideImage
                                  src={book.imgUrl}
                                  alt="..."
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
                                  <SText>작성일자 : {book.id}</SText>
                                </tr>
                              </td>
                            </tr>
                          </table>
                          <Paging
                            page={page}
                            count={count}
                            perPage={PER_PAGE}
                            // handlePageChange={handlePageChange}
                            
                          />
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
              </td>
              <td>
                <STitle2>우리{nickname}의 커뮤 활동 </STitle2>
                <SBoxB>
                  <SDivide>
                    <table>
                      <tr>데이터가없습니다.</tr>
                    </table>
                  </SDivide>
                </SBoxB>
              </td>
            </tr>
          </tbody>
        </STable>
      </SOverflow>
    </>
  );
};
export default MyPage;

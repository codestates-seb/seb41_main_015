import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import Paging from '../components/Paging';
import { prettyDate } from '../util/dateparse';
import { useNavigate } from 'react-router-dom';
import instanceAxios from '../reissue/InstanceAxios';

const STable = styled.table`
  display: flex;
  justify-content: center;
  flex-direction: row;

  .toEdit {
    transform: translate(384px, -55px);
  }
`;

const SBoxA = styled.div`
  height: 500px;
  padding: 10px;
  width: 800px;
  border-radius: 50%;
`;

const SProfile = styled.td`
  font-size: 14px;
  font-weight: 600;
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
  margin: 18px;
  color: #bb2649;
  border: 1px solid #bb2649;
  border-radius: 10px;
  :hover {
    color: #ffffff;
    background-color: #bb2649;
  }
`;

const SInformationtd = styled.td`
  text-align: right;
  width: 50%;
`;
const SText = styled.td`
  font-size: 13px;
`;

const SImage = styled.img`
  border-radius: 50%;
  width: 150px;
  height: 150px;
  margin-left: 50px;
  margin-right: 30px;
`;

const SDivideImage = styled.img`
  width: 70%;
  display: flex;
  align-items: center;
  justify-content: center;
  align-content: center;
`;

const STitle = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 20px 10%;
  border-bottom: 1px solid #acacac;
  h2 {
    color: #2c2c2c;
    padding: 18px;
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

const SShareList = styled.tr`
  display: flex;
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

  useEffect(() => {
    // 회원 정보 받아오는 코드
    instanceAxios.get('/v1/members').then((res) => {
      setEmail(res.data.data.email);
      setNickname(res.data.data.displayName);
      setImgUrl(res.data.data.imgUrl);
    });

    // 내가 나눔한 목록 받아오는 코드 (페이지네이션 필요)
    instanceAxios
      .get(`/v1/borrows/mine?page=0&size=${PER_PAGE}&sort=createdAt%2Cdesc`)
      .then((res) => {
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
        <SInformation onClick={() => navigate('/mypageEdit')}>
          정보수정
        </SInformation>
      </STitle>
      <div></div>
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
              <Str>{email}</Str>
              <br></br>
              <br></br>
              닉네임
              <br></br>
              <Str>{nickname}</Str>
            </SProfile>
            <td className="toEdit"></td>
            <td></td>
          </tr>
          <SShareList>
            <td>
              <STitle2>
                <STr2>{nickname}</STr2> 의 나눔목록
              </STitle2>
              <SBoxA>
                {data.length !== 0 ? (
                  data.map((book) => (
                    <React.Fragment key={book.borrowId}>
                      <SDivide
                        onClick={() =>
                          navigate(`/shareDetail/${book.borrowId}`)
                        }
                      >
                        <table>
                          <tbody>
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
                          </tbody>
                        </table>
                      </SDivide>

                      <br></br>
                    </React.Fragment>
                  ))
                ) : (
                  <SDivide>
                    <table>
                      <tbody>
                        <tr>
                          <td>데이터가 없습니다.</td>
                        </tr>
                      </tbody>
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
          </SShareList>
        </tbody>
      </STable>
    </>
  );
};
export default MyPage;

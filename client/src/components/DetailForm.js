import { Link, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import ToggleSwitch from './ToggleSwitch';
import ShareStatus from './ShareStatus';
import instanceAxios from '../reissue/InstanceAxios';
import { prettyDate } from '../util/dateparse';
import { ReactComponent as KakaoFill } from '../image/kakaofill.svg';
import { ReactComponent as Eye } from '../image/eye.svg';
import Swal from 'sweetalert2';

const SDetailLayout = styled.main`
  padding: 24px;
  min-height: calc(100vh - 60px - 280px);

  .container {
    max-width: 1280px;
    margin: 0 auto;
  }
`;

const SDetailWrap = styled.div`
  display: flex;
  justify-content: center;
  gap: 35px;

  img {
    margin: 24px 24px 24px 0px;
    width: 250px;
  }

  @media screen and (max-width: 1100px) {
    flex-direction: column;
    align-items: center;
  }
`;

const SRightSide = styled.div`
  margin: 24px;
  /* border: 1px solid green; */
  width: calc(100% - 610px);

  .controlButtons {
    flex-shrink: 0;
    color: #aaaaaa;
    margin-right: 8px;
  }

  .betweenButtons {
    margin: 5px;
  }

  .controlButton {
    &:hover {
      color: #bb2649;
      cursor: pointer;
    }
  }

  .description {
    font-size: 1.05rem;
  }
  @media screen and (max-width: 1100px) {
    width: 100%;
    padding: 0px 24px;
  }
`;

const STopWrap = styled.div`
  padding-bottom: 5px;
  border-bottom: 1px solid #aaaaaa;
  /* max-width: 677px; */

  h1 {
    margin: 10px 0px;
  }

  .titleAndButton {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 40px;
  }
`;

const SAuthorAndStatus = styled.div`
  display: flex;
  justify-content: space-between;
  /* align-items: center; */
  margin-bottom: 10px;

  .authorInfo {
    display: flex;
    align-items: center;
    gap: 10px;
    padding-left: 3px;

    .author {
      display: flex;
      align-items: center;
      gap: 5px;
    }

    img {
      margin: 0;
      width: 28px;
      height: 28px;
      border-radius: 70%;
    }

    .views {
      color: #aaaaaa;
      font-size: 0.8rem;
      display: flex;
      align-items: center;
      gap: 3px;
    }

    .createdAt {
      color: #aaaaaa;
      font-size: 0.8rem;
      @media screen and (max-width: 527px) {
        display: none;
      }
    }
  }

  .onlyInShare {
    display: none;
  }
`;

const SBookInfo = styled.div`
  margin-top: 20px;
  margin-bottom: 40px;
  /* border: 1px solid #bb2649; */
  background-color: #fffbeac2;
  border-radius: 5px;
  /* border-bottom: 1px solid #aaaaaa; */
  padding: 24px 12px 5px 24px;

  h2 {
    margin-top: 0;
  }

  div {
    color: #000000;
    margin: 5px 0px;
    font-weight: 600;
    font-size: 1rem;
    span {
      color: #505050;
    }
  }
`;

const SContact = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: center;

  div {
    font-weight: 600;
    margin-right: 15px;
    color: #7c7c7c;
    font-size: 0.9rem;
  }

  button {
    border: none;
    border-radius: 5px;
    background-color: #f9e000;
    padding: 12px;
    margin: 10px 5px 10px 0px;
    font-weight: 600;
    font-size: 0.9rem;

    display: flex;
    justify-content: center;
    align-items: center;
    gap: 5px;
    flex-shrink: 0;
  }
`;

const DetailForm = ({ data, page, id }) => {
  const navigate = useNavigate();
  const endpoint = page === 'share' ? 'borrows' : 'requests';

  // ????????? ??? ?????? ????????? ??????, ?????? ????????? ??? ????????? ???
  const currentUser = sessionStorage.getItem('displayName');
  const isSameUser = data.displayName === currentUser ? true : false;

  const onlyInShare = page === 'share' ? '' : 'onlyInShare';

  // ?????? ?????? ?????????
  const handleDelete = () => {
    // ????????? ?????? ?????? ????????? (instanceAxios ??????)
    Swal.fire({
      title: '????????? ?????????????????????????',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#bb2649',
      confirmButtonText: '??????',
      cancelButtonText: '??????',
    }).then((res) => {
      if (res.isConfirmed) {
        instanceAxios
          .delete(`v1/${endpoint}/${id}`)
          .then(() => {
            Swal.fire('?????? ?????????????????????.');
            navigate(-1);
          })
          .catch((err) => {
            Swal.fire('????????? ??????????????????');
            console.error(err);
          });
      }
    });
  };

  // ?????? ?????????
  const cover = data.thumbnail
    ? data.thumbnail
    : 'https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg';

  // ????????? ?????? ?????? ?????????
  const profile = data.imgUrl
    ? data.imgUrl
    : 'https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/309/59932b0eb046f9fa3e063b8875032edd_crop.jpeg';

  return (
    <SDetailLayout>
      <div className="container">
        <SDetailWrap>
          <div>
            <img alt="??? ??????" src={cover} />
          </div>
          <SRightSide>
            <STopWrap>
              <div className="titleAndButton">
                <h1>{data.title}</h1>
                {isSameUser ? (
                  <div className="controlButtons">
                    <Link
                      to={
                        page === 'request'
                          ? `/reqEdit/${id}`
                          : `/shareEdit/${id}`
                      }
                    >
                      <span className="controlButton">??????</span>
                    </Link>
                    <span className="betweenButtons">|</span>
                    <span className="controlButton" onClick={handleDelete}>
                      ??????
                    </span>
                  </div>
                ) : null}
              </div>
              <SAuthorAndStatus>
                <div className="authorInfo">
                  <div className="author">
                    <img alt="profileImage" src={profile} />
                    <div>{data.displayName}</div>
                  </div>
                  <div className="views">
                    <Eye width="14px" height="14px" />
                    {data.view}
                  </div>
                  <div className="createdAt">{prettyDate(data.createdAt)}</div>
                </div>
                <div className={onlyInShare}>
                  {isSameUser ? (
                    <ToggleSwitch id={id} status={data.borrowWhthr} />
                  ) : (
                    <ShareStatus status={data.borrowWhthr} />
                  )}
                </div>
              </SAuthorAndStatus>
            </STopWrap>
            <SBookInfo>
              <h2>{data.bookTitle}</h2>
              <div>
                ??????: <span>{data.author}</span>
              </div>
              <div>
                ?????????: <span>{data.publisher}</span>
              </div>
              <SContact>
                <div>
                  {page === 'request'
                    ? '??? ?????? ?????? ?????????????'
                    : '??? ?????? ?????? ?????????????'}
                </div>
                <button
                  onClick={() => {
                    window.open(data.talkUrl);
                  }}
                >
                  <KakaoFill width="16" height="16" />
                  ?????????????????? ????????????
                </button>
              </SContact>
            </SBookInfo>
            <p className="description">{data.content}</p>
          </SRightSide>
        </SDetailWrap>
      </div>
    </SDetailLayout>
  );
};

export default DetailForm;

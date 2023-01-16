import { useState, useEffect } from 'react';
import NicknameModal from '../components/NicknameModal';

const Home = () => {
  // 모달을 띄워보자
  const [isModalOpen, setIsModalOpen] = useState(false);

  // const handleOpenModal = () => {
  //   setIsModalOpen(true);
  // }

  useEffect(() => {
    // membership 상태에 따라 바꾸기
    // sessionStorage.getItem('membership')
    // 액세스 토큰이 있고 (로그인함) membership = new일 때만 모달 띄우기
    if (
      sessionStorage.getItem('accessToken') &&
      sessionStorage.getItem('membership') === 'new'
    ) {
      setIsModalOpen(true);
    }
  }, []);

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  // const checklogin = () => {
  //   if (sessionStorage.getItem('accessToken')) {
  //     console.log('토큰 있음');
  //     return;
  //   }
  //   console.log('토큰 없음');
  //   return;
  // };

  // useEffect(() => {
  //   checklogin();
  // });

  return (
    <>
      {/* <button onClick={handleOpenModal}>모달 열기</button> */}
      {isModalOpen ? (
        <NicknameModal
          isModalOpen={isModalOpen}
          handleCloseModal={handleCloseModal}
        />
      ) : null}
      <p>홈 화면 부분입니다!</p>
    </>
  );
};

export default Home;

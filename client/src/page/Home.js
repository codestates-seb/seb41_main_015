import { useState, useEffect } from 'react';
import CarouselForm from '../components/Carousel';
import NicknameModal from '../components/NicknameModal';
import Ranking from '../components/Ranking';

const Home = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);

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

  return (
    <>
      {isModalOpen ? (
        <NicknameModal
          isModalOpen={isModalOpen}
          handleCloseModal={handleCloseModal}
        />
      ) : null}
      {/* 캐러셀 */}
      <CarouselForm />
      <Ranking />
    </>
  );
};

export default Home;

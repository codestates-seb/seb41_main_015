import { useState } from 'react';
import LoginModal from '../page/Login';

const Header = () => {
  // 임시로 해본 것!
  const [isModalOpen, setIsModalOpen] = useState(false);
  const handleOpenModal = () => {
    setIsModalOpen(true);
  };
  const handleCloseModal = () => {
    setIsModalOpen(false);
  };
  return (
    <>
      <button onClick={handleOpenModal}>로그인 버튼</button>
      <LoginModal
        isModalOpen={isModalOpen}
        handleCloseModal={handleCloseModal}
      />
    </>
  );
};

export default Header;

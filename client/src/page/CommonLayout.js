import Footer from '../components/Footer';
import { Outlet } from 'react-router-dom';

const CommonLayout = () => {
  return (
    <>
      <Outlet />
      <Footer />
    </>
  );
};

export default CommonLayout;

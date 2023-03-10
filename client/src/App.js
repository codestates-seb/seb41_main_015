import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './page/Home';
import CommonList from './page/CommonList';
import Header from './components/Header';
import ShareAdd from './page/ShareAdd';
import ShareEdit from './page/ShareEdit';
import ShareDetail from './page/ShareDetail';
import ReqAdd from './page/ReqAdd';
import ReqEdit from './page/ReqEdit';
import ReqDetail from './page/ReqDetail';
import MyPage from './page/MyPage';
import MyPageEdit from './page/MyPageEdit';
import Callback from './page/Callback';
import RateList from './page/RateList';
import RateAdd from './page/RateAdd';
import RateDetail from './page/RateDetail';
import Footer from './components/Footer';

const App = () => {
  return (
    <BrowserRouter>
      <Header />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route
          path="/shareList"
          element={
            <CommonList
              headTitle="현재 빌리지에 올라온 목록입니다!"
              endpoint="borrows"
              route="share"
            />
          }
        />
        <Route path="/shareAdd" element={<ShareAdd />} />
        <Route path="/shareEdit/:id" element={<ShareEdit />} />
        <Route path="/shareDetail/:id" element={<ShareDetail />} />
        <Route
          path="/reqList"
          element={
            <CommonList
              headTitle="빌리지 사람들이 찾고 있는 책이에요!"
              endpoint="requests"
              route="request"
            />
          }
        />
        <Route path="/reqAdd" element={<ReqAdd />} />
        <Route path="/reqEdit/:id" element={<ReqEdit />} />
        <Route path="/reqDetail/:id" element={<ReqDetail />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/mypageEdit" element={<MyPageEdit />} />
        <Route path="/rateList" element={<RateList />} />
        <Route path="/rateAdd" element={<RateAdd />} />
        <Route path="/rateDetail/:id" element={<RateDetail />} />
        <Route path="/oauth" element={<Callback />} />
      </Routes>
      <Footer />
    </BrowserRouter>
  );
};

export default App;

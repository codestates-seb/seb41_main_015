import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './page/Home';
import CommonLayout from './page/CommonLayout';
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

const App = () => {
  return (
    <BrowserRouter>
      <Header />
      <Routes>
        {/* 푸터가 들어가는 부분 */}
        <Route path="/" element={<CommonLayout />}>
          <Route index element={<Home />} />
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
        </Route>
        {/* 푸터 필요없는 부분 */}
        <Route path="/oauth" element={<Callback />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;
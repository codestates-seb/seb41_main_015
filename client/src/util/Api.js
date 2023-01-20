import axios from 'axios';

const REST_API_KEY = 'af7ced04a5a24ac94a313ca297b7ccaa';

const Kakao = axios.create({
  baseURL: 'https://dapi.kakao.com',
  headers: {
    Authorization: `KakaoAK ${REST_API_KEY}`,
  },
});

const BookSearch = (params) => {
  return Kakao.get('/v3/search/book', { params });
};

export default BookSearch;

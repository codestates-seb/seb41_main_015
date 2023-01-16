import DetailForm from '../components/DetailForm';
import Comment from '../components/Comment';

const ReqDetail = () => {
  const dummyData = {
    title: '나눔해주실 분 찾아요',
    bookTitle: '모던 자바스크립트 Deep Dive',
    bookAuthor: '이웅모',
    bookPublisher: '위키북스',
    imgUrl:
      'https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9791158392239.jpg',
    content: '안 보시는 분 나눔 부탁드립니다 ㅠㅠ!',
  };

  return (
    <>
      <DetailForm data={dummyData} page="request" />
      <Comment />
    </>
  );
};

export default ReqDetail;

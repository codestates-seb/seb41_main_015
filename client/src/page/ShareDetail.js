import DetailForm from '../components/DetailForm';

const ShareDetail = () => {
  // get 요청으로 데이터 받아오면 될 듯
  const dummyData = {
    title: '팝니다',
    bookTitle: 'ETS 토익 정기시험 기출문제집 1000 Vol 3 READING(리딩)',
    bookAuthor: 'ETS',
    bookPublisher: 'YBM',
    imgUrl:
      'https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788917238549.jpg',
    content: '상태 최상, 한 번도 편 적 없음',
  };

  return (
    <>
      <DetailForm data={dummyData} page="share" />
    </>
  );
};
export default ShareDetail;

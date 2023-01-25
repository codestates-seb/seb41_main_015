const elapsed = (date) => {
  // 현재 시간과 비교하여 경과된 시간을 리턴하는 함수
  const KR_TIME_DIFF = 18 * 60 * 60 * 1000;

  const uploaded = new Date(date);
  const utc = uploaded.getTime() + uploaded.getTimezoneOffset() * 60 * 1000;
  const kr_curr = new Date(utc + KR_TIME_DIFF);

  const current = new Date();
  const diff = (current - kr_curr) / 1000; // <- 초단위로 변환한 시간

  const unit = [
    { time: '일', seconds: 60 * 60 * 24 },
    { time: '시간', seconds: 60 * 60 },
    { time: '분', seconds: 60 },
    { time: '초', seconds: 1 },
  ];

  for (let value of unit) {
    const betweenTime = Math.floor(diff / value.seconds);

    if (betweenTime > 1) {
      if (value.time === '일') {
        const options = {
          year: 'numeric',
          month: 'short',
          day: 'numeric',
          hour12: false,
          hour: '2-digit',
          minute: '2-digit',
          timeZone: 'Asia/Seoul',
        };

        let converted = uploaded.toLocaleDateString('ko-KR', options).split('');
        let sliced = converted.slice(0, -6);

        return sliced.join('');
      }
      return `${betweenTime}${value.time} 전`;
    } else if (betweenTime === 1) {
      if (value.time === '일') return '어제';
      return `${betweenTime}${value.time} 전`;
    }
  }
  return '방금 전';
};

const prettyDate = (date) => {
  const KR_TIME_DIFF = 18 * 60 * 60 * 1000;

  const uploaded = new Date(date);
  const utc = uploaded.getTime() + uploaded.getTimezoneOffset() * 60 * 1000;
  const kr_curr = new Date(utc + KR_TIME_DIFF);

  return kr_curr.toLocaleString('ko-KR');
};

export { elapsed, prettyDate };

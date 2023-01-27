import styled from 'styled-components';
import StarRatings from 'react-star-ratings';

const SRatingContainer = styled.div`
  display: flex;
  justify-content: center;
  gap: 15px;

  #rate {
    font-size: 1.2rem;
  }
`;

const SRatingContainerSmall = styled.div`
  display: flex;
  justify-content: center;
  gap: 10px;

  #rate {
    font-size: 1rem;
    font-weight: 600;
  }
`;

const RateStar = ({ rating, setRating }) => {
  return (
    <SRatingContainer>
      <StarRatings
        rating={rating}
        starRatedColor="#f1e05a"
        starHoverColor="#f1e05a"
        starDimension="30px"
        starSpacing="3px"
        changeRating={setRating}
        name="ratingDefault"
      />
      <div id="rate">{rating}</div>
    </SRatingContainer>
  );
};

const RateStarSmall = ({ rating, setRating }) => {
  return (
    <SRatingContainerSmall>
      <StarRatings
        rating={rating}
        starRatedColor="#f1e05a"
        starHoverColor="#f1e05a"
        starDimension="20px"
        starSpacing="3px"
        changeRating={setRating}
        name="ratingSmall"
      />
      <div id="rate">{rating}</div>
    </SRatingContainerSmall>
  );
};

export { RateStar, RateStarSmall };

package com.book.village.server.domain.rate.mapper;

import com.book.village.server.domain.rate.dto.RateDto;
import com.book.village.server.domain.rate.entity.Rate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RateMapper {
    Rate ratePostDtoToRate(RateDto.Post ratePostDto);
    Rate ratePatchDtoToRate(RateDto.Patch ratePatchDto);
    default RateDto.Response rateToRateResponseDto(Rate rate){
        if (rate == null) {
            return null;
        } else {
            RateDto.Response response = new RateDto.Response();
            response.setRateId(rate.getRateId());
            response.setRating(rate.getRating());
            response.setDisplayName(rate.getDisplayName());
            response.setImgUrl(rate.getMember().getImgUrl());
            response.setTitle(rate.getTitle());
            response.setContent(rate.getContent());
            response.setCreatedAt(rate.getCreatedAt());
            response.setModifiedAt(rate.getModifiedAt());
            return response;
        }
    }
    List<RateDto.Response> ratesToRateResponseDtos(List<Rate> rates);
}

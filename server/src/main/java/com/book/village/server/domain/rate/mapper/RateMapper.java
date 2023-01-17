package com.book.village.server.domain.rate.mapper;

import com.book.village.server.domain.rate.dto.RateDto;
import com.book.village.server.domain.rate.entity.Rate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RateMapper {
    Rate ratePostDtoToRate(RateDto.Post ratePostDto);
    Rate ratePatchDtoToRate(RateDto.Patch ratePatchDto);
    RateDto.Response rateToRateResponseDto(Rate rate);
    List<RateDto.Response> RatesToRateResponseDtos(List<Rate> rates);
}

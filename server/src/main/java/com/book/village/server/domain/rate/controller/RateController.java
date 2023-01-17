package com.book.village.server.domain.rate.controller;

import com.book.village.server.domain.rate.dto.RateDto;
import com.book.village.server.domain.rate.entity.Rate;
import com.book.village.server.domain.rate.mapper.RateMapper;
import com.book.village.server.domain.rate.service.RateService;
import com.book.village.server.global.response.PageInfo;
import com.book.village.server.global.response.PageResponseDto;
import com.book.village.server.global.response.SingleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@Validated
@RequestMapping("/v1/rates")
public class RateController {
    private final RateService rateService;
    private final RateMapper mapper;

    public RateController(RateService rateService, RateMapper mapper) {
        this.rateService = rateService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postRate(@Valid @RequestBody RateDto.Post ratePostDto,
                                   @RequestParam String isbn,
                                   @RequestParam String bookTitle,
                                   @RequestParam String author,
                                   @RequestParam String publisher,
                                   Principal principal){
        Rate rate = rateService.createRate(mapper.ratePostDtoToRate(ratePostDto), principal.getName(),
                isbn,bookTitle, author, publisher);
        return new ResponseEntity(new SingleResponse<>(mapper.rateToRateResponseDto(rate)),
                HttpStatus.CREATED);
    }
    @PatchMapping("/{rate-id}")
    public ResponseEntity patchRate(@PathVariable("rate-id") long rateId,
                                    @Valid @RequestBody RateDto.Patch ratePatchDto,
                                    Principal principal){
        ratePatchDto.setRateId(rateId);
        Rate rate = rateService.updateRate(mapper.ratePatchDtoToRate(ratePatchDto), principal.getName());
        return ResponseEntity.ok(new SingleResponse<>(mapper.rateToRateResponseDto(rate)));
    }
    @GetMapping("/{rate-id}")
    public ResponseEntity getRate(@PathVariable("rate-id") long rateId){
        Rate rate = rateService.findRate(rateId);
        return ResponseEntity.ok(new SingleResponse<>(mapper.rateToRateResponseDto(rate)));
    }
    @GetMapping("/mine")
    public ResponseEntity getMyRates(@PageableDefault Pageable pageable, Principal principal){
        Page<Rate> rates = rateService.findMyRates(principal.getName(), pageable);
        return new ResponseEntity<>(
                new PageResponseDto<>(mapper.RatesToRateResponseDtos(rates.getContent()),
                        new PageInfo(rates.getPageable(), rates.getTotalElements())),
                        HttpStatus.OK);
    }
    @DeleteMapping("/{rate-id}")
    public ResponseEntity deleteRates(@PathVariable("rate-id") long rateId,Principal principal){
        rateService.deleteRate(rateId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}

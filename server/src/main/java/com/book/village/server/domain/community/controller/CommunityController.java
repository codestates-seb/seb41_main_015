package com.book.village.server.domain.community.controller;

import com.book.village.server.domain.community.dto.CommunityDto;
import com.book.village.server.domain.community.entity.Community;
import com.book.village.server.domain.community.mapper.CommunityMapper;
import com.book.village.server.domain.community.service.CommunityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@Validated
@RequestMapping("/v1/communities")
public class CommunityController {
    private final CommunityService communityService;
    private final CommunityMapper mapper;

    public CommunityController(CommunityService communityService, CommunityMapper mapper) {
        this.communityService = communityService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postCommunity(@Valid @RequestBody CommunityDto.Post communityPostDto,
                                        Principal principal){
        Community community =communityService.createCommunity(mapper.postCommunityDtoToCommunity(communityPostDto), principal.getName());
        return new ResponseEntity(mapper.communityToCommunityResponseDto(community),
                HttpStatus.CREATED);
    }

}

package com.book.village.server.domain.community.controller;

import com.book.village.server.domain.community.dto.CommunityDto;
import com.book.village.server.domain.community.entity.Community;
import com.book.village.server.domain.community.mapper.CommunityMapper;
import com.book.village.server.domain.community.service.CommunityService;
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
        return new ResponseEntity(new SingleResponse<>(mapper.communityToCommunityResponseDto(community)),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{community-id}")
    public ResponseEntity patchCommunity(@PathVariable("community-id") long communityId,
                                         @Valid @RequestBody CommunityDto.Patch communityPatchDto,
                                        Principal principal){
        communityPatchDto.setCommunityId(communityId);
        Community community =communityService.updateCommunity(mapper.patchCommunityDtoToCommunity(communityPatchDto), principal.getName());
        return ResponseEntity.ok(new SingleResponse<>(mapper.communityToCommunityResponseDto(community)));
    }
    @GetMapping("/{community-id}")
    public ResponseEntity getCommunity(@PathVariable("community-id") long communityId){
        Community community =communityService.findCommunity(communityId);
        community.setView(community.getView()+1L);
        communityService.updateCommunity(community, community.getMember().getEmail());
        return ResponseEntity.ok(new SingleResponse<>(mapper.communityToCommunityResponseDto(community)));
    }

    @GetMapping
    public ResponseEntity getCommunities(@PageableDefault Pageable pageable){
        Page<Community> communities = communityService.findCommunities(pageable);
        return new ResponseEntity<>(
                new PageResponseDto<>(mapper.communitiesToCommunityResponseDtos(communities.getContent()), new PageInfo(communities.getPageable(), communities.getTotalElements())),
                HttpStatus.OK);
    }

    @GetMapping("/mine")
    public ResponseEntity getMyCommunities(@PageableDefault Pageable pageable, Principal principal){
        Page<Community> communities = communityService.findMyCommunities(principal.getName(), pageable);
        return new ResponseEntity<>(
                new PageResponseDto<>(mapper.communitiesToCommunityResponseDtos(communities.getContent()), new PageInfo(communities.getPageable(), communities.getTotalElements())),
                HttpStatus.OK);
    }

    @DeleteMapping("/{community-id}")
    public ResponseEntity deleteCommunities(@PathVariable("community-id") long communityId, Principal principal){
        communityService.deleteCommunity(communityId,principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity searchCommunity(@RequestParam String keyword , @RequestParam String field, @RequestParam String type, @PageableDefault Pageable pageable){
        Page<Community> communities = communityService.searchCommunity(keyword, field, type, pageable);
        return new ResponseEntity<>(new PageResponseDto<>(mapper.communitiesToCommunityResponseDtos(communities.getContent()),
                new PageInfo(communities.getPageable(), communities.getTotalElements())), HttpStatus.OK);
    }

}

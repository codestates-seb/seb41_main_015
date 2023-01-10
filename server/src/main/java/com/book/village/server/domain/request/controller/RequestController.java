package com.book.village.server.domain.request.controller;
import com.book.village.server.domain.request.dto.RequestDto;
import com.book.village.server.domain.request.entity.Request;
import com.book.village.server.domain.request.mapper.RequestMapper;
import com.book.village.server.domain.request.service.RequestService;
import com.book.village.server.global.response.SingleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@Validated
@RequestMapping("/v1/requests")
public class RequestController {
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    public RequestController(RequestService requestService, RequestMapper requestMapper) {
        this.requestService = requestService;
        this.requestMapper = requestMapper;
    }

    @PostMapping
    public ResponseEntity postRequest(Principal principal,
                                      @Valid @RequestBody RequestDto.Post requestPostDto) {
        Request request = requestMapper.requestPostDtoToRequest(requestPostDto);
        requestService.createRequest(request, principal.getName());
        return new ResponseEntity(
                new SingleResponse<>(requestMapper.requestToRequestResponseDto(request)),
                HttpStatus.CREATED);
    }

}

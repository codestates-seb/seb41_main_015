package com.book.village.server.domain.request.service;

import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.domain.request.entity.Request;
import com.book.village.server.domain.request.repository.RequestRepository;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import com.book.village.server.global.utils.CustomBeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RequestService {

    private final RequestRepository requestRepository;
    private final MemberService memberService;

    private final CustomBeanUtils beanUtils;

    public RequestService(RequestRepository requestRepository, MemberService memberService, CustomBeanUtils beanUtils) {
        this.requestRepository = requestRepository;
        this.memberService = memberService;
        this.beanUtils = beanUtils;
    }


    public Request createRequest(Request request, String userEmail) {
        request.setMember(memberService.findMember(userEmail));
        return requestRepository.save(request);
    }

    public Request updateRequest(Request request, String userEmail) {
        Request findRequest = findVerifiedRequest(request.getRequestId());
        if (findRequest.getMember().equals(memberService.findMember(userEmail))) {
            beanUtils.copyNonNullProperties(request, findRequest);
            return requestRepository.save(findRequest);
        }
        throw new CustomLogicException(ExceptionCode.REQUEST_WRITER_NOT_MATCH);

    }

    private Request findVerifiedRequest(Long requestId) {
        Optional<Request> optionalRequest = requestRepository.findById(requestId);
        Request findRequest = optionalRequest.orElseThrow(() ->
                new CustomLogicException(ExceptionCode.REQUEST_NOT_FOUND));
        return findRequest;
    }
}

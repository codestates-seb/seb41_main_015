package com.book.village.server.domain.request.service;

import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.domain.request.dto.RequestDto;
import com.book.village.server.domain.request.entity.Request;
import com.book.village.server.domain.request.entity.RequestRank;
import com.book.village.server.domain.request.repository.RequestRepository;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import com.book.village.server.global.utils.CustomBeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
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
        request.setDisplayName(request.getMember().getDisplayName());
        return requestRepository.save(request);
    }

    public Request updateRequest(Request request, String userEmail) {
        Request findRequest = findVerifiedRequest(request.getRequestId());
        if (findRequest.getMember().getEmail().equals(userEmail)) {
            beanUtils.copyNonNullProperties(request, findRequest);
            return requestRepository.save(findRequest);
        }
        throw new CustomLogicException(ExceptionCode.REQUEST_WRITER_NOT_MATCH);

    }

    public Request findRequest(long requestId) {
        return findVerifiedRequest(requestId);
    }

    public Page<Request> findMyRequests(String userEmail,Pageable pageable) {
        return requestRepository.findAllByMember_Email(userEmail, pageable);
    }

    public Page<Request> findRequests(Pageable pageable) {
        return requestRepository.findAll(pageable);
    }

    private Request findVerifiedRequest(Long requestId) {
        Optional<Request> optionalRequest = requestRepository.findById(requestId);
        Request findRequest = optionalRequest.orElseThrow(() ->
                new CustomLogicException(ExceptionCode.REQUEST_NOT_FOUND));
        return findRequest;
    }

    public Page<Request> searchRequests(String keyword, String field, Pageable pageable) {
        switch (field) {
            case "displayName":
                return requestRepository.findAllByDisplayName(keyword, pageable);
            case "title":
                return requestRepository.findAllByTitleContaining(keyword, pageable);
            case "content":
                return requestRepository.findAllByContentContaining(keyword, pageable);
            case "bookTitle":
                return requestRepository.findAllByBookTitleContaining(keyword, pageable);
            case "author":
                return requestRepository.findAllByAuthor(keyword, pageable);
            case "publisher":
                return requestRepository.findAllByPublisher(keyword, pageable);
            default:
                return new PageImpl<>(Collections.emptyList());
        }
    }

    public void deleteRequest(long requestId, String userEmail) {
        Request findRequest = findVerifiedRequest(requestId);
        if (findRequest.getMember().getEmail().equals(userEmail)) {
            requestRepository.delete(findRequest);
            return;
        }
        throw new CustomLogicException(ExceptionCode.REQUEST_WRITER_NOT_MATCH);

    }

    public List<RequestRank> findRankedRequests() {
        return requestRepository.findRankedRequests();
    }
}

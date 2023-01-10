package com.book.village.server.domain.request.service;

import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.domain.request.entity.Request;
import com.book.village.server.domain.request.repository.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RequestService {

    private final RequestRepository requestRepository;
    private final MemberService memberService;

    public RequestService(RequestRepository requestRepository, MemberService memberService) {
        this.requestRepository = requestRepository;
        this.memberService = memberService;
    }


    public Request createRequest(Request request, String userEmail) {
        request.setMember(memberService.findMember(userEmail));
        return requestRepository.save(request);
    }
}

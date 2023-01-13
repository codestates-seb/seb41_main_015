package com.book.village.server.domain.borrowcomment.controller;

import com.book.village.server.domain.borrowcomment.mapper.BorrowCommentMapper;
import com.book.village.server.domain.borrowcomment.service.BorrowCommentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/v1/borrow")
public class BorrowCommentController {
    private final BorrowCommentService borrowCommentService;
    private final BorrowCommentMapper borrowCommentMapper;

    public BorrowCommentController(BorrowCommentService borrowCommentService,
                                   BorrowCommentMapper borrowCommentMapper) {
        this.borrowCommentService = borrowCommentService;
        this.borrowCommentMapper = borrowCommentMapper;
    }



}

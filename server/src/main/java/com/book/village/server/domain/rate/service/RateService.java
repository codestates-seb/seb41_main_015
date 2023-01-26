package com.book.village.server.domain.rate.service;

import com.book.village.server.domain.book.entity.Book;
import com.book.village.server.domain.book.repository.BookRepository;
import com.book.village.server.domain.book.service.BookService;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.domain.rate.entity.Rate;
import com.book.village.server.domain.rate.repository.RateRepository;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import com.book.village.server.global.utils.CustomBeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RateService {
    private final RateRepository rateRepository;
    private final MemberService memberService;
    private final BookService bookService;
    private final CustomBeanUtils<Rate> beanUtils;
    private final BookRepository bookRepository;

    public RateService(RateRepository rateRepository, MemberService memberService, BookService bookService, CustomBeanUtils<Rate> beanUtils,
                       BookRepository bookRepository) {
        this.rateRepository = rateRepository;
        this.memberService = memberService;
        this.bookService = bookService;
        this.beanUtils = beanUtils;
        this.bookRepository = bookRepository;
    }

    public Rate createRate(Rate rate, String email, String isbn, String bookTitle, String author, String publisher, String thumbnail) {
        if(rateRepository.findByMember_EmailAndBook_Isbn(email, isbn).isPresent())
            throw new CustomLogicException(ExceptionCode.RATE_DUPLICATE);
        if(!bookRepository.findByIsbn(isbn).isPresent()){
            Book book = new Book();
            book.setAuthor(author);
            book.setBookTitle(bookTitle);
            book.setIsbn(isbn);
            book.setPublisher(publisher);
            book.setThumbnail(thumbnail);
            bookRepository.save(book);
        }
        Book findBook=bookRepository.findByIsbn(isbn).get();
        findBook.setRateCount(findBook.getRateCount()+1);
        findBook.setTotalRate(findBook.getTotalRate()+rate.getRating());
        Double rating=findBook.getRateCount() ==0 ? 0.0 : Math.round((double)findBook.getTotalRate()/ findBook.getRateCount()*10.0)/10.0;
        findBook.setAvgRate(rating);
        rate.setDisplayName(memberService.findMember(email).getDisplayName());
        rate.setMember(memberService.findMember(email));
        rate.setBook(findBook);
        bookRepository.save(findBook);

        return rateRepository.save(rate);
    }
    public Rate updateRate(Rate rate, String email){
        Rate findRate = findVerifiedRate(rate.getRateId());
        verifyWriter(findRate,email);
        Long existRating= findRate.getRating();
        beanUtils.copyNonNullProperties(rate, findRate);
        Book findBook = findRate.getBook();
        findBook.setTotalRate(findBook.getTotalRate()-existRating+rate.getRating());
        Double rating=findBook.getRateCount() ==0 ? 0.0 : Math.round((double)findBook.getTotalRate()/ findBook.getRateCount()*10.0)/10.0;
        findBook.setAvgRate(rating);
        bookRepository.save(findBook);

        return rateRepository.save(findRate);
    }

    public Rate findRate(long rateId){
        return findVerifiedRate(rateId);
    }

    public Page<Rate> findMyRates(String email, Pageable pageable) {
        return rateRepository.findAllByMember_Email(email, pageable);
    }

    public void deleteRate(long rateId, String email){
        Rate findRate = findVerifiedRate(rateId);
        verifyWriter(findRate,email);
        Long existRating= findRate.getRating();
        Book findBook = findRate.getBook();
        findBook.setTotalRate(findBook.getTotalRate()-existRating);
        findBook.setRateCount(findBook.getRateCount()-1);
        Double rating=findBook.getRateCount() ==0 ? 0.0 : Math.round((double)findBook.getTotalRate()/ findBook.getRateCount()*10.0)/10.0;
        findBook.setAvgRate(rating);
        bookRepository.save(findBook);
        rateRepository.delete(findRate);
    }


    public Rate findVerifiedRate(long rateId){
        Optional<Rate> optionalRate = rateRepository.findById(rateId);
        Rate rate=
                optionalRate.orElseThrow(()->
                        new CustomLogicException(ExceptionCode.RATE_NOT_FOUND));
        return rate;
    }

    public void verifyWriter(Rate rate, String email){
        if(!rate.getMember().getEmail().equals(email))
            throw new CustomLogicException(ExceptionCode.RATE_USER_DIFFERENT);
    }
}

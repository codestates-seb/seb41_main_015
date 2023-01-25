package com.book.village.server.auth.filter;

import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.response.ErrorResponder;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);

        } catch (RuntimeException e) {
            if(e instanceof CustomLogicException){
                ErrorResponder errorResponder= new ErrorResponder();
                errorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED);
            }
            else throw e;
        }
    }
}

package com.book.village.server.global.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class RedirectType {
    @Value("${type}")
    private String serverType;
}

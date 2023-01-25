package com.book.village.server.auth.jwt.mapper;

import com.book.village.server.auth.jwt.dto.RefreshTokenDto;
import com.book.village.server.auth.jwt.entity.RefreshToken;
import org.mapstruct.Mapper;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    RefreshToken refreshTokenDtoToRefreshToken(RefreshTokenDto refreshTokenDto);
}

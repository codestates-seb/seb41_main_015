package com.book.village.server.auth.principal;

import com.book.village.server.auth.data.OAuth2Attribute;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class CustomDefaultOAuth2User extends DefaultOAuth2User {

    private OAuth2Attribute oAuth2Attribute;

    public CustomDefaultOAuth2User(Collection<? extends GrantedAuthority> authorities,
                                   Map<String, Object> attributes,
                                   String nameAttributeKey,
                                   OAuth2Attribute oAuth2Attribute) {
        super(authorities, attributes, nameAttributeKey);
        this.oAuth2Attribute = oAuth2Attribute;
    }
}
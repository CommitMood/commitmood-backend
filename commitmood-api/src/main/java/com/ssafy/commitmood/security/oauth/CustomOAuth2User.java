package com.ssafy.commitmood.security.oauth;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final UserAccount userAccount;
    private final Map<String, Object> attributes;
    private final String accessToken;

    public CustomOAuth2User(UserAccount userAccount, Map<String, Object> attributes, String accessToken) {
        this.userAccount = userAccount;
        this.attributes = attributes;
        this.accessToken = accessToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        return String.valueOf(userAccount.getId());
    }
}

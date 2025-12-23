package com.ssafy.commitmood.security.oauth;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserAccountMapper userAccountMapper;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        Long githubUserId = ((Number) attributes.get("id")).longValue();
        String githubLogin = (String) attributes.get("login");
        String githubEmail = (String) attributes.get("email");
        String githubAvatarUrl = (String) attributes.get("avatar_url");
        String githubName = (String) attributes.get("name");

        String accessToken = userRequest.getAccessToken().getTokenValue();

        UserAccount userAccount = userAccountMapper.findByGithubUserId(githubUserId)
                .orElseGet(() -> createNewUser(githubUserId, githubLogin, githubEmail, githubAvatarUrl, githubName));

        updateUserProfile(userAccount, githubEmail, githubAvatarUrl, githubName);

        return new CustomOAuth2User(userAccount, attributes, accessToken);
    }

    private UserAccount createNewUser(Long githubUserId, String githubLogin, String githubEmail,
                                       String githubAvatarUrl, String githubName) {
        UserAccount newUser = UserAccount.create(githubUserId, githubLogin, githubEmail, githubAvatarUrl, githubName);
        userAccountMapper.insert(newUser);
        return newUser;
    }

    private void updateUserProfile(UserAccount userAccount, String githubEmail,
                                    String githubAvatarUrl, String githubName) {
        if (!Objects.equals(userAccount.getGithubEmail(), githubEmail) ||
            !Objects.equals(userAccount.getGithubAvatarUrl(), githubAvatarUrl) ||
            !Objects.equals(userAccount.getGithubName(), githubName)) {
            userAccount.updateProfile(githubEmail, githubAvatarUrl, githubName);
            userAccountMapper.update(userAccount);
        }
    }
}

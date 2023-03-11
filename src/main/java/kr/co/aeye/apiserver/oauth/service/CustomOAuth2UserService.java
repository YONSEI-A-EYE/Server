package kr.co.aeye.apiserver.oauth.service;

import kr.co.aeye.apiserver.api.user.entity.RoleType;
import kr.co.aeye.apiserver.api.user.entity.User;
import kr.co.aeye.apiserver.oauth.attributes.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import kr.co.aeye.apiserver.api.user.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(userNameAttributeName, oauth2User.getAttributes());
        User user = getOrSave(attributes);
        // attribute -> new User;
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleType().getCode())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private User getOrSave(OAuthAttributes attributes){
        Optional<User> tempUser = userRepository.findByEmail(attributes.getEmail());
        User user;
        if (tempUser.isEmpty()){
            user = User.builder()
                    .email(attributes.getEmail())
                    .roleType(RoleType.USER)
                    .build();
            userRepository.save(user);
        }
        else{
            user = tempUser.get();
        }

        return user;
    }
}

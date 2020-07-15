package com.supshop.suppingmall.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(oAuth2UserRequest);
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
                                                        .getProviderDetails()
                                                        .getUserInfoEndpoint()
                                                        .getUserNameAttributeName();

        OAuthAttribute attribute = OAuthAttribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        SessionUser sessionUser = save(attribute);
        return sessionUser;
//        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(sessionUser.getRole().toString())), attribute.getAttributes(), attribute.getNameAttributeKey());
    }

    private SessionUser save(OAuthAttribute authAttribute) {
        User user = authAttribute.setUserInformation();
        user = userService.createUser(user);
        SessionUser sessionUser = modelMapper.map(user, SessionUser.class);
        sessionUser.setAttributes(authAttribute.getAttributes());
        sessionUser.setNameAttributeKey(authAttribute.getNameAttributeKey());
        return modelMapper.map(user, SessionUser.class);
    }

}

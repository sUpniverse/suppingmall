package com.supshop.suppingmall.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;
    private final HttpSession httpSession;
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
        UserVO userVO = save(attribute);
        httpSession.setAttribute("user", userVO);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(userVO.getRole().toString())), attribute.getAttributes(), attribute.getNameAttributeKey());
    }

    private UserVO save(OAuthAttribute authAttribute) {
        User user;
        try {
            user = authAttribute.setUserVo();
            userService.createUser(user);
        } catch (RuntimeException e) {
            return null;
        }

        return modelMapper.map(user, UserVO.class);
    }

}

package com.supshop.suppingmall.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter @Builder
@ToString
public class OAuthAttribute {

    private Map<String,Object> attributes;  // Provider로 부터 얻어오는 User의 정보
    private String nameAttributeKey;
    private String email;
    private String name;
    private User.LoginType loginType;

    public static OAuthAttribute of(String registrationId, String userNameAttributeName, Map<String,Object> attributes) {
        if(User.LoginType.NAVER.getKey().equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttribute ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttribute.builder()
                                .email((String) attributes.get("email"))
                                .name((String) attributes.get("name"))
                                .loginType(User.LoginType.GOOGLE)
                                .attributes(attributes)
                                .nameAttributeKey(userNameAttributeName)
                                .build();
    }

    private static OAuthAttribute ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuthAttribute.builder()
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .loginType(User.LoginType.NAVER)
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User setUserVo() {
        return User.builder()
                .email(this.email)
                .name(this.name)
                .nickName(this.name)
                .role(Role.USER)
                .type(this.loginType)
                .build();
    }

}

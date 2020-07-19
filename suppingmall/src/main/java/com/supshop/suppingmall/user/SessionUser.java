package com.supshop.suppingmall.user;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter @Setter
@ToString @Builder
@EqualsAndHashCode(of = "userId")
@NoArgsConstructor @AllArgsConstructor
public class SessionUser implements UserDetails, OAuth2User, Serializable {

    private static final long serialVersionUID = 6831318661116074785L;
    private Long userId;
    private String password;
    private String email;
    private String name;
    private String nickName;
    private String address;
    private String addressDetail;
    private String zipCode;
    private String phoneNumber;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private String delYn;
    private String emailConfirmYn;
    private Role role;
    private User.LoginType type;
    private UserConfirmation userConfirmation;
    private StoreVO storeVO;
    private Map<String, Object> attributes;
    private String nameAttributeKey;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String rolePrefix = "ROLE_";
        List<Role> roles = Arrays.asList(this.role);
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(rolePrefix+ role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.nickName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

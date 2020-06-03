package com.supshop.suppingmall.config;

import com.supshop.suppingmall.common.LoginSuccessHandler;
import com.supshop.suppingmall.user.CustomOAuth2UserService;
import com.supshop.suppingmall.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.CustomUserTypesOAuth2UserService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final LoginSuccessHandler loginSuccessHandler;

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/resources/**");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .anonymous()
                .and()
            .csrf().disable()
            .formLogin()
                .successHandler(loginSuccessHandler)
                .loginPage("/users/loginform")
                .loginProcessingUrl("/users/login")
                .failureUrl("/users/loginform?error")
                    .permitAll()
                .and()
            .logout()
                .logoutUrl("/users/logout")
                .logoutSuccessUrl("/")
            .and()
                .oauth2Login()
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService);
        setAntMatchers(http);
    }

    private void setAntMatchers(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/users").hasAnyRole("ROLE_MASTER","ROLE_ADMIN")
                .antMatchers(HttpMethod.GET,"/users/signup").permitAll()
                .antMatchers(HttpMethod.POST,"/users").anonymous()
                .antMatchers(HttpMethod.GET,"/users/{id}/**").authenticated()
                .antMatchers(HttpMethod.PUT,"/users/{id}/**").authenticated()
                .antMatchers(HttpMethod.DELETE,"/users/{id}/**").hasAnyRole("ROLE_MASTER","ROLE_ADMIN")
                .antMatchers(HttpMethod.PATCH,"/users/{id}/**").hasAnyRole("ROLE_MASTER","ROLE_ADMIN")
                .antMatchers("/users/seller/**").authenticated()
                .antMatchers(HttpMethod.GET,"/boards/form").authenticated()
                .antMatchers(HttpMethod.GET,"/boards").anonymous()
                .antMatchers(HttpMethod.GET,"/boards/{id}").anonymous()
                .antMatchers(HttpMethod.POST,"/boards").authenticated()
                .antMatchers(HttpMethod.GET,"/boards/{id}").anonymous()
                .antMatchers(HttpMethod.POST,"/boards/form").authenticated()
                .antMatchers(HttpMethod.PUT,"/boards/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE,"/boards/{id}").authenticated()

        ;
    }
}

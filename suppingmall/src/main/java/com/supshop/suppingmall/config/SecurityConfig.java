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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final LoginSuccessHandler   loginSuccessHandler;


    private static final String resourcesPath = "/resources/**";
    private static final String loginForm = "/users/loginform";
    private static final String login = "/users/login";
    private static final String loginFormError = "/users/loginform?error";
    private static final String logout = "/users/logout";
    private static final String main = "/";

    private static final String MASTER = "MASTER";
    private static final String ADMIN = "ADMIN";
    private static final String SELLER = "SELLER";
    private static final String USER = "USER";


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
        web.ignoring().mvcMatchers(resourcesPath);
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .anonymous()
        .and()
            .cors().disable()
            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .and()
            .formLogin()
                .successHandler(loginSuccessHandler)
                .loginPage(loginForm)
                .loginProcessingUrl(login)
                .failureUrl(loginFormError)
                    .permitAll()
        .and()
            .logout()
                .logoutUrl(logout)
                .logoutSuccessUrl(main)
                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
        .and()
            .exceptionHandling()
                .accessDeniedPage(loginForm)
        .and()
            .oauth2Login()
                .successHandler(loginSuccessHandler)
                .userInfoEndpoint()
                    .userService(customOAuth2UserService)
        ;
        setAntMatchers(http);
    }

    private void setAntMatchers(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/users").hasAnyRole(MASTER,ADMIN)
                .antMatchers(HttpMethod.GET,"/users/signup").anonymous()
                .antMatchers(HttpMethod.GET,"/users/findAccountForm").anonymous()
                .antMatchers(HttpMethod.POST,"/users/findAccount").anonymous()
                .antMatchers(HttpMethod.POST,"/users").permitAll()
                .antMatchers(HttpMethod.GET,"/users/{id}/**").authenticated()
                .antMatchers(HttpMethod.PUT,"/users/{id}/**").authenticated()
                .antMatchers(HttpMethod.DELETE,"/users/{id}/**").hasAnyRole(MASTER,ADMIN)
                .antMatchers(HttpMethod.PATCH,"/users/{id}/**").hasAnyRole(MASTER,ADMIN)
                .antMatchers(HttpMethod.GET,"/users/seller").hasAnyRole(MASTER,ADMIN)
                .antMatchers(HttpMethod.GET,"/users/seller/{id}").hasAnyRole(MASTER,ADMIN,SELLER)
                .antMatchers(HttpMethod.GET,"/users/seller/applyForm").hasAnyRole(USER)
                .antMatchers(HttpMethod.POST,"/users/seller/{id}/apply").hasAnyRole(MASTER,ADMIN,USER)
                .antMatchers(HttpMethod.GET,"/users/seller/applicant").hasAnyRole(MASTER,ADMIN)
                .antMatchers(HttpMethod.PATCH,"/users/seller/{id}/apply").hasAnyRole(MASTER,ADMIN)
                .antMatchers(HttpMethod.GET,"/users/confirm").authenticated()
                .antMatchers(HttpMethod.GET,"/boards/form").authenticated()
                .antMatchers(HttpMethod.GET,"/boards").permitAll()
                .antMatchers(HttpMethod.GET,"/boards/{id}").permitAll()
                .antMatchers(HttpMethod.POST,"/boards").authenticated()
                .antMatchers(HttpMethod.GET,"/boards/{id}").permitAll()
                .antMatchers(HttpMethod.POST,"/boards/form").authenticated()
                .antMatchers(HttpMethod.PUT,"/boards/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE,"/boards/{id}").authenticated()
                .antMatchers("/comments/**").authenticated()
                .antMatchers(HttpMethod.GET,"/images/**").permitAll()
                .antMatchers(HttpMethod.POST,"/images/**").authenticated()
                .antMatchers(HttpMethod.GET,"/products").hasAnyRole(MASTER,ADMIN,SELLER)
                .antMatchers(HttpMethod.GET,"/products/form").hasAnyRole(MASTER,ADMIN,SELLER)
                .antMatchers(HttpMethod.POST,"/products").hasAnyRole(MASTER,ADMIN,SELLER)
                .antMatchers(HttpMethod.PUT,"/products/{id}").hasAnyRole(MASTER,ADMIN,SELLER)
                .antMatchers(HttpMethod.DELETE,"/products/{id}").hasAnyRole(MASTER,ADMIN,SELLER)
                .antMatchers(HttpMethod.GET,"/products/seller").hasAnyRole(MASTER,ADMIN,SELLER)
                .antMatchers(HttpMethod.GET,"/products/main").permitAll()
                .antMatchers(HttpMethod.GET,"/products/{id}").permitAll()
                .antMatchers(HttpMethod.PATCH,"/products/{id/status/{status}").permitAll()
                .antMatchers(HttpMethod.GET,"/products/{productId}/qnas/form").authenticated()
                .antMatchers(HttpMethod.POST,"/products/{productId}/qnas").authenticated()
                .antMatchers(HttpMethod.GET,"/products/qnas/{qnaId}").permitAll()
                .antMatchers(HttpMethod.GET,"/products/qnas/{qnaId}/updateForm").authenticated()
                .antMatchers(HttpMethod.PUT,"/products/qnas/{qnaId}").authenticated()
                .antMatchers(HttpMethod.DELETE,"/products/qnas/{qnaId}").authenticated()
                .antMatchers("/orders/**").authenticated()
                .antMatchers("/carts/**").authenticated()
                .antMatchers("/payments/**").authenticated()
                .antMatchers("/delivery/**").authenticated()
                .antMatchers(HttpMethod.GET,"/category/{id}").hasAnyRole(MASTER,ADMIN,SELLER)
                .antMatchers(HttpMethod.POST,"/category/**").hasAnyRole(MASTER,ADMIN)
                .antMatchers(HttpMethod.PUT,"/category/**").hasAnyRole(MASTER,ADMIN)
                .antMatchers(HttpMethod.DELETE,"/category/**").hasAnyRole(MASTER,ADMIN)


        ;
    }
}

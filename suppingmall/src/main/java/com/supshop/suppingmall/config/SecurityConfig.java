package com.supshop.suppingmall.config;

import com.supshop.suppingmall.common.CustomDeniedHandler;
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
    private final LoginSuccessHandler loginSuccessHandler;
    private final CustomDeniedHandler customDeniedHandler;


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
                .accessDeniedHandler(customDeniedHandler)
        .and()
            .oauth2Login()
                .successHandler(loginSuccessHandler)
                .userInfoEndpoint()
                    .userService(customOAuth2UserService)
        ;
        setMvcMatchers(http);
    }

    private void setMvcMatchers(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .mvcMatchers(HttpMethod.POST,"/users").permitAll()
                .mvcMatchers(HttpMethod.GET,"/users").hasAnyRole(MASTER,ADMIN)
                .mvcMatchers(HttpMethod.GET,"/users/signup/**").anonymous()
                .mvcMatchers(HttpMethod.GET,"/users/account").anonymous()
                .mvcMatchers(HttpMethod.POST,"/users/account").anonymous()
                .mvcMatchers(HttpMethod.GET,"/users/confirm").authenticated()
                .mvcMatchers(HttpMethod.POST,"/users/confirm/resend").authenticated()
                .mvcMatchers(HttpMethod.GET,"/users/{id}").authenticated()
                .mvcMatchers(HttpMethod.PUT,"/users/{id}").authenticated()
                .mvcMatchers(HttpMethod.DELETE,"/users/{id}").authenticated()
                .mvcMatchers(HttpMethod.PATCH,"/users/{id}").hasAnyRole(MASTER,ADMIN)
                .mvcMatchers(HttpMethod.GET,"/users/{id}/form").authenticated()
                .mvcMatchers(HttpMethod.GET,"/users/{id}/password").authenticated()
                .mvcMatchers(HttpMethod.PUT,"/users/{id}/password").authenticated()
                .mvcMatchers(HttpMethod.GET,"/users/{id}/signout").authenticated()
                .mvcMatchers(HttpMethod.POST,"/users/{id}/signout").authenticated()
                .mvcMatchers(HttpMethod.GET,"/users/seller/main").hasAnyRole(SELLER)
                .mvcMatchers(HttpMethod.GET,"/users/seller/apply").hasAnyRole(USER)
                .mvcMatchers(HttpMethod.POST,"/users/seller/apply").hasAnyRole(USER)
                .mvcMatchers(HttpMethod.GET,"/users/seller/applicant").hasAnyRole(MASTER,ADMIN)
                .mvcMatchers(HttpMethod.PATCH,"/users/seller/{id}/apply").hasAnyRole(MASTER,ADMIN)
                .mvcMatchers(HttpMethod.GET,"/users/seller/transfer").hasAnyRole(SELLER)
                .mvcMatchers(HttpMethod.POST,"/users/seller/transfer").hasAnyRole(SELLER)
//                .mvcMatchers(HttpMethod.GET,"/users/seller/{id}").hasAnyRole(MASTER,ADMIN,SELLER)
                .mvcMatchers(HttpMethod.GET,"/users/seller/{id}/updateform").hasAnyRole(SELLER)
                .mvcMatchers(HttpMethod.PATCH,"/users/seller/{id}").hasAnyRole(MASTER,ADMIN)
                .mvcMatchers(HttpMethod.GET,"/users/seller").hasAnyRole(MASTER,ADMIN,SELLER)
                .mvcMatchers(HttpMethod.GET,"/boards").permitAll()
                .mvcMatchers(HttpMethod.POST,"/boards").authenticated()
                .mvcMatchers(HttpMethod.GET,"/boards/main").authenticated()
                .mvcMatchers(HttpMethod.GET,"/boards/form").authenticated()
                .mvcMatchers(HttpMethod.POST,"/boards/form").authenticated()
                .mvcMatchers(HttpMethod.GET,"/boards/{id}").permitAll()
                .mvcMatchers(HttpMethod.GET,"/boards/{id}").permitAll()
                .mvcMatchers(HttpMethod.PUT,"/boards/{id}").authenticated()
                .mvcMatchers(HttpMethod.DELETE,"/boards/{id}").authenticated()
                .mvcMatchers(HttpMethod.POST,"/boards/{id}/blind").hasAnyRole(MASTER,ADMIN)
                .mvcMatchers(HttpMethod.GET,"/boards/{id}/form").authenticated()
                .mvcMatchers(HttpMethod.GET,"/comments/**").permitAll()
                .mvcMatchers("/comments/**").authenticated()
                .mvcMatchers(HttpMethod.GET,"/images/**").permitAll()
                .mvcMatchers(HttpMethod.POST,"/images/**").authenticated()
                .mvcMatchers(HttpMethod.GET,"/products").permitAll()
                .mvcMatchers(HttpMethod.GET,"/products/form").hasAnyRole(MASTER,ADMIN,SELLER)
                .mvcMatchers(HttpMethod.PUT,"/products/{id}").hasAnyRole(MASTER,ADMIN,SELLER)
                .mvcMatchers(HttpMethod.DELETE,"/products/{id}").hasAnyRole(MASTER,ADMIN,SELLER)
                .mvcMatchers(HttpMethod.GET,"/products/admin").hasAnyRole(MASTER,ADMIN)
                .mvcMatchers(HttpMethod.GET,"/products/seller").hasAnyRole(SELLER)
                .mvcMatchers(HttpMethod.GET,"/products/main").permitAll()
                .mvcMatchers(HttpMethod.GET,"/products/{id}").permitAll()
                .mvcMatchers(HttpMethod.PATCH,"/products/{id/status/{status}").permitAll()
                .mvcMatchers(HttpMethod.GET,"/products/qna/main").authenticated()
                .mvcMatchers(HttpMethod.GET,"/products/qna/seller/main").hasAnyRole(SELLER)
                .mvcMatchers(HttpMethod.GET,"/products/qna/admin").hasAnyRole(MASTER,ADMIN)
                .mvcMatchers(HttpMethod.GET,"/products/{productId}/qna/form").authenticated()
                .mvcMatchers(HttpMethod.GET,"/products/qna/{qnaId}").permitAll()
                .mvcMatchers(HttpMethod.POST,"/products/qna/{qnaId}/reply").permitAll()
                .mvcMatchers(HttpMethod.GET,"/products/{productId}/qna/{page}").permitAll()
                .mvcMatchers(HttpMethod.POST,"/products/{productId}/qna").authenticated()
                .mvcMatchers(HttpMethod.GET,"/products/category/{categoryName}").permitAll()
                .mvcMatchers(HttpMethod.POST,"/products/**").hasAnyRole(MASTER,ADMIN,SELLER)
                .mvcMatchers(HttpMethod.GET,"/products/**").hasAnyRole(MASTER,ADMIN,SELLER)
                .mvcMatchers("/orders/**").authenticated()
                .mvcMatchers("/cart/**").authenticated()
                .mvcMatchers("/payments/**").authenticated()
                .mvcMatchers("/delivery/**").authenticated()
                .mvcMatchers(HttpMethod.GET,"/category/{id}").hasAnyRole(MASTER,ADMIN,SELLER)
                .mvcMatchers(HttpMethod.POST,"/category/**").hasAnyRole(MASTER,ADMIN)
                .mvcMatchers(HttpMethod.PUT,"/category/**").hasAnyRole(MASTER,ADMIN)
                .mvcMatchers(HttpMethod.DELETE,"/category/**").hasAnyRole(MASTER,ADMIN)
                .mvcMatchers(HttpMethod.GET,"/reviews/**").permitAll()
                .mvcMatchers(HttpMethod.POST,"/reviews/**").authenticated()


        ;
    }
}

package com.supshop.suppingmall.user;

import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.error.exception.order.InvalidConfirmationTokenException;
import com.supshop.suppingmall.page.PageMaker;
import com.supshop.suppingmall.page.ThirtyItemsCriteria;
import com.supshop.suppingmall.user.Form.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequestMapping("/users")
@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    private static final String redirectLoginUrl = "redirect:/users/loginform";
    private static final String redirectMainUrl = "redirect:/";
    private static final String requestReferer = "Referer";
    private static final String prevPage = "prevPage";

    private static final int perPageNumber = 15;
    private static final int pagingCount = 15;


    @GetMapping("/signup")
    public String signupform() {
        return "/user/signup";
    }

    /**
     * 로그인 페이지
     * 회원자격의 요청이 필요하여 로그인창에 도달 시,
     * 이전 referer을 이용해 왔던 페이지를 session에 넣어줌
     * @param user
     * @param request
     * @return
     */
    @GetMapping("/loginform")
    public String loginform(@AuthenticationPrincipal SessionUser user, HttpServletRequest request) {
        String uri = request.getHeader(requestReferer);
        if (uri == null || !uri.contains("/loginform")) {
            request.getSession().setAttribute(prevPage, request.getHeader(requestReferer));
        }

        if(user != null) {
            return "redirect:/";
        }

        return "/user/login";
    }

    @GetMapping("")
    public String getAllUser(Model model,
                             ThirtyItemsCriteria criteria,
                             @RequestParam(required = false) String type,
                             @RequestParam(required = false) String searchValue) {

        model.addAttribute(userService.getAllUser(criteria,type,searchValue));
        PageMaker pageMaker = new PageMaker(userService.getUserCount(type,searchValue),pagingCount, criteria);
        model.addAttribute("pageMaker", pageMaker);
        return "/user/admin/list";
    }


    @GetMapping("/{id}")
    public String getUserPage(@PathVariable Long id,
                              Model model,
                              @AuthenticationPrincipal SessionUser sessionUser) {

        if(!UserUtils.isOwner(id,sessionUser)) {
            throw new IllegalArgumentException("유효하지 않은 회원입니다.");
        }

        User user = userService.getUser(id);
        model.addAttribute("user",user);

        return "/user/main";
    }


//    @PostMapping("/login")
//    public String login(String email, String password, HttpSession session) {
//        UserVO user = userService.isSignedInUser(email,password);
//        if(user == null) {
//            return redirectLoginUrl;
//        }
//        session.setAttribute("user", user);
//        return redirectMainUrl;
//    }

//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.removeAttribute("user");
//        return redirectMainUrl;
//    }

    @PostMapping("")
    public String createUser(@Valid SignUpForm signUpForm) {

        User createdUser = modelMapper.map(signUpForm, User.class);
        userService.createUser(createdUser);

        return redirectLoginUrl;
    }

    @GetMapping("/{id}/form")
    public String getUpdateForm(@PathVariable Long id,
                                Model model,
                                @AuthenticationPrincipal SessionUser sessionUser) {


        if(!UserUtils.isOwner(id, sessionUser) && !UserUtils.isAdmin(sessionUser)) {
            throw new IllegalArgumentException();
        }

        User user = userService.getUser(id);
        model.addAttribute("user",user);

        if(UserUtils.isAdmin(sessionUser)) {        // 운영자 자격으로 해당 회원의 정보를 수정할 시 사용
            return "/user/admin/adminUpdateForm";
        }

        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity updateUser(@PathVariable Long id,
                                     @RequestBody @Valid UpdateUserForm form,
                                     @AuthenticationPrincipal SessionUser sessionUser) {

        if(!(UserUtils.isOwner(id, sessionUser))) {
            throw new IllegalArgumentException("유효하지 않는 유저입니다.");
        }

        User originUser = userService.getUser(id);

        // 제대로 비밀번호를 입력했는지 확인
        if(!userService.matchedPassword(originUser.getEmail(), form.getPassword())){
            throw new IllegalArgumentException("유효하지 않은 패스워드");
        }

        modelMapper.map(form, originUser);
        userService.updateUser(id, originUser);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> partialUpdateUser(@RequestBody(required = false) @Valid SessionUser sessionUser,
                                                    @PathVariable Long id) {

//        if(isAdmin(sessionUser)) {
//            User user = modelMapper.map(userVO, User.class);
//            userService.patchUser(id,user);
//            return ResponseEntity.ok().build();
//        }
        User user = modelMapper.map(sessionUser, User.class);
        userService.patchUser(id,user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/password")
    public String getPasswordForm(@PathVariable Long id,
                                  Model model,
                                  @AuthenticationPrincipal SessionUser sessionUser) {

        if(!UserUtils.isOwner(id,sessionUser)) {
            throw new IllegalArgumentException("유효하지 않는 유저입니다.");
        }

        User user = userService.getUser(id);
        model.addAttribute("user",user);
        return "/user/passwordForm";
    }

    @PutMapping("/{id}/password")
    @ResponseBody
    public ResponseEntity updatePassword(@PathVariable Long id,
                                         @RequestBody @Valid UpdatePasswordForm form,
                                         @AuthenticationPrincipal SessionUser sessionUser) {

        if(!(UserUtils.isOwner(id, sessionUser))) {
            throw new IllegalArgumentException("유효하지 않는 유저입니다.");
        }

        User user = userService.getUser(id);

        // 제대로 원래 비밀번호를 입력했는지 확인 && 새로 입력한 비밀번호와 확인 비밀번호가 맞는지 확인
        if(!userService.matchedPassword(user.getEmail(), form.getPassword()) || !form.getNewPassword().equals(form.getNewPasswordCheck())){
            throw new IllegalArgumentException("유효하지 않은 비밀번호입니다.");
        }

        user.setPassword(form.getNewPassword());
        userService.updateUser(id, user);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}/signout")
    public String getSignOutForm(@PathVariable Long id, Model model, @AuthenticationPrincipal SessionUser sessionUser) {

        if(!UserUtils.isOwner(id,sessionUser)) {
            throw new IllegalArgumentException("유효하지 않은 유저입니다.");
        }

        User user = userService.getUser(id);
        model.addAttribute("user",user);
        return "/user/signout";
    }


    @GetMapping("/confirm")
    public String confirmUser(@RequestParam String token,
                              @AuthenticationPrincipal SessionUser sessionUser) {

        User user = userService.getUserWithConfirmationByEmail(sessionUser.getEmail()).get();

        if(user.getEmailConfirmYn().equals("Y")) {
            throw new IllegalArgumentException("이미 인증 된 유저입니다");
        }

        if(!user.getUserConfirmation().getConfirmToken().equals(token)) {
            throw new InvalidConfirmationTokenException("유효하지 않는 토큰입니다.");
        }
        user.setEmailConfirmYn("Y");
        userService.patchUser(user.getUserId(), user);

        return "/user/confirm/success";
    }

    @PostMapping("/confirm/resend")
    @ResponseBody
    public ResponseEntity resendConfirmEmail(@AuthenticationPrincipal SessionUser sessionUser) {
        User user = userService.getUserWithConfirmationByEmail(sessionUser.getEmail()).get();

        if(user.getEmailConfirmYn().equals("Y")) {
            throw new IllegalArgumentException("이미 인증 된 유저입니다");
        }

        userService.resendConfirmationEmail(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/account")
    public String getFindAccountForm() {

        return "/user/findAccountForm";
    }

    @PostMapping("/account")
    @ResponseBody
    public ResponseEntity findAccount(@RequestBody FindAccountForm findAccountForm) {
        User user;

        try {
            user = userService.getUserByEmail(findAccountForm.getEmail());
            if(!user.getName().equals(findAccountForm.getUserName()))
                throw new UsernameNotFoundException("유저의 이름이 존재하지 않습니다.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.noContent().build();
        }

        try {
            userService.sendChangePasswordEmail(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@PathVariable Long id,
                                             @RequestBody PasswordCheckForm form,
                                             HttpSession httpSession,
                                             @AuthenticationPrincipal SessionUser sessionUser) {

        if(!(UserUtils.isOwner(id, sessionUser))) {
            throw new IllegalArgumentException("유효하지 않은 유저입니다.");
        }

        User user = userService.getUser(id);

        if(!userService.matchedPassword(user.getEmail(), form.getPassword())){
            return ResponseEntity.badRequest().body("유효하지 않은 비밀번호 입니다.");
        }


        user.setDelYn("Y");
        userService.patchUser(id,user);
        //Todo : 세션 정보 제거
        httpSession.invalidate();
        return ResponseEntity.ok().build();
    }

}

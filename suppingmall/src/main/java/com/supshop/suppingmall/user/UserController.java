package com.supshop.suppingmall.user;

import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.page.BoardCriteria;
import com.supshop.suppingmall.page.PageMaker;
import com.supshop.suppingmall.user.Form.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
    public String signupform(@AuthenticationPrincipal SessionUser user) {
        return "/user/signup";
    }

    @GetMapping("/loginform")
    public String loginform(@AuthenticationPrincipal SessionUser user, HttpServletRequest request) {
        String uri = request.getHeader(requestReferer);
        if (uri == null || !uri.contains("/loginform")) {
            request.getSession().setAttribute(prevPage, request.getHeader(requestReferer));
        }

        return "/user/login";
    }

    @GetMapping("")
    public String getAllUser(Model model,
                             BoardCriteria boardCriteria,
                             @RequestParam(required = false) String type,
                             @RequestParam(required = false) String searchValue) {

        model.addAttribute(userService.getAllUser(boardCriteria,type,searchValue));
        PageMaker pageMaker = new PageMaker(userService.getUserCount(type,searchValue),pagingCount,boardCriteria);
        model.addAttribute("pageMaker", pageMaker);
        return "/user/list";
    }


    @GetMapping("/{id}")
    public String getUserPage(@PathVariable Long id, Model model,@AuthenticationPrincipal SessionUser sessionUser) {

        if(UserUtils.isOwner(id,sessionUser)) {

            User user = userService.getUser(id);
            model.addAttribute("user",user);

            return "/user/main";
        }

        return "redirect:/";
    }

    @GetMapping("/{id}/updateForm")
    public String getUpdateForm(@PathVariable Long id, Model model, @AuthenticationPrincipal SessionUser sessionUser) {

        // 운영자 자격으로 해당 회원의 정보를 수정할 시 사용
        if(UserUtils.isAdmin(sessionUser)) {
            User user = userService.getUser(id);
            model.addAttribute("user",user);
            return "/user/adminUpdateForm";
        } else if(UserUtils.isOwner(id,sessionUser)) { // 개인화원 자격으로 자신의 회원 정보를 수정할 시 사용
            User user = userService.getUser(id);
            model.addAttribute("user",user);
            return "/user/updateForm";
        }

        return "redirect:/";
    }

    @GetMapping("/{id}/passwordForm")
    public String getPasswordForm(@PathVariable Long id, Model model, @AuthenticationPrincipal SessionUser sessionUser) {

        if(UserUtils.isOwner(id,sessionUser)) {
            User user = userService.getUser(id);
            model.addAttribute("user",user);
            return "/user/passwordForm";
        }

        return "redirect:/";
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
    public String createUser(@Valid SignUpForm signUpForm, @AuthenticationPrincipal SessionUser user) {
        User createdUser = modelMapper.map(signUpForm, User.class);
        userService.createUser(createdUser);

        return redirectLoginUrl;
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserForm form, @AuthenticationPrincipal SessionUser sessionUser) {

        if(!(UserUtils.isOwner(id, sessionUser))) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }

        User originUser = userService.getUser(id);

        // 제대로 비밀번호를 입력했는지 확인
        if(!userService.matchedPassword(originUser.getEmail(), form.getPassword())){
            return ResponseEntity.badRequest().body("유효하지 않은 패스워드 입니다.");
        }

        User user = modelMapper.map(form, User.class);
        try {
            userService.updateUser(id, user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("회원변경 실패");
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/password")
    @ResponseBody
    public ResponseEntity updatePassword(@PathVariable Long id, @RequestBody @Valid UpdatePasswordForm form, @AuthenticationPrincipal SessionUser sessionUser) {

        if(!(UserUtils.isOwner(id, sessionUser))) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }

        User user = userService.getUser(id);

        //Todo : 직접 validator 구현
        // 제대로 원래 비밀번호를 입력했는지 확인 && 새로 입력한 비밀번호와 확인 비밀번호가 맞는지 확인
        if(!userService.matchedPassword(user.getEmail(), form.getPassword()) || !form.getNewPassword().equals(form.getNewPasswordCheck())){
            return ResponseEntity.badRequest().body("유효하지 않은 비밀번호 입니다.");
        }

        try {
            user.setPassword(form.getNewPassword());
            userService.updateUser(id, user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("회원변경 실패");
        }

        return ResponseEntity.ok().build();
    }


    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> partialUpdateUser(@RequestBody(required = false) @Valid SessionUser sessionUser, @PathVariable Long id) {

//        if(isAdmin(sessionUser)) {
//            User user = modelMapper.map(userVO, User.class);
//            userService.patchUser(id,user);
//            return ResponseEntity.ok().build();
//        }
        try {
            User user = modelMapper.map(sessionUser, User.class);
            userService.patchUser(id,user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("업데이트 실패");
        }
    }

    @GetMapping("/{id}/signout")
    public String getSignOutForm(@PathVariable Long id, Model model, @AuthenticationPrincipal SessionUser sessionUser) {

        if(UserUtils.isOwner(id,sessionUser)) {
            User user = userService.getUser(id);
            model.addAttribute("user",user);
            return "/user/signout";
        }

        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@PathVariable Long id,
                                             @RequestBody SignOutForm form,
                                             @AuthenticationPrincipal SessionUser sessionUser) {

        log.debug("deleteUser 호출 됌");

        if(!(UserUtils.isOwner(id, sessionUser))) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }

        User user = userService.getUser(id);

        if(!userService.matchedPassword(user.getEmail(), form.getPassword())){
            return ResponseEntity.badRequest().body("유효하지 않은 비밀번호 입니다.");
        }

        try {
            user.setDelYn("Y");
            userService.patchUser(id,user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/confirm")
    public String confirmUser(@RequestParam String token,
                              @AuthenticationPrincipal SessionUser sessionUser) {

        User user = userService.getUserWithConfirmationByEmail(sessionUser.getEmail()).get();

        if(user.getEmailConfirmYn().equals("Y")) {
            return redirectMainUrl;
        }

        if(!user.getUserConfirmation().getConfirmToken().equals(token)) {
            return "/user/confirm/fail";
        }
        user.setEmailConfirmYn("Y");
        userService.patchUser(user.getUserId(), user);

        return "/user/confirm/success";
    }

    @PostMapping("/confirm/resend")
    @ResponseBody
    public ResponseEntity resendConfirmEmail(@AuthenticationPrincipal SessionUser sessionUser) {
        String fail = "가입되지 않은 이메일입니다.";
        User user;
        try {
            user = userService.getUserWithConfirmationByEmail(sessionUser.getEmail()).orElseThrow(() -> new UsernameNotFoundException(fail));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
        userService.resendConfirmationEmail(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findAccountForm")
    public String getFindAccountForm(@AuthenticationPrincipal SessionUser sessionUser) {
        if(sessionUser != null)
            return redirectMainUrl;

        return "/user/findAccountForm";
    }

    @PostMapping("/findAccount")
    @ResponseBody
    public ResponseEntity findAccount(@AuthenticationPrincipal SessionUser sessionUser,@RequestBody FindAccountForm findAccountForm) {
        if(sessionUser != null)
            return ResponseEntity.badRequest().build();

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


}

package com.supshop.suppingmall.user;

import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.page.BoardCriteria;
import com.supshop.suppingmall.page.PageMaker;
import com.supshop.suppingmall.user.Form.ApplySellerForm;
import com.supshop.suppingmall.user.Form.FindAccountForm;
import com.supshop.suppingmall.user.Form.SignUpForm;
import lombok.RequiredArgsConstructor;
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

    /* 나중에 MSA 전환 시 사용 */
//    @ResponseBody
//    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public User getUser(@PathVariable Long id) {
//        return userService.getUser(id);
//    }

    @ResponseBody
    @GetMapping(value = "/emails/{email}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Boolean> getUserByEmail(@PathVariable String email) {
        boolean userAlreadyExist;
        userAlreadyExist = userService.isUserAlreadyExistByEmail(email);
        if(userAlreadyExist) return ResponseEntity.ok(userAlreadyExist);
        return ResponseEntity.ok(userAlreadyExist);
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
    public String updateUser(@PathVariable Long id, User user, @AuthenticationPrincipal SessionUser sessionUser) {
        if(UserUtils.isOwner(id, sessionUser) || UserUtils.isAdmin(sessionUser)) {
            userService.updateUser(id, user);
            return redirectMainUrl;
        }
        return redirectLoginUrl;
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
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@RequestBody User user,
                                             @PathVariable Long id) {
//        if(isAdmin(sessionUser)) {
//            userService.patchUser(id,user);
//            return ResponseEntity.ok().build();
//        }
        try {
            userService.patchUser(id,user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /*

    ToDo 1. 추후 비밀번호 재확인을 통한 유저 검증 후
         2. 유저의 정보 변경과 비밀번호 변경을 분리할 예정

    @GetMapping("/checkForm")
    public String checkForm(@AuthenticationPrincipal SessionUser user) {
        if(user == null) {
            return redirectMainUrl;
        }
        return "/user/checkForm";
    }

    @PostMapping("/{id}/password")
    public String checkUser(@PathVariable Long id, @AuthenticationPrincipal SessionUser user, String password) {
        if(user == null || !user.getUserId().equals(id)) {
            return redirectMainUrl;
        }
        userService.matchedPassword(user.getEmail(),password);
        return "/user/checkForm";
    }

    @PutMapping("/{id}/password")
    @ResponseBody
    public String updateUserPassword(@PathVariable Long id, User user, @AuthenticationPrincipal SessionUser sessionUser) {
        if(isOwner(id, sessionUser) || isAdmin(sessionUser)) {
            userService.updateUser(id, user);
            return redirectMainUrl;
        }
        return redirectLoginUrl;
    }
    */

    @GetMapping("/{id}/form")
    public String getUserPage(@PathVariable Long id, Model model,@AuthenticationPrincipal SessionUser sessionUser) {

        // 관리자 일 경우 관리자 메인페이지로
        if(UserUtils.isAdmin(sessionUser)) {
            model.addAttribute("user",sessionUser);
            return "/user/admin/main";
        }

        return "/user/main";
    }

    @GetMapping("/{id}/updateForm")
    public String getUpdateForm(@PathVariable Long id, Model model, @AuthenticationPrincipal SessionUser sessionUser) {

        // 운영자 자격으로 해당 회원의 정보를 수정할 시 사용
        if(UserUtils.isAdmin(sessionUser)) {
            User user = userService.getUser(id);
            model.addAttribute("user",user);
            return "/user/adminUpdateForm";
        }

        // 개인화원 자격으로 자신의 회원 정보를 수정할 시 사용
        model.addAttribute("user",sessionUser);
        return "/user/updateForm";
    }

    @GetMapping("/seller")
    @ResponseBody
    public ResponseEntity<String> getSellerInfoByName(@RequestParam String type,
                                                      @RequestParam String value) {

        List<SessionUser> stores = userService.getStore(type, value);
        if(stores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();

    }

    @GetMapping("/seller/{id}")
    public String getSeller(@PathVariable Long id, Model model, @AuthenticationPrincipal SessionUser sessionUser) {
        if(UserUtils.isOwner(id, sessionUser)) {
            model.addAttribute("user", sessionUser);
            return "redirect:/user/"+id+"/form";
        }
        return redirectLoginUrl;
    }

    @GetMapping("/seller/applyForm")
    public String getApplySellerRoleForm(Model model, @AuthenticationPrincipal SessionUser sessionUser) {

        if(sessionUser.getRole().equals(Role.USER) && sessionUser.getStoreVO().getStoreApplyYn().equals("N")) {
            model.addAttribute("user", sessionUser);
            return "/user/applySellerForm";
        }
        return redirectLoginUrl;
    }

    @PostMapping("/seller/{id}/apply")
    public String applySellerRole(@PathVariable Long id,
                                  @Valid ApplySellerForm applyForm,
                                  @AuthenticationPrincipal SessionUser sessionUser) {
        if(UserUtils.isOwner(id, sessionUser)) {
            StoreVO store = modelMapper.map(applyForm, StoreVO.class);
            userService.patchUser(id,User.builder().storeVO(store).build());
            return "redirect:/users/"+id+"/form";
        }
        return redirectLoginUrl;
    }

    @GetMapping("/seller/applicants")
    public String getApplySeller(BoardCriteria boardCriteria,
                                 Model model,
                                 @AuthenticationPrincipal SessionUser sessionUser) {

        if(UserUtils.isAdmin(sessionUser)) {

            List<User> applySellerUsers = userService.getApplySellerUsers(boardCriteria);
            PageMaker pageMaker = new PageMaker(applySellerUsers.size(),pagingCount,boardCriteria);

            model.addAttribute("userList",applySellerUsers);
            model.addAttribute("pageMaker", pageMaker);
            return "/user/admin/applySellerList";
        }
        return redirectLoginUrl;
    }

    @PatchMapping("/seller/{id}/apply")
    @ResponseBody
    public ResponseEntity grantSellerRole(@PathVariable Long id, @AuthenticationPrincipal SessionUser sessionUser) {
        if(UserUtils.isAdmin(sessionUser)) {
            User user = userService.getUser(id);
            StoreVO storeVO = user.getStoreVO();
            storeVO.setStoreApplyYn("N");
            user.setStoreVO(storeVO);
            user.setRole(Role.SELLER);
            try {
                userService.patchUser(user.getUserId(), user);
            } catch (Exception e){
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
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
